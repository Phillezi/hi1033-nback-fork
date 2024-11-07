package mobappdev.example.nback_cimpl.ui.viewmodels.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ILeaderBoardVM
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ISettingsVM

class MockVM(override val settingsVm: StateFlow<ISettingsVM>,
             override val leaderBoardVM: StateFlow<ILeaderBoardVM>
) : IGameVM {
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val audioState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val visualState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val nrOfCorrectGuesses: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()

    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun checkVisualMatch() {
    }

    override fun checkAudioMatch() {
    }

    override fun resetGame() {
    }

    override fun highestPossibleScore(): Int {
        return 0;
    }

}