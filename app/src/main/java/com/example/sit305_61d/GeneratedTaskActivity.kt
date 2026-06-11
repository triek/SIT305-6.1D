package com.example.sit305_61d

import android.content.Intent
import android.os.Bundle
import android.widget.Button as AndroidButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sit305_61d.ui.theme.SIT30561DTheme
import kotlinx.coroutines.launch
import java.io.IOException

class GeneratedTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generated_task)

        val task = AppData.currentTask()
        findViewById<TextView>(R.id.generatedTaskTitleText).text = task.title
        findViewById<TextView>(R.id.generatedTaskDescriptionText).text = task.description
        findViewById<TextView>(R.id.questionOneTitleText).text = "1. ${task.question}"
        findViewById<RadioButton>(R.id.answerOneRadio).text = task.options[0]
        findViewById<RadioButton>(R.id.answerTwoRadio).text = task.options[1]
        findViewById<RadioButton>(R.id.answerThreeRadio).text = task.options[2]
        findViewById<TextView>(R.id.questionTwoTitleText).text = "2. ${task.followUpQuestion}"

        val answerGroup = findViewById<RadioGroup>(R.id.questionOneAnswerGroup)
        findViewById<ComposeView>(R.id.aiLearningUtilitiesCompose).setContent {
            SIT30561DTheme {
                AiLearningUtilities(
                    task = task,
                    selectedAnswerProvider = {
                        val selectedAnswerId = answerGroup.checkedRadioButtonId
                        if (selectedAnswerId == -1) {
                            ""
                        } else {
                            answerGroup.findViewById<RadioButton>(selectedAnswerId)
                                ?.text
                                ?.toString()
                                .orEmpty()
                        }
                    }
                )
            }
        }

        findViewById<AndroidButton>(R.id.submitTaskButton).setOnClickListener {
            startActivity(Intent(this, ResultsActivity::class.java))
        }
    }
}

@Composable
private fun AiLearningUtilities(
    task: SampleTask,
    selectedAnswerProvider: () -> String
) {
    var promptText by remember { mutableStateOf("Tap an AI learning help button to preview the prompt.") }
    var aiResponseText by remember { mutableStateOf("The Gemini response will appear below the prompt.") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    fun runGeminiPrompt(prompt: String) {
        promptText = prompt
        aiResponseText = ""
        errorMessage = ""
        isLoading = true

        coroutineScope.launch {
            runCatching { GeminiApiClient.generateContent(prompt) }
                .onSuccess { response ->
                    aiResponseText = response
                }
                .onFailure { error ->
                    aiResponseText = "No Gemini response available."
                    errorMessage = error.toReadableMessage()
                }
            isLoading = false
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.card_blue))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "AI Learning Utilities",
                color = colorResource(id = R.color.card_text_white),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Use Gemini to get quick help for this task.",
                color = colorResource(id = R.color.card_text_white),
                fontSize = 14.sp
            )

            AiActionButton(
                text = "Generate Hint",
                isLoading = isLoading,
                onClick = {
                    runGeminiPrompt(
                        LearningPromptBuilder.buildHintPrompt(
                            topic = task.title,
                            question = task.question,
                            studentInterests = studentInterestsText(),
                            learningHistory = learningHistoryText()
                        )
                    )
                }
            )
            AiActionButton(
                text = "Explain Answer",
                isLoading = isLoading,
                onClick = {
                    runGeminiPrompt(
                        LearningPromptBuilder.buildExplanationPrompt(
                            topic = task.title,
                            question = task.question,
                            selectedAnswer = selectedAnswerProvider().ifBlank { "No answer selected yet" },
                            correctAnswer = task.options.first(),
                            learningHistory = learningHistoryText()
                        )
                    )
                }
            )
            AiActionButton(
                text = "Create Flashcards",
                isLoading = isLoading,
                onClick = {
                    runGeminiPrompt(
                        LearningPromptBuilder.buildFlashcardsPrompt(
                            topic = task.title,
                            studentInterests = studentInterestsText(),
                            learningHistory = learningHistoryText()
                        )
                    )
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = colorResource(id = R.color.card_text_white))
                }
                Text(
                    text = if (isLoading) "Loading Gemini response..." else "Tap a button to ask Gemini for help.",
                    color = colorResource(id = R.color.card_text_white),
                    fontSize = 13.sp
                )
            }

            AiTextSection(title = "Prompt sent to LLM", body = promptText, fontSize = 13)
            AiTextSection(title = "AI response", body = aiResponseText, fontSize = 14)

            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFFCDD2),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AiActionButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.action_green),
            contentColor = colorResource(id = R.color.text_black)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AiTextSection(
    title: String,
    body: String,
    fontSize: Int
) {
    Text(
        text = title,
        color = colorResource(id = R.color.card_text_white),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = body,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.field_white))
            .padding(12.dp),
        color = colorResource(id = R.color.text_black),
        fontSize = fontSize.sp
    )
}

private fun studentInterestsText(): String = AppData.studentProfile.selectedInterests.joinToString()

private fun learningHistoryText(): String = AppData.studentProfile.learningHistory.joinToString()

private fun Throwable.toReadableMessage(): String = when (this) {
    is GeminiApiException -> message ?: "Gemini request failed. Please check your API key and try again."
    is IllegalStateException -> message ?: "Gemini is not configured yet. Please check the API key."
    is IOException -> "Could not reach Gemini. Please check your internet connection and try again."
    else -> "Gemini request failed: ${message ?: "Please try again."}"
}
