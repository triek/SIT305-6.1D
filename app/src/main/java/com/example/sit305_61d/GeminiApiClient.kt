package com.example.sit305_61d

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class GeminiApiException(message: String) : Exception(message)

object GeminiApiClient {
    private const val MODEL_NAME = "gemini-2.5-flash"
    private const val GENERATE_CONTENT_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"
    private const val CONNECT_TIMEOUT_MS = 15_000
    private const val READ_TIMEOUT_MS = 60_000

    suspend fun generateContent(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY.trim()
        if (apiKey.isEmpty()) {
            throw IllegalStateException("Missing Gemini API key. Set GEMINI_API_KEY in local.properties.")
        }

        val requestBody = buildGenerateContentRequestBody(prompt)

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
                val errorDetails = parseGeminiErrorMessage(errorBody)
                    .ifBlank { connection.responseMessage ?: "No response details available." }
                throw GeminiApiException("Gemini API request failed with HTTP $responseCode: $errorDetails")
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
                throw GeminiApiException("Empty Gemini response: no text found at candidates[0].content.parts[0].text.")
            }

            generatedText
        } finally {
            connection.disconnect()
        }
    }

    internal fun buildGenerateContentRequestBody(prompt: String): String = JSONObject()
        .put(
            "contents",
            JSONArray().put(
                JSONObject().put(
                    "parts",
                    JSONArray().put(JSONObject().put("text", prompt))
                )
            )
        )
        .toString()

    private fun parseGeminiErrorMessage(errorBody: String): String {
        if (errorBody.isBlank()) return ""

        return runCatching {
            JSONObject(errorBody)
                .optJSONObject("error")
                ?.optString("message")
                .orEmpty()
        }.getOrDefault(errorBody)
    }
}
