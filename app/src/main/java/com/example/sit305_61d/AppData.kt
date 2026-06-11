package com.example.sit305_61d

data class StudentProfile(
    val name: String,
    val email: String,
    val phone: String,
    val selectedInterests: MutableList<String>,
    val learningHistory: List<String>
)

data class SampleTask(
    val title: String,
    val description: String,
    val question: String,
    val options: List<String>,
    val followUpQuestion: String,
    val resultSummary: String
)

object AppData {
    var currentStudentName = "Alex Johnson"

    val studentProfile = StudentProfile(
        name = "Alex Johnson",
        email = "alex.johnson@student.deakin.edu.au",
        phone = "+61 400 123 456",
        selectedInterests = mutableListOf("Algorithms", "Data Structures"),
        learningHistory = listOf(
            "Completed Kotlin basics quiz",
            "Reviewed data structures flashcards",
            "Submitted UI navigation prototype"
        )
    )

    val availableInterests = listOf(
        "Algorithms",
        "Data Structures",
        "Web Development",
        "Testing",
        "Mobile Apps",
        "Databases",
        "Cybersecurity",
        "Cloud Computing"
    )

    fun updateSelectedInterests(interests: Collection<String>) {
        studentProfile.selectedInterests.clear()
        studentProfile.selectedInterests.addAll(interests)
    }

    fun currentTask(): SampleTask {
        val selected = studentProfile.selectedInterests
        return when {
            selected.any { it.equals("Web Development", ignoreCase = true) } -> SampleTask(
                title = "Responsive Web Layout Challenge",
                description = "Design a responsive card layout using your web development interest.",
                question = "Which CSS technique is best for a two-dimensional responsive grid?",
                options = listOf("CSS Grid", "Inline styles", "Absolute positioning"),
                followUpQuestion = "Explain how breakpoints can improve the layout on mobile screens.",
                resultSummary = "Good work practising responsive layouts and choosing CSS Grid for flexible two-dimensional designs."
            )
            selected.any { it.equals("Testing", ignoreCase = true) } -> SampleTask(
                title = "Unit Testing Practice",
                description = "Write tests for a simple student progress calculator.",
                question = "What should a unit test focus on first?",
                options = listOf("One small behaviour", "The whole application", "Manual UI checks only"),
                followUpQuestion = "Describe one edge case you would add to the calculator tests.",
                resultSummary = "Nice progress identifying focused unit tests and thinking about meaningful edge cases."
            )
            selected.any { it.equals("Data Structures", ignoreCase = true) } -> SampleTask(
                title = "Data Structure Selector",
                description = "Choose the right data structure for a student learning app feature.",
                question = "Which data structure is usually best for first-in, first-out processing?",
                options = listOf("Queue", "Stack", "Binary tree"),
                followUpQuestion = "Give one example of where a queue could be used in a learning app.",
                resultSummary = "Great job matching queue behaviour to first-in, first-out processing scenarios."
            )
            selected.any { it.equals("Algorithms", ignoreCase = true) } -> SampleTask(
                title = "Algorithm Warm-up",
                description = "Practise choosing an efficient approach for a search problem.",
                question = "What is the time complexity of binary search on a sorted list?",
                options = listOf("O(log n)", "O(n)", "O(n²)"),
                followUpQuestion = "Explain why the input must be sorted before binary search is used.",
                resultSummary = "Strong work recognising binary search complexity and the importance of sorted input."
            )
            else -> SampleTask(
                title = "Personal Learning Sprint",
                description = "Complete a short task based on your selected study interests.",
                question = "What is the best first step for learning a new technical topic?",
                options = listOf("Set a small goal", "Skip practice", "Memorise everything at once"),
                followUpQuestion = "Write one goal for your next study session.",
                resultSummary = "Well done setting up a focused study sprint with a practical next goal."
            )
        }
    }
}
