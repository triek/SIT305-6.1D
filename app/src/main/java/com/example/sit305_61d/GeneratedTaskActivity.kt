package com.example.sit305_61d

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.ComponentActivity

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

        findViewById<Button>(R.id.submitTaskButton).setOnClickListener {
            startActivity(Intent(this, ResultsActivity::class.java))
        }
    }
}
