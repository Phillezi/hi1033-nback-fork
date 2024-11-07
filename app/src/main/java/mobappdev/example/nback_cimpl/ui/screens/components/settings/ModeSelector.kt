package mobappdev.example.nback_cimpl.ui.screens.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ISettingsVM

@Composable
fun ModeSelector(
    vm: ISettingsVM,
    isLandscape: Boolean
) {
    val gameType by vm.gameType.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameModeButton(
                iconRes = R.drawable.visual,
                isSelected = gameType == GameType.Visual,
                onClick = { vm.setGameType(GameType.Visual) }
            )
            GameModeButton(
                iconRes = R.drawable.sound_on,
                isSelected = gameType == GameType.Audio,
                onClick = { vm.setGameType(GameType.Audio) }
            )
            GameModeButton(
                iconRes = R.drawable.both,
                isSelected = gameType == GameType.AudioVisual,
                onClick = { vm.setGameType(GameType.AudioVisual) }
            )
        }
    }
}