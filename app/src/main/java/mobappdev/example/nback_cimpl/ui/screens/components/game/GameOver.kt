package mobappdev.example.nback_cimpl.ui.screens.components.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun GameOver(
    onSubmit: suspend (String) -> Unit,
    navigate: () -> Unit,
    score: Int,
    highestScore: Int,
) {
    val playerName = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = { navigate.invoke() },
        title = { Text("You scored $score/$highestScore points!") },
        text = {
            Column {
                Text("Save your score")

                TextField(
                    value = playerName.value,
                    onValueChange = { playerName.value = it },
                    label = { Text("Enter your name") },
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            coroutineScope.launch {
                                onSubmit(playerName.value)
                            }
                            navigate.invoke()
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        onSubmit(playerName.value)
                    }
                    navigate.invoke()
                }
            ) {
                Text("Save Score")
            }
        }
    )
}
