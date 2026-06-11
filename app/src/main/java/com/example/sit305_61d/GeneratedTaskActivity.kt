package com.example.sit305_61d

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.ComponentActivity

class GeneratedTaskActivity : ComponentActivity() {
    private val dummyAiDelayMs = 900L
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var aiPromptText: TextView
    private lateinit var aiResponseText: TextView
    private lateinit var aiFailureText: TextView
    private lateinit var aiLoadingIndicator: ProgressBar
    private lateinit var aiActionButtons: List<Button>

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

        setupLearningUtilities(task)

        findViewById<Button>(R.id.submitTaskButton).setOnClickListener {
            startActivity(Intent(this, ResultsActivity::class.java))
        }
    }

    private fun setupLearningUtilities(task: SampleTask) {
        aiPromptText = findViewById(R.id.aiPromptText)
        aiResponseText = findViewById(R.id.aiResponseText)
        aiFailureText = findViewById(R.id.aiFailureText)
        aiLoadingIndicator = findViewById(R.id.aiLoadingIndicator)

        val hintButton = findViewById<Button>(R.id.generateHintButton)
        val explainButton = findViewById<Button>(R.id.explainAnswerButton)
        val summaryButton = findViewById<Button>(R.id.summariseTopicButton)
        val flashcardsButton = findViewById<Button>(R.id.createFlashcardsButton)
        val studyPlanButton = findViewById<Button>(R.id.suggestStudyPlanButton)
        aiActionButtons = listOf(hintButton, explainButton, summaryButton, flashcardsButton, studyPlanButton)

        hintButton.setOnClickListener { requestLearningHelp(AiLearningAction.GENERATE_HINT, task) }
        explainButton.setOnClickListener { requestLearningHelp(AiLearningAction.EXPLAIN_ANSWER, task) }
        summaryButton.setOnClickListener { requestLearningHelp(AiLearningAction.SUMMARISE_TOPIC, task) }
        flashcardsButton.setOnClickListener { requestLearningHelp(AiLearningAction.CREATE_FLASHCARDS, task) }
        studyPlanButton.setOnClickListener { requestLearningHelp(AiLearningAction.SUGGEST_STUDY_PLAN, task) }
    }

    private fun requestLearningHelp(action: AiLearningAction, task: SampleTask) {
        val prompt = buildPrompt(action, task)
        aiPromptText.text = prompt
        aiResponseText.text = "Waiting for dummy AI response..."
        aiFailureText.visibility = View.GONE
        setLoadingState(true)

        handler.postDelayed({
            runCatching { dummyAiResponse(action, task) }
                .onSuccess { response ->
                    aiResponseText.text = response
                    aiFailureText.visibility = View.GONE
                }
                .onFailure {
                    aiResponseText.text = "No response available."
                    aiFailureText.text = "AI learning help failed. Please try again in a moment."
                    aiFailureText.visibility = View.VISIBLE
                }
            setLoadingState(false)
        }, dummyAiDelayMs)
    }

    private fun setLoadingState(isLoading: Boolean) {
        aiLoadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        aiActionButtons.forEach { it.isEnabled = !isLoading }
    }

    private fun buildPrompt(action: AiLearningAction, task: SampleTask): String {
        val selectedInterests = AppData.studentProfile.selectedInterests.joinToString()
        return """
            Student: ${AppData.currentStudentName}
            Interests: $selectedInterests
            Task: ${task.title}
            Description: ${task.description}
            Question: ${task.question}
            Options: ${task.options.joinToString()}
            Follow-up: ${task.followUpQuestion}

            Request: ${action.promptRequest}
        """.trimIndent()
    }

    private fun dummyAiResponse(action: AiLearningAction, task: SampleTask): String {
        return when (action) {
            AiLearningAction.GENERATE_HINT -> "Hint: Focus on the key phrase in the question. For '${task.question}', compare each option against the behaviour being described before choosing."
            AiLearningAction.EXPLAIN_ANSWER -> "Explanation: The strongest answer is '${task.options.first()}' because it directly matches the concept being tested. The other options are useful to recognise, but they do not fit this task as well."
            AiLearningAction.SUMMARISE_TOPIC -> "Topic summary: ${task.title} is about applying ${task.description.lowercase()} Keep the main idea, a practical example, and one limitation in mind while revising."
            AiLearningAction.CREATE_FLASHCARDS -> "Flashcards:\n1. Q: What is the core idea? A: ${task.options.first()} best matches the prompt.\n2. Q: What should you explain next? A: ${task.followUpQuestion}\n3. Q: How should you study it? A: Practise with one small example, then explain your reasoning aloud."
            AiLearningAction.SUGGEST_STUDY_PLAN -> "Study plan:\n• 5 min: Re-read the question and identify keywords.\n• 10 min: Review why '${task.options.first()}' is the best answer.\n• 10 min: Write a short response to '${task.followUpQuestion}' and check it against your notes."
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}

private enum class AiLearningAction(val promptRequest: String) {
    GENERATE_HINT("Generate one short hint without giving away the full answer."),
    EXPLAIN_ANSWER("Explain the best answer in beginner-friendly language."),
    SUMMARISE_TOPIC("Summarise the topic in three concise study points."),
    CREATE_FLASHCARDS("Create three flashcards for quick revision."),
    SUGGEST_STUDY_PLAN("Suggest a short study plan for this task.")
}
