package com.example.sit305_61d

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiApiClientTest {
    @Test
    fun requestBodyUsesJsonArraysForContentsAndParts() {
        val requestBody = GeminiApiClient.buildGenerateContentRequestBody("Give me a hint")

        val root = JSONObject(requestBody)
        val contents = root.getJSONArray("contents")
        val content = contents.getJSONObject(0)
        val parts = content.getJSONArray("parts")

        assertEquals(1, contents.length())
        assertEquals(1, parts.length())
        assertEquals("Give me a hint", parts.getJSONObject(0).getString("text"))
        assertTrue(requestBody.contains("\"contents\":["))
        assertTrue(requestBody.contains("\"parts\":["))
    }
}
