package mobappdev.example.nback_cimpl.ui.viewmodels.game

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.repositories.UserPreferencesRepository
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.IGameVM
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ILeaderBoardVM
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ISettingsVM
import mobappdev.example.nback_cimpl.ui.viewmodels.leaderboard.LeaderBoardVM
import mobappdev.example.nback_cimpl.ui.viewmodels.settings.SettingsVM
import java.util.Arrays

/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */
class GameVM(
    private val _settingsVM: ISettingsVM,
    private val _leaderBoardVM: ILeaderBoardVM,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val _application: GameApplication,
) : IGameVM, ViewModel() {
    // TODO(do better)
    private val _settingsVMState = MutableStateFlow<ISettingsVM>(_settingsVM)
    private val _leaderBoardVMState = MutableStateFlow<ILeaderBoardVM>(_leaderBoardVM)

    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private val _audioState = MutableStateFlow(GameState())
    override val audioState: StateFlow<GameState>
        get() = _audioState

    private val _visualState = MutableStateFlow(GameState())
    override val visualState: StateFlow<GameState>
        get() = _visualState

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _nrOfCorrectGuesses = MutableStateFlow(0)
    override val nrOfCorrectGuesses: StateFlow<Int>
        get() = _nrOfCorrectGuesses

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore

    private var job: Job? = null  // coroutine job for the game event
    private var _textToSpeech: TextToSpeech? = null

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var visualEvents = emptyArray<Int>()  // Array with all events
    private var audioEvents = emptyArray<Int>()
    override fun setGameType(gameType: GameType) {
        _settingsVM.setGameType(gameType)
    }

    override fun startGame() {
        job?.cancel()

        Log.d("GameVm", "Starting game with settings: ${_settingsVM.gameSettings}")

        visualEvents = nBackHelper.generateNBackString(
            _settingsVM.nrOfTurns.value,
            _settingsVM.sideLength.value * _settingsVM.sideLength.value,
            _settingsVM.percentMatches.value,
            _settingsVM.nBack.value
        ).toList().toTypedArray()

        audioEvents = nBackHelper.generateNBackString(
            _settingsVM.nrOfTurns.value,
            _settingsVM.sideLength.value * _settingsVM.sideLength.value,
            _settingsVM.percentMatches.value,
            _settingsVM.nBack.value
        ).toList().toTypedArray()

        Log.d("GameVM", "The following sequence was generated: ${visualEvents.contentToString()}")
        Log.d("GameVM", "The following sequence was generated: ${audioEvents.map { intToLetter(it) }.toList()}")

        job = viewModelScope.launch {
            when (_settingsVM.gameType.value) {
                GameType.Audio -> runAudioGame(audioEvents)
                GameType.AudioVisual -> runAudioVisualGame(audioEvents, visualEvents)
                GameType.Visual -> runVisualGame(visualEvents)
            }
            if (_highscore.value < _score.value) {
                userPreferencesRepository.saveHighScore(_score.value)
            }
            Log.d("Saving", _settingsVM.sideLength.value.toString())
            userPreferencesRepository.saveSideLength(_settingsVM.sideLength.value)
            userPreferencesRepository.saveN(_settingsVM.nBack.value)
            userPreferencesRepository.saveTurns(_settingsVM.nrOfTurns.value)
            userPreferencesRepository.savePercent(_settingsVM.percentMatches.value)
            userPreferencesRepository.saveTime(_settingsVM.eventInterval.value)
        }
    }

    override fun checkVisualMatch() {
        if (_visualState.value.eventValue == -1 || Guess.NONE != _visualState.value.guess || _visualState.value.index < _settingsVM.nBack.value) return
        val currentValue = _visualState.value.eventValue
        val previousValue = _visualState.value.previousValue
        val guess: Guess
        if (previousValue != -1 && currentValue == previousValue) {
            _nrOfCorrectGuesses.value += 1
            _score.value += 1
            guess = Guess.CORRECT
        } else {
            _score.value = 0.coerceAtLeast(_score.value - 1)
            guess = Guess.INCORRECT
        }
        _visualState.value = _visualState.value.copy(guess = guess)
    }

    override fun checkAudioMatch() {
        if (_audioState.value.eventValue == -1 || Guess.NONE != _audioState.value.guess || _audioState.value.index < _settingsVM.nBack.value) return
        val currentValue = _audioState.value.eventValue
        val previousValue = _audioState.value.previousValue
        val guess: Guess
        if (previousValue != -1 && currentValue == previousValue) {
            _nrOfCorrectGuesses.value += 1
            _score.value = 1
            guess = Guess.CORRECT
        } else {
            _score.value = 0.coerceAtLeast(_score.value - 1)
            guess = Guess.INCORRECT
        }
        _audioState.value = _audioState.value.copy(guess = guess)
    }

    override fun highestPossibleScore(): Int {
        return when (_settingsVM.gameType.value) {
            GameType.Audio -> getHighestScore(GameType.Audio, audioEvents)
            GameType.AudioVisual -> getHighestScore(GameType.Audio, audioEvents) + getHighestScore(GameType.Visual, visualEvents)
            GameType.Visual -> getHighestScore(GameType.Visual, visualEvents)
        }
    }

    fun getHighestScore(type: GameType, events: Array<Int>): Int {
        val isAudio = type == GameType.Audio
        var matchesCount = 0

        val nBack = _settingsVM.nBack.value

        for (i in nBack until events.size) {
            if (i - nBack < 0) continue
            if (isAudio) {
                if (events[i] % 26 == events[i - nBack] % 26) {
                    matchesCount++
                }
            } else {
                if (events[i] == events[i - nBack]) {
                    matchesCount++
                }
            }
        }

        return matchesCount
    }

    override fun resetGame() {
        resetGame(_visualState)
        resetGame(_audioState)
    }

    override val settingsVm: StateFlow<ISettingsVM>
        get() = _settingsVMState

    override val leaderBoardVM: StateFlow<ILeaderBoardVM>
        get() = _leaderBoardVMState

    private fun resetGame(gameState: MutableStateFlow<GameState>) {
        job?.cancel()
        gameState.value = gameState.value.copy(
            guess = Guess.NONE,
            eventValue = -1,
            previousValue = -1,
            index = -1,
            state = GameProgressionState.NOT_STARTED,
        )
        _nrOfCorrectGuesses.value = 0
        _settingsVM.setIsPlaying(false)
        _score.value = 0
    }

    private suspend fun runAudioGame(events: Array<Int>) {
        resetGame()
        _settingsVM.setGameType(GameType.Audio)
        var previousValue: Int = -1
        _settingsVM.setIsPlaying(true)
        for (i in events.indices) {
            if (i >= _settingsVM.nBack.value) {
                previousValue = events[i - _settingsVM.nBack.value]%26
            }
            val letter = intToLetter(events[i])
            _audioState.value = _audioState.value.copy(
                eventValue = events[i]%26,
                previousValue = previousValue,
                guess = Guess.NONE,
                letter = letter,
                index = i,
                state = GameProgressionState.ONGOING,
            )
            _textToSpeech?.speak(letter, TextToSpeech.QUEUE_FLUSH, null, null)
            delay(_settingsVM.eventInterval.value)
            previousValue = -1
        }
        _settingsVM.setIsPlaying(false)
        _audioState.value = _audioState.value.copy(state = GameProgressionState.OVER)
    }

    private suspend fun runVisualGame(events: Array<Int>) {
        resetGame()
        _settingsVM.setGameType(GameType.Visual)
        var previousValue: Int = -1
        _settingsVM.setIsPlaying(true)
        for (i in events.indices) {
            if (i >= _settingsVM.nBack.value) {
                previousValue = events[i - _settingsVM.nBack.value]
            }
            _visualState.value = _visualState.value.copy(
                eventValue = events[i],
                previousValue = previousValue,
                guess = Guess.NONE,
                index = i,
                state = GameProgressionState.ONGOING,
            )
            delay(_settingsVM.eventInterval.value)
            previousValue = -1
        }
        _settingsVM.setIsPlaying(false)
        _visualState.value = _visualState.value.copy(state = GameProgressionState.OVER)
    }

    private suspend fun runAudioVisualGame(audioEvents: Array<Int>, visualEvents: Array<Int>) {
        resetGame()
        _settingsVM.setGameType(GameType.AudioVisual)
        var previousAudioValue: Int = -1
        var previousVisualValue: Int = -1
        _settingsVM.setIsPlaying(true)
        for (i in visualEvents.indices) {
            if (i >= _settingsVM.nBack.value) {
                previousVisualValue = visualEvents[i - _settingsVM.nBack.value]
                previousAudioValue = audioEvents[i - _settingsVM.nBack.value] % 26
            }
            val letter = intToLetter(audioEvents[i] % 26)
            _visualState.value = _visualState.value.copy(
                eventValue = visualEvents[i],
                previousValue = previousVisualValue,
                guess = Guess.NONE,
                index = i,
                state = GameProgressionState.ONGOING,
            )
            _audioState.value = _audioState.value.copy(
                eventValue = audioEvents[i] % 26,
                previousValue = previousAudioValue,
                guess = Guess.NONE,
                letter = letter,
                index = i,
                state = GameProgressionState.ONGOING,
            )
            _textToSpeech?.speak(letter, TextToSpeech.QUEUE_FLUSH, null, null)
            delay(_settingsVM.eventInterval.value)
            previousVisualValue = -1
        }
        _settingsVM.setIsPlaying(false)
        _visualState.value = _visualState.value.copy(state = GameProgressionState.OVER)
        _audioState.value = _audioState.value.copy(state = GameProgressionState.OVER)
    }

    private fun intToLetter(value: Int): String? {
        if (value < 1) {
            return null
        }
        val letter = 'A'.code + ((value-1)%26)
        return letter.toChar() + ""
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(
                    SettingsVM(userPreferencesRepository = application.userPreferencesRespository),
                    LeaderBoardVM(application.leaderBoardRepository),
                    application.userPreferencesRespository,
                    application,
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            userPreferencesRepository.highScore.collect {
                Log.d("LOAD", "HighScore: $it")
                _highscore.value = it
            }
        }
        viewModelScope.launch {
            _textToSpeech = TextToSpeech(_application) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    println("TextToSpeech Initialized Successfully")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            Log.d("SAVE", "HighScore: ${highscore.value}")
            userPreferencesRepository.saveHighScore(highscore.value)
        }
        _textToSpeech?.shutdown()

    }
}