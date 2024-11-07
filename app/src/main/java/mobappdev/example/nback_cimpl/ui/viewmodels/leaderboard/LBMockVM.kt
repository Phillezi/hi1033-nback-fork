package mobappdev.example.nback_cimpl.ui.viewmodels.leaderboard

import mobappdev.example.nback_cimpl.data.models.Score
import mobappdev.example.nback_cimpl.ui.viewmodels.interfaces.ILeaderBoardVM

class LBMockVM: ILeaderBoardVM {
    override suspend fun saveScore(name: String, score: Int, date: Long?) {
        TODO("Not yet implemented")
    }

    override suspend fun getScoresSortedByName(): List<Score> {
        TODO("Not yet implemented")
    }

    override suspend fun getScoresSortedByScore(): List<Score> {
        TODO("Not yet implemented")
    }

    override suspend fun getScoresSortedByDate(): List<Score> {
        TODO("Not yet implemented")
    }

    override suspend fun getHighestScoreByPlayer(player: String): Score? {
        TODO("Not yet implemented")
    }
}