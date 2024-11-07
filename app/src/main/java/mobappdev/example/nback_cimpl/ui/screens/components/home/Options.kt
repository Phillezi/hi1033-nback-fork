package mobappdev.example.nback_cimpl.ui.screens.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.screens.components.settings.ModeSelector
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM

@Composable
fun Options(vm: IGameVM, navigate: (location: String)->Unit, isLandscape: Boolean)  {
    val settingsState = vm.settingsVm.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isLandscape) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                ) {
                        Column {
                            ModeSelector(settingsState.value, isLandscape)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { navigate("game") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(12.dp),
                                    text = "Go to Game".uppercase(),
                                    style = MaterialTheme.typography.displaySmall
                                )
                            }
                        }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                ModeSelector(settingsState.value, isLandscape)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navigate("game") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = "Play",
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
    }
}