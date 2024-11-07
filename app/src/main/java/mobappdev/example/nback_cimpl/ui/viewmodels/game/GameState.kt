package mobappdev.example.nback_cimpl.ui.viewmodels.game

data class GameState(
    val index: Int = -1,
    val eventValue: Int = -1,
    val previousValue: Int = -1,
    val guess: Guess = Guess.NONE,
    val letter: String? = null,
    val state: GameProgressionState = GameProgressionState.NOT_STARTED,
)
