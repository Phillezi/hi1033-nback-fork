package mobappdev.example.nback_cimpl.ui.viewmodels.interfaces;

import kotlinx.coroutines.flow.StateFlow
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.settings.GameSettings;

interface ISettingsVM {
    val gameSettings: GameSettings

    fun setGameType(gameType: GameType)
    fun setIsPlaying(isPlaying: Boolean)
    fun setNBack(nBack: Int)
    fun setSideLength(sideLength: Int)
    fun setNrOfTurns(nrOfTurns: Int)
    fun setPercentMatches(percentMatches: Int)
    fun setEventInterval(eventInterval: Long)

    val gameType: StateFlow<GameType>
    val isPlaying: StateFlow<Boolean>
    val nBack: StateFlow<Int>
    val sideLength: StateFlow<Int>
    val nrOfTurns: StateFlow<Int>
    val percentMatches: StateFlow<Int>
    val eventInterval: StateFlow<Long>

    fun load()
    fun save()
}
