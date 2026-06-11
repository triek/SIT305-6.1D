package com.example.sit305_61d

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class AccountSetupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setup)

        findViewById<Button>(R.id.createAccountButton).setOnClickListener {
            startActivity(Intent(this, InterestSelectionActivity::class.java))
        }
    }
}
