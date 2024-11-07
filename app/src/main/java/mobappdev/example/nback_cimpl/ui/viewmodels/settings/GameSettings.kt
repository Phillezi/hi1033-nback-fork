package mobappdev.example.nback_cimpl.ui.viewmodels.settings

import kotlinx.coroutines.flow.MutableStateFlow
import mobappdev.example.nback_cimpl.ui.viewmodels.game.GameType

data class GameSettings(
    var gameType: MutableStateFlow<GameType> = MutableStateFlow(GameType.Visual),
    var isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false),
    var nBack: MutableStateFlow<Int> = MutableStateFlow(3),
    var sideLength: MutableStateFlow<Int> = MutableStateFlow(3),
    var nrOfTurns: MutableStateFlow<Int> = MutableStateFlow(10),
    var percentMatches: MutableStateFlow<Int> = MutableStateFlow(30),
    var eventInterval: MutableStateFlow<Long> = MutableStateFlow(2000L)
) {
    override fun toString(): String {
        return "GameSettings(" +
                "\n\tgameType=${gameType.value}, " +
                "\n\tisPlaying=${isPlaying.value}, " +
                "\n\tnBack=${nBack.value}, " +
                "\n\tsideLength=${sideLength.value}, " +
                "\n\tnrOfTurns=${nrOfTurns.value}, " +
                "\n\tpercentMatches=${percentMatches.value}, " +
                "\n\teventInterval=${eventInterval.value}" +
                "\n)"
    }
}
