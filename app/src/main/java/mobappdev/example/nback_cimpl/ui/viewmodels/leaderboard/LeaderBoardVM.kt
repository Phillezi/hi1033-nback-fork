package mobappdev.example.nback_cimpl.ui.viewmodels.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.data.models.Score
import mobappdev.example.nback_cimpl.data.repositories.LeaderBoardRepository
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ILeaderBoardVM
import java.util.Date

class LeaderBoardVM(private val repository: LeaderBoardRepository) : ILeaderBoardVM, ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameApplication
                LeaderBoardVM(application.leaderBoardRepository)
            }
        }
    }

    override suspend fun saveScore(name: String, score: Int, date: Long?) {
        val finalDate = date?.let { Date(it) } ?: Date()
        val newScore = Score(playerName = name, score = score, date = finalDate)
        repository.saveScore(newScore)
    }

    override suspend fun getScoresSortedByName(): List<Score> {
        return repository.getScoresSortedByName()
    }

    override suspend fun getScoresSortedByScore(): List<Score> {
        return repository.getScoresSortedByScore()
    }

    override suspend fun getScoresSortedByDate(): List<Score> {
        return repository.getScoresSortedByDate()
    }

    override suspend fun getHighestScoreByPlayer(player: String): Score? {
        return repository.getHighestScoreByName(name = player)
    }
}
