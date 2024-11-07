package mobappdev.example.nback_cimpl.ui.screens.components.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GameModeButton(
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(8.dp),
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Game Mode Icon",
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp)
        )
    }
}