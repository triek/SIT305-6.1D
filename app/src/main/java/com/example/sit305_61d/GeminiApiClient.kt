package com.example.sit305_61d

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object GeminiApiClient {
    private const val MODEL_NAME = "gemini-2.5-flash"
    private const val GENERATE_CONTENT_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"
    private const val CONNECT_TIMEOUT_MS = 15_000
    private const val READ_TIMEOUT_MS = 60_000

    suspend fun generateContent(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY.trim()
        if (apiKey.isEmpty()) {
            throw IllegalStateException("Missing Gemini API key. Set GEMINI_API_KEY in gradle.properties or as an environment variable.")
        }

        val requestBody = JSONObject()
            .put(
                "contents",
                listOf(
                    JSONObject().put(
                        "parts",
                        listOf(JSONObject().put("text", prompt))
                    )
                )
            )
            .toString()

        val connection = (URL(GENERATE_CONTENT_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            setRequestProperty("x-goog-api-key", apiKey)
        }

        try {
            connection.outputStream.use { outputStream ->
                outputStream.write(requestBody.toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode
            val responseBody = if (responseCode in 200..299) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                val errorBody = connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
                throw IOException("Gemini API request failed with HTTP $responseCode: ${errorBody.ifBlank { connection.responseMessage }}")
            }

            val generatedText = JSONObject(responseBody)
                .optJSONArray("candidates")
                ?.optJSONObject(0)
                ?.optJSONObject("content")
                ?.optJSONArray("parts")
                ?.optJSONObject(0)
                ?.optString("text")
                ?.trim()
                .orEmpty()

            if (generatedText.isEmpty()) {
                throw IOException("Empty Gemini response: no text found at candidates[0].content.parts[0].text.")
            }

            generatedText
        } finally {
            connection.disconnect()
        }
    }
}
