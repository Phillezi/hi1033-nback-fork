package mobappdev.example.nback_cimpl.ui.screens.components.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsSlider(
    min: Int,
    max: Int,
    currentValue: Int,
    onValueChange: (Int) -> Unit,
    steps: Int? = null
) {
    val calculatedSteps = steps ?: (max - min)

    Slider(
        value = currentValue.toFloat(),
        onValueChange = { newValue -> onValueChange(newValue.toInt()) },
        valueRange = min.toFloat()..max.toFloat(),
        steps = calculatedSteps - 1,
        modifier = Modifier.fillMaxWidth()
    )
}
