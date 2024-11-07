package mobappdev.example.nback_cimpl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.modifiers.swipeNavigation
import mobappdev.example.nback_cimpl.ui.screens.GameScreen
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.screens.LeaderBoardScreen
import mobappdev.example.nback_cimpl.ui.screens.SettingsScreen
import mobappdev.example.nback_cimpl.ui.theme.NBack_CImplTheme
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameVM

/**
 * This is the MainActivity of the application
 *
 * Your navigation between the two (or more) screens should be handled here
 * For this application you need at least a homescreen (a start is already made for you)
 * and a gamescreen (you will have to make yourself, but you can use the same viewmodel)
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBack_CImplTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val gameViewModel: GameVM = viewModel(factory = GameVM.Factory)
                    val settingsState = gameViewModel.settingsVm.collectAsState()
                    val leaderBoardState = gameViewModel.leaderBoardVM.collectAsState()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(vm = gameViewModel, navigate={destination -> navController.navigate(destination)})
                        }
                        composable("game") {
                            GameScreen(vm = gameViewModel, navigate=navController,)
                        }
                        composable("settings") {
                            SettingsScreen(vm = settingsState.value, navigate = navController)
                        }
                        composable("leaderboard") {
                            LeaderBoardScreen(vm = leaderBoardState.value, navigate = navController)
                        }
                    }

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                vm = gameViewModel,
                                navigate={destination -> navController.navigate(destination)},
                                modifier = Modifier.swipeNavigation(
                                    navController = navController,
                                    leftDestination = "leaderboard",
                                    rightDestination = "settings"
                                )
                            )
                        }
                        composable("game") {
                            GameScreen(
                                vm = gameViewModel,
                                navigate=navController,
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                vm = settingsState.value,
                                navigate = navController,
                                modifier = Modifier.swipeNavigation(
                                    navController = navController,
                                    leftDestination = "home"
                                )
                            )
                        }
                        composable("leaderboard") {
                            LeaderBoardScreen(
                                vm = leaderBoardState.value,
                                navigate = navController,
                                modifier = Modifier.swipeNavigation(
                                    navController = navController,
                                    rightDestination = "home"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}