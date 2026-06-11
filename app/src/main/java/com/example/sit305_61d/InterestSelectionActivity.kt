package com.example.sit305_61d

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class InterestSelectionActivity : ComponentActivity() {
    private val selectedInterests = linkedSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest_selection)

        selectedInterests.addAll(AppData.studentProfile.selectedInterests)
        setupInterestChips()

        findViewById<Button>(R.id.interestsNextButton).setOnClickListener {
            AppData.updateSelectedInterests(selectedInterests)
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

    private fun setupInterestChips() {
        val grid = findViewById<GridLayout>(R.id.interestsGrid)
        for (index in 0 until grid.childCount) {
            val chip = grid.getChildAt(index) as TextView
            val interest = chip.text.toString()
            chip.isSelected = selectedInterests.contains(interest)
            chip.setOnClickListener {
                if (selectedInterests.contains(interest)) {
                    selectedInterests.remove(interest)
                    chip.isSelected = false
                } else if (selectedInterests.size < 10) {
                    selectedInterests.add(interest)
                    chip.isSelected = true
                }
            }
        }
    }
}
