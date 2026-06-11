package com.example.sit305_61d

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LearningPromptBuilderTest {
    @Test
    fun hintPromptIncludesQuestionAndProtectsAnswer() {
        val prompt = LearningPromptBuilder.buildHintPrompt(
            topic = "Photosynthesis",
            question = "What gas do plants absorb?",
            studentInterests = "Gardening",
            learningHistory = "Needs help with plant biology"
        )

        assertTrue(prompt.contains("Photosynthesis"))
        assertTrue(prompt.contains("What gas do plants absorb?"))
        assertTrue(prompt.contains("Gardening"))
        assertTrue(prompt.contains("Do not reveal the final answer"))
    }

    @Test
    fun explanationPromptAsksForCorrectOrIncorrectReasoning() {
        val prompt = LearningPromptBuilder.buildExplanationPrompt(
            topic = "Fractions",
            question = "Which fraction equals 1/2?",
            selectedAnswer = "2/3",
            correctAnswer = "2/4"
        )

        assertTrue(prompt.contains("Selected answer: 2/3"))
        assertTrue(prompt.contains("Correct answer: 2/4"))
        assertTrue(prompt.contains("correct or incorrect"))
    }

    @Test
    fun flashcardPromptRequiresExactlyThreeFlashcards() {
        val prompt = LearningPromptBuilder.buildFlashcardsPrompt(topic = "Volcanoes")

        assertTrue(prompt.contains("exactly 3 flashcards"))
        assertTrue(prompt.contains("Question and Answer"))
    }

    @Test
    fun studyPlanPromptRequiresSevenLabelledDays() {
        val prompt = LearningPromptBuilder.buildStudyPlanPrompt(topic = "Algebra")

        assertTrue(prompt.contains("7-day study plan"))
        assertTrue(prompt.contains("exactly 7 lines"))
        assertTrue(prompt.contains("Day 1 to Day 7"))
    }

    @Test
    fun blankOptionalContextIsOmitted() {
        val prompt = LearningPromptBuilder.buildTopicSummaryPrompt(
            topic = "Gravity",
            studentInterests = "   ",
            learningHistory = ""
        )

        assertFalse(prompt.contains("Student interests:"))
        assertFalse(prompt.contains("Learning history:"))
    }
}
