package mobappdev.example.nback_cimpl.ui.viewmodels.interfaces

import kotlinx.coroutines.flow.StateFlow
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameState
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameType

interface IGameVM {
    val gameState: StateFlow<GameState>
    val audioState: StateFlow<GameState>
    val visualState: StateFlow<GameState>
    val score: StateFlow<Int>
    val nrOfCorrectGuesses: StateFlow<Int>
    val highscore: StateFlow<Int>

    fun setGameType(gameType: GameType)
    fun startGame()
    fun checkVisualMatch()
    fun checkAudioMatch()
    fun resetGame()
    fun highestPossibleScore(): Int

    val settingsVm: StateFlow<ISettingsVM>
    val leaderBoardVM: StateFlow<ILeaderBoardVM>
}