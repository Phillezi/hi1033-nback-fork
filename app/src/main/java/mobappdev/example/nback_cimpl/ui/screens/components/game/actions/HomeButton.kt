package mobappdev.example.nback_cimpl.ui.screens.components.game.actions

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM

@Composable
fun HomeButton(vm: IGameVM, navigate: () -> Unit) {
    Button(onClick = {
        navigate.invoke()
        vm.resetGame()
    }) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Home",
            tint = Color.Black,
            modifier = Modifier.size(20.dp).padding(end = 4.dp)
        )
    }
}
