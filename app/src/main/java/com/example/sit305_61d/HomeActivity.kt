package com.example.sit305_61d

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onResume() {
        super.onResume()
        val task = AppData.currentTask()
        findViewById<TextView>(R.id.homeGreetingText).text = "Hello,\n${AppData.studentProfile.name}"
        findViewById<TextView>(R.id.taskTitleText).text = task.title
        findViewById<TextView>(R.id.taskDescriptionText).text = task.description
        findViewById<TextView>(R.id.notificationText).text = "You have 1 task due today"
        findViewById<TextView>(R.id.interestsSummaryText).text = "Interests: ${AppData.studentProfile.selectedInterests.joinToString()}"

        findViewById<ImageButton>(R.id.openTaskButton).setOnClickListener {
            startActivity(Intent(this, GeneratedTaskActivity::class.java))
        }
    }
}
