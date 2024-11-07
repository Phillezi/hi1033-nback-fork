package mobappdev.example.nback_cimpl.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.screens.components.game.GameOver
import mobappdev.example.nback_cimpl.ui.screens.components.game.GameStartCountdown
import mobappdev.example.nback_cimpl.ui.screens.components.game.Grid
import mobappdev.example.nback_cimpl.ui.screens.components.game.ScoreDisplay
import mobappdev.example.nback_cimpl.ui.screens.components.game.actions.HomeButton
import mobappdev.example.nback_cimpl.ui.screens.components.game.actions.ResetButton
import mobappdev.example.nback_cimpl.ui.screens.components.game.actions.StartButton
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameProgressionState
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameState
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.game.Guess
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameScreen(
    vm: IGameVM, navigate: NavController
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val audioState by vm.audioState.collectAsState()
    val visualState by vm.visualState.collectAsState()
    val settingsState by vm.settingsVm.collectAsState()
    val sideLength by settingsState.sideLength.collectAsState()
    val score by vm.score.collectAsState()
    val N by settingsState.nBack.collectAsState()
    val nrOfCorrectGuesses by vm.nrOfCorrectGuesses.collectAsState()
    val gameType by settingsState.gameType.collectAsState()
    val isPaused = audioState.state == GameProgressionState.PAUSED || visualState.state == GameProgressionState.PAUSED
    val isOngoing =
        audioState.state == GameProgressionState.ONGOING || visualState.state == GameProgressionState.ONGOING
    val isOver = when (gameType) {
        GameType.Visual -> visualState.state == GameProgressionState.OVER
        GameType.Audio -> audioState.state == GameProgressionState.OVER
        GameType.AudioVisual -> audioState.state == GameProgressionState.OVER && visualState.state == GameProgressionState.OVER
    }
    val leaderBoardState by vm.leaderBoardVM.collectAsState()

    val currentDestination by navigate.currentBackStackEntryAsState()

    val turn = if (gameType == GameType.Visual || gameType == GameType.AudioVisual) {
        visualState.index
    } else {
        audioState.index
    }

    var showCountDownTimer by remember { mutableStateOf(false) }

    val shouldStartGame = !isOver && !isOngoing && !isPaused

    LaunchedEffect(currentDestination?.destination?.route) {
        if (currentDestination?.destination?.route != "game") {
            Log.d("GAME_EXIT", "Navigating away from game screen, ending game")
            vm.resetGame()
        } else if (shouldStartGame) {
            showCountDownTimer = true
        }
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HomeButton(vm, navigate = { navigate.navigate("home") })
                    if (!isOngoing) {
                        StartButton(vm)
                    } else {
                        ResetButton(vm)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isOngoing || isOver) {
                    if (isOver) {
                        GameOver(
                            onSubmit = { name ->
                                leaderBoardState.saveScore(
                                    name = name,
                                    score = score,
                                    )
                            },
                            navigate = { navigate.navigate("home") },
                            score = score,
                            highestScore = vm.highestPossibleScore(),
                        )
                    }
                    if (isLandscape) {
                        LandscapeLayout(
                            score = score,
                            N = N,
                            turn = turn,
                            nrOfCorrectGuesses = nrOfCorrectGuesses,
                            vm = vm,
                            visualState = visualState,
                            sideLength = sideLength,
                            navigate = { navigate to "home" },
                            gameType = gameType,
                        )
                    } else {
                        PortraitLayout(
                            score = score,
                            N = N,
                            turn = turn,
                            nrOfCorrectGuesses = nrOfCorrectGuesses,
                            vm = vm,
                            visualState = visualState,
                            sideLength = sideLength,
                            navigate = { navigate to "home" },
                            gameType = gameType,
                        )
                    }
                } else if (showCountDownTimer) {
                    GameStartCountdown(3) {
                        if(!settingsState.isPlaying.value) {
                            Log.d("GameVM", "Countdown triggered start")
                            vm.startGame();
                        }
                        showCountDownTimer = false;
                    }
                }
            }
        }
    )
}

@Composable
fun LandscapeLayout(
    score: Int,
    N: Int,
    turn: Int,
    nrOfCorrectGuesses: Int,
    vm: IGameVM,
    visualState: GameState,
    sideLength: Int,
    navigate: () -> Unit,
    gameType: GameType,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Column {
                ScoreDisplay(score, N, turn, nrOfCorrectGuesses)
                VisualAndAudio(vm, navigate, gameType)
            }
        }
        if (gameType != GameType.Audio) {
            Box(modifier = Modifier.weight(1f)) {
                Grid(vm, visualState, sideLength)
            }
        }
    }
}

@Composable
fun PortraitLayout(
    score: Int,
    N: Int,
    turn: Int,
    nrOfCorrectGuesses: Int,
    vm: IGameVM,
    visualState: GameState,
    sideLength: Int,
    navigate: () -> Unit,
    gameType: GameType,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScoreDisplay(score, N, turn, nrOfCorrectGuesses)
        if (gameType != GameType.Audio) {
            Grid(vm, visualState, sideLength)
        }
        VisualAndAudio(vm, navigate, gameType)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun VisualAndAudio(
    vm: IGameVM,
    navigate: () -> Unit,
    gameType: GameType,
) {
    val audioState by vm.audioState.collectAsState()
    val visualState by vm.visualState.collectAsState()
    val settingState by vm.settingsVm.collectAsState()

    val visualButtonColor = when (visualState.guess) {
        Guess.CORRECT -> Color.Green // Correct guess (Green)
        Guess.INCORRECT -> Color.Red // Incorrect guess (Red)
        else -> MaterialTheme.colorScheme.primary // Default fallback, in case of unexpected state
    }
    val audioButtonColor = when (audioState.guess) {
        Guess.CORRECT -> Color.Green // Correct guess (Green)
        Guess.INCORRECT -> Color.Red // Incorrect guess (Red)
        else -> MaterialTheme.colorScheme.primary // Default fallback, in case of unexpected state
    }

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (gameType == GameType.Audio || gameType == GameType.AudioVisual) {
                    Button(
                        onClick = { vm.checkAudioMatch() },
                        modifier = Modifier
                            .background(
                                color = audioButtonColor, shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp),
                        enabled = (audioState.index >= settingState.nBack.value)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sound_on),
                            contentDescription = "Sound",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                        )
                    }
                }
                if (gameType == GameType.Visual || gameType == GameType.AudioVisual) {
                    Button(
                        onClick = { vm.checkVisualMatch() },
                        modifier = Modifier
                            .background(
                                color = visualButtonColor, shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp),
                        enabled = (visualState.index >= settingState.nBack.value)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.visual),
                            contentDescription = "Visual",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                        )
                    }
                }
            }
        }
    }
}
/*
@Preview(showBackground = true, device = Devices.NEXUS_9, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun GameScreenLandscapePreview() {
    GameScreen(MockVM(MutableStateFlow(SMockVM()), MutableStateFlow(LBMockVM())), navigate = NavController())
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen(MockVM(MutableStateFlow(SMockVM()), MutableStateFlow(LBMockVM())), navigate = NavController())
}*/