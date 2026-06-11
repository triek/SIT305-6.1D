package com.example.sit305_61d

object LearningPromptBuilder {
    fun buildHintPrompt(
        topic: String,
        question: String,
        studentInterests: String = "",
        learningHistory: String = ""
    ): String = buildString {
        appendLine("You are a supportive tutor for a student learning app.")
        appendLine("Give one short hint for the question below.")
        appendLine("Do not reveal the final answer or solve it fully.")
        appendLine("Topic: ${topic.cleanForPrompt()}")
        appendLine("Question: ${question.cleanForPrompt()}")
        appendOptionalContext("Student interests", studentInterests)
        appendOptionalContext("Learning history", learningHistory)
        append("Return only the hint in 1-2 sentences.")
    }

    fun buildExplanationPrompt(
        topic: String,
        question: String,
        selectedAnswer: String,
        correctAnswer: String,
        learningHistory: String = ""
    ): String = buildString {
        appendLine("You are a clear tutor for a student learning app.")
        appendLine("Explain why the selected answer is correct or incorrect.")
        appendLine("Topic: ${topic.cleanForPrompt()}")
        appendLine("Question: ${question.cleanForPrompt()}")
        appendLine("Selected answer: ${selectedAnswer.cleanForPrompt()}")
        appendLine("Correct answer: ${correctAnswer.cleanForPrompt()}")
        appendOptionalContext("Learning history", learningHistory)
        append("Return a simple explanation in 3-5 short sentences.")
    }

    fun buildTopicSummaryPrompt(
        topic: String,
        studentInterests: String = "",
        learningHistory: String = ""
    ): String = buildString {
        appendLine("You are a concise tutor for a student learning app.")
        appendLine("Summarise this topic for a student.")
        appendLine("Topic: ${topic.cleanForPrompt()}")
        appendOptionalContext("Student interests", studentInterests)
        appendOptionalContext("Learning history", learningHistory)
        append("Return 4-6 short bullet points using simple language.")
    }

    fun buildFlashcardsPrompt(
        topic: String,
        studentInterests: String = "",
        learningHistory: String = ""
    ): String = buildString {
        appendLine("You are creating flashcards for a student learning app.")
        appendLine("Create exactly 3 flashcards for this topic.")
        appendLine("Topic: ${topic.cleanForPrompt()}")
        appendOptionalContext("Student interests", studentInterests)
        appendOptionalContext("Learning history", learningHistory)
        append("Return only a numbered list with Question and Answer for each flashcard.")
    }

    fun buildStudyPlanPrompt(
        topic: String,
        studentInterests: String = "",
        learningHistory: String = ""
    ): String = buildString {
        appendLine("You are a study coach for a student learning app.")
        appendLine("Create a simple 7-day study plan for this topic.")
        appendLine("Topic: ${topic.cleanForPrompt()}")
        appendOptionalContext("Student interests", studentInterests)
        appendOptionalContext("Learning history", learningHistory)
        append("Return exactly 7 lines labelled Day 1 to Day 7, with one short task per day.")
    }

    private fun StringBuilder.appendOptionalContext(label: String, value: String) {
        val cleanedValue = value.cleanForPrompt()
        if (cleanedValue.isNotBlank()) {
            appendLine("$label: $cleanedValue")
        }
    }

    private fun String.cleanForPrompt(): String = trim().replace(Regex("\\s+"), " ")
}
