package com.example.dogday.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogday.ui.theme.ButtonColorLight
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.dogday.models.getRecommendedDogID


@Composable
fun DogQuizScreen(navController: NavController) {
    val questions = listOf(
        "What is your activity level?" to listOf("Low", "Moderate", "High"),
        "How much space do you have for a dog?" to listOf("Small", "Medium", "Large"),
        "How experienced are you with dogs?" to listOf("Beginner", "Intermediate", "Advanced")
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(mutableListOf<String>()) }
    val isLastQuestion = currentQuestionIndex == questions.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Find the right dog breed for you!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        QuizQuestion(
            question = questions[currentQuestionIndex].first,
            answers = questions[currentQuestionIndex].second,
            onAnswerSelected = { answer ->
                if (selectedAnswers.size <= currentQuestionIndex) {
                    selectedAnswers.add(answer)
                } else {
                    selectedAnswers[currentQuestionIndex] = answer
                }

                if (isLastQuestion) {
                    val recommendedDogID = getRecommendedDogID(selectedAnswers)
                    navController.navigate("quiz_results/${recommendedDogID.name}")
                } else {
                    currentQuestionIndex++
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLastQuestion) {
            Button(
                onClick = {
                    val recommendedDogID = getRecommendedDogID(selectedAnswers)
                    navController.navigate("quiz_results/${recommendedDogID.name}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text("Submit Quiz", style = MaterialTheme.typography.bodyLarge)
            }
        }

    }
}

@Composable
fun QuizQuestion(
    question: String,
    answers: List<String>,
    onAnswerSelected: (String) -> Unit
) {
    Text(
        question,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    answers.forEach { answer ->
        Button(
            onClick = { onAnswerSelected(answer) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight)
        ) {
            Text(answer, style = MaterialTheme.typography.bodyLarge)
        }
    }
}