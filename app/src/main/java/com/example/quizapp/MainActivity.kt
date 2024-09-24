package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizApp()
        }
    }
}

@Composable
fun QuizApp() {
    var questionIndex by remember { mutableIntStateOf(0) }
    var answerInput by remember { mutableStateOf("") }
    var isQuizComplete by remember { mutableStateOf(false) }

    val questions = listOf(
        "What is the capital of France?" to "Paris",
        "What is 2 + 2?" to "4",
        "What is the capital of Japan?" to "Tokyo",
        "What is the capital of China?" to "Beijing"
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            if (isQuizComplete) {
                Button(onClick = {
                    questionIndex = 0
                    answerInput = ""
                    isQuizComplete = false
                }) {
                    Text("Restart Quiz")
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = questions[questionIndex].first,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // 答案输入框
                BasicTextField(
                    value = answerInput,
                    onValueChange = { answerInput = it },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                // 提交按钮
                Button(
                    onClick = {
                        val correctAnswer = questions[questionIndex].second
                        if (answerInput.equals(correctAnswer, ignoreCase = true)) {
                            questionIndex++
                            if (questionIndex >= questions.size) {
                                isQuizComplete = true
                            } else {
                                answerInput = ""
                            }
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Correct!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Incorrect! Try again.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Submit Answer")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppPreview() {
    QuizAppTheme {
        QuizApp()
    }
}
