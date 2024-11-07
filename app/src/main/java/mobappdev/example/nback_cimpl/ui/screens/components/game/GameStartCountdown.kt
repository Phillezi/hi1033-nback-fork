package mobappdev.example.nback_cimpl.ui.screens.components.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun GameStartCountdown(
    startTimeInSeconds: Int,
    onStart: () -> Unit
) {

    var timeLeft by remember { mutableStateOf(startTimeInSeconds) }
    var gameStarted by remember { mutableStateOf(false) }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L) // 1 second delay
            timeLeft -= 1
        } else if (!gameStarted) {
            onStart() // Trigger game start when countdown reaches zero
            gameStarted = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (timeLeft > 0) {
                "$timeLeft"
            } else {
                "Go!"
            },
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
