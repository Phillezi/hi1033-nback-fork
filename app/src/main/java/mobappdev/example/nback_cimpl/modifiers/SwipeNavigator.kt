package mobappdev.example.nback_cimpl.modifiers

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavController

fun Modifier.swipeNavigation(
    navController: NavController,
    leftDestination: String? = null,
    rightDestination: String? = null,
    swipeThreshold: Float = 100f
): Modifier = this.pointerInput(Unit) {
    detectHorizontalDragGestures { _, dragAmount ->
        if (dragAmount > swipeThreshold && leftDestination != null) {
            navController.navigate(leftDestination)
        } else if (dragAmount < -swipeThreshold && rightDestination != null) {
            navController.navigate(rightDestination)
        }
    }
}
