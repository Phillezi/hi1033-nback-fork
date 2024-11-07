package mobappdev.example.nback_cimpl.ui.screens.components.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameState
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.game.Guess
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM

@Composable
fun Grid(vm: IGameVM, gameState: GameState, sideLength: Int) {
    val settingsState = vm.settingsVm.collectAsState()
    val gameType by settingsState.value.gameType.collectAsState()
    val isPlaying by settingsState.value.isPlaying.collectAsState()
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        repeat(sideLength) { rowIndex ->
            Row {
                repeat(sideLength) { columnIndex ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(
                                if (
                                    gameType != GameType.Audio &&
                                    isPlaying &&
                                    rowIndex * sideLength + columnIndex == gameState.eventValue - 1
                                ) {
                                    if (gameState.guess === Guess.INCORRECT) {
                                        Color(245, 100, 100)
                                    } else {
                                        Color(177, 253, 132)
                                    }
                                } else {
                                    Color.LightGray
                                }
                            )
                    )
                }
            }
        }
    }
}