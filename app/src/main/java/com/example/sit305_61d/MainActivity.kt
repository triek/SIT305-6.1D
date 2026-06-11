package com.example.sit305_61d

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val welcomeMessage = "Welcome,\nStudent!\nLets Start Learning!"
        findViewById<TextView>(R.id.welcomeText).text = SpannableString(welcomeMessage).apply {
            val studentStart = welcomeMessage.indexOf("Student!")
            setSpan(
                RelativeSizeSpan(2f),
                studentStart,
                studentStart + "Student!".length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val typedName = findViewById<EditText>(R.id.usernameInput).text.toString().trim()
            AppData.currentStudentName = typedName.ifBlank { AppData.studentProfile.name }
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.createAccountLink).setOnClickListener {
            startActivity(Intent(this, AccountSetupActivity::class.java))
        }
    }
}
