package mobappdev.example.nback_cimpl.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.screens.components.settings.SettingsSlider
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ISettingsVM

data class Setting(
    val name: String,
    val min: Int,
    val max: Int,
    val currentValue: Int,
    val steps: Int? = null,
    val onValueChange: (Int) -> Unit
)

@Composable
fun GameSettings(vm: ISettingsVM) {
    val nBack by vm.nBack.collectAsState()
    val sideLength by vm.sideLength.collectAsState()
    val nrOfTurns by vm.nrOfTurns.collectAsState()
    val percentMatches by vm.percentMatches.collectAsState()
    val eventInterval by vm.eventInterval.collectAsState()

    LaunchedEffect(nBack) {
        if (nrOfTurns < nBack+2) {
            vm.setNrOfTurns(nBack+2)
        }
    }

    LaunchedEffect(nrOfTurns) {
        if (nrOfTurns < nBack+2) {
            vm.setNBack(nrOfTurns-2)
        }
    }

    val settingsList = listOf(Setting("NBack", 1, 7, nBack) { vm.setNBack(it) },
        Setting("Side Length", 3, 5, sideLength) { vm.setSideLength(it) },
        Setting("Turns", nBack + 2, 25, nrOfTurns) { vm.setNrOfTurns(it) },
        Setting("Percent Matches", 10, 50, percentMatches) { vm.setPercentMatches(it) },
        Setting("Event Interval", 500, 4000, eventInterval.toInt(), 35) { vm.setEventInterval(it.toLong()) })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        settingsList.forEach { setting ->
            Text(text = "${setting.name}: ${setting.currentValue}")
            SettingsSlider(
                min = setting.min,
                max = setting.max,
                currentValue = setting.currentValue,
                onValueChange = setting.onValueChange,
                steps = setting.steps
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}