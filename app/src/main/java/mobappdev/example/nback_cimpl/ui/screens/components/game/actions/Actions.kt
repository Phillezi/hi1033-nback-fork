package mobappdev.example.nback_cimpl.ui.screens.components.game.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM

@Composable
fun Actions(vm: IGameVM, navigate: ()-> Unit) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StartButton(vm)
        HomeButton(vm, navigate)
        ResetButton(vm)
    }
}