package mobappdev.example.nback_cimpl.ui.viewmodels.interfaces

import mobappdev.example.nback_cimpl.data.models.Score

interface ILeaderBoardVM {
    suspend fun saveScore(name: String, score: Int, date: Long? = null)

    suspend fun getScoresSortedByName(): List<Score>
    suspend fun getScoresSortedByScore(): List<Score>
    suspend fun getScoresSortedByDate(): List<Score>
    suspend fun getHighestScoreByPlayer(player: String): Score?
}