package mobappdev.example.nback_cimpl.ui.viewmodels.settings

import kotlinx.coroutines.flow.StateFlow
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ISettingsVM

class SMockVM: ISettingsVM {
    override val gameSettings: GameSettings
        get() = TODO("Not yet implemented")

    override fun setGameType(gameType: GameType) {
        TODO("Not yet implemented")
    }

    override fun setIsPlaying(isPlaying: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setNBack(nBack: Int) {
        TODO("Not yet implemented")
    }

    override fun setSideLength(sideLength: Int) {
        TODO("Not yet implemented")
    }

    override fun setNrOfTurns(nrOfTurns: Int) {
        TODO("Not yet implemented")
    }

    override fun setPercentMatches(percentMatches: Int) {
        TODO("Not yet implemented")
    }

    override fun setEventInterval(eventInterval: Long) {
        TODO("Not yet implemented")
    }

    override val gameType: StateFlow<GameType>
        get() = TODO("Not yet implemented")
    override val isPlaying: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val nBack: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val sideLength: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val nrOfTurns: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val percentMatches: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val eventInterval: StateFlow<Long>
        get() = TODO("Not yet implemented")

    override fun load() {
        TODO("Not yet implemented")
    }

    override fun save() {
        TODO("Not yet implemented")
    }
}