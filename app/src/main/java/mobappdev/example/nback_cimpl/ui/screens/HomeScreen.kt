package mobappdev.example.nback_cimpl.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.screens.components.home.GameInfoCard
import mobappdev.example.nback_cimpl.ui.screens.components.home.Options
import mobappdev.example.nback_cimpl.ui.viewmodels.game.MockVM
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM
import mobappdev.example.nback_cimpl.ui.viewmodels.leaderboard.LBMockVM
import mobappdev.example.nback_cimpl.ui.viewmodels.settings.SMockVM

/**
 * This is the Home screen composable
 *
 * Currently this screen shows the saved highscore
 * It also contains a button which can be used to show that the C-integration works
 * Furthermore it contains two buttons that you can use to start a game
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */
@Composable
fun HomeScreen(
    vm: IGameVM,
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        modifier = modifier,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navigate("leaderboard") },
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(30.dp)
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.trophy_icon, ),
                            contentDescription = "Leaderboard",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = { navigate("settings") },
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(30.dp)
                            )
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }

                Box{
                    GameInfoCard()
                }

                Options(vm, navigate, isLandscape)
            }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    Surface() {
        HomeScreen(
            MockVM(MutableStateFlow(SMockVM()), MutableStateFlow(LBMockVM())),
            navigate = { "game" })
    }
}

@Preview(showBackground = true, device = Devices.NEXUS_9, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HomeScreenLandscapePreview() {
    Surface() {
        HomeScreen(
            MockVM(MutableStateFlow(SMockVM()), MutableStateFlow(LBMockVM())),
            navigate = { "game" })
    }
}