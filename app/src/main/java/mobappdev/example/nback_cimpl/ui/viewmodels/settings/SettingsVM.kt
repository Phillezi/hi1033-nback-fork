package mobappdev.example.nback_cimpl.ui.viewmodels.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.data.repositories.UserPreferencesRepository
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ISettingsVM

class SettingsVM(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ISettingsVM, ViewModel() {

    private val _gameSettings = GameSettings()
    override val gameSettings: GameSettings get() = _gameSettings

    override fun setGameType(gameType: GameType) {
        _gameSettings.gameType.value = gameType
    }
    // TODO(check if i should refactor / remove)
    override fun setIsPlaying(isPlaying: Boolean) {
        _gameSettings.isPlaying.value = isPlaying
    }
    override fun setNBack(nBack: Int) {
        _gameSettings.nBack.value = 8.coerceAtMost(1.coerceAtLeast(nBack) +1) -1
    }
    override fun setSideLength(sideLength: Int) {
        _gameSettings.sideLength.value = 5.coerceAtMost(3.coerceAtLeast(sideLength))
    }
    override fun setNrOfTurns(nrOfTurns: Int) {
        _gameSettings.nrOfTurns.value = 25.coerceAtMost(2.coerceAtLeast(nrOfTurns))
    }
    override fun setPercentMatches(percentMatches: Int) {
        _gameSettings.percentMatches.value = percentMatches
    }
    override fun setAudioPercentMatches(audioPercentMatches: Int) {
        _gameSettings.audioPercentMatches.value = audioPercentMatches
    }
    override fun setEventInterval(eventInterval: Long) {
        _gameSettings.eventInterval.value = eventInterval
    }

    override val gameType: StateFlow<GameType> get() = _gameSettings.gameType
    override val isPlaying: StateFlow<Boolean> get() = _gameSettings.isPlaying
    override val nBack: StateFlow<Int> get() = _gameSettings.nBack
    override val sideLength: StateFlow<Int> get() = _gameSettings.sideLength
    override val nrOfTurns: StateFlow<Int> get() = _gameSettings.nrOfTurns
    override val percentMatches: StateFlow<Int> get() = _gameSettings.percentMatches
    override val audioPercentMatches: StateFlow<Int> get() = _gameSettings.audioPercentMatches
    override val eventInterval: StateFlow<Long> get() = _gameSettings.eventInterval

    override fun load() {
        viewModelScope.launch {
            userPreferencesRepository.n.collect {
                Log.d("LOAD", "NBack: $it")
                setNBack(it)
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.sideLength.collect {
                Log.d("LOAD", "SideLength: $it")
                setSideLength(it)
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.turns.collect {
                Log.d("LOAD", "NrOfTurns: $it")
                setNrOfTurns(it)
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.percent.collect {
                Log.d("LOAD", "PercentMatches: $it")
                setPercentMatches(it)
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.audioPercent.collect {
                Log.d("LOAD", "AudioPercentMatches: $it")
                setAudioPercentMatches(it)
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.time.collect {
                Log.d("LOAD", "EventInterval: $it")
                setEventInterval(it)
            }
        }
    }

    override fun save() {
        viewModelScope.launch {
            Log.d("SAVE", "NBack: ${nBack.value}")
            userPreferencesRepository.saveN(nBack.value)
        }
        viewModelScope.launch {
            Log.d("SAVE", "SideLength: ${sideLength.value}")
            userPreferencesRepository.saveSideLength(sideLength.value)
        }
        viewModelScope.launch {
            Log.d("SAVE", "NrOfTurns: ${nrOfTurns.value}")
            userPreferencesRepository.saveTurns(nrOfTurns.value)
        }
        viewModelScope.launch {
            Log.d("SAVE", "PercentMatches: ${percentMatches.value}")
            userPreferencesRepository.savePercent(percentMatches.value)
        }
        viewModelScope.launch {
            Log.d("SAVE", "AudioPercentMatches: ${audioPercentMatches.value}")
            userPreferencesRepository.saveAudioPercent(audioPercentMatches.value)
        }
        viewModelScope.launch {
            Log.d("SAVE", "EventInterval: ${eventInterval.value}")
            userPreferencesRepository.saveTime(eventInterval.value)
        }
    }

    init {
        load()
    }

    override fun onCleared() {
        super.onCleared()
        save()
    }
}