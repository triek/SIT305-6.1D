package com.example.sit305_61d

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val task = AppData.currentTask()
        findViewById<TextView>(R.id.resultTaskTitleText).text = task.title
        findViewById<TextView>(R.id.resultQuestionOneText).text = "1. ${task.question}"
        findViewById<TextView>(R.id.resultQuestionTwoText).text = "2. ${task.followUpQuestion}"
        findViewById<TextView>(R.id.resultSummaryText).text = task.resultSummary

        findViewById<Button>(R.id.continueButton).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }
    }
}
