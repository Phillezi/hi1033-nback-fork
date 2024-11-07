package mobappdev.example.nback_cimpl.data.repositories

import android.content.ContentValues
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobappdev.example.nback_cimpl.data.DBManager
import mobappdev.example.nback_cimpl.data.models.Score
import java.util.Date

class LeaderBoardRepository(context: Context) {

    private val dbManager = DBManager.getInstance(context)

    suspend fun saveScore(score: Score) = withContext(Dispatchers.IO) {
        val db = dbManager.getDatabase()
        db?.let {
            try {
                val values = ContentValues().apply {
                    put(DBManager.DatabaseHelper.COLUMN_PLAYER_NAME, score.playerName)
                    put(DBManager.DatabaseHelper.COLUMN_SCORE, score.score)
                    put(DBManager.DatabaseHelper.COLUMN_DATE, score.date.time)
                }
                it.insert(DBManager.DatabaseHelper.TABLE_LEADERBOARD, null, values)
            } finally {
                dbManager.releaseDatabase(it)
            }
        }
    }

    suspend fun getScoresSortedBy(orderBy: String): List<Score> = withContext(Dispatchers.IO) {
        val db = dbManager.getDatabase()
        val scores = mutableListOf<Score>()
        db?.let {
            try {
                val cursor = it.query(
                    DBManager.DatabaseHelper.TABLE_LEADERBOARD,
                    null, null, null, null, null,
                    orderBy
                )
                with(cursor) {
                    while (moveToNext()) {
                        val id = getInt(getColumnIndexOrThrow(DBManager.DatabaseHelper.COLUMN_ID))
                        val playerName =
                            getString(getColumnIndexOrThrow(DBManager.DatabaseHelper.COLUMN_PLAYER_NAME))
                        val score =
                            getInt(getColumnIndexOrThrow(DBManager.DatabaseHelper.COLUMN_SCORE))
                        val dateMillis =
                            getLong(getColumnIndexOrThrow(DBManager.DatabaseHelper.COLUMN_DATE))
                        scores.add(Score(id, playerName, score, Date(dateMillis)))
                    }
                    close()
                }
            } finally {
                dbManager.releaseDatabase(it)
            }
        }
        scores
    }

    suspend fun getScoresSortedByName(): List<Score> = getScoresSortedBy("player_name")
    suspend fun getScoresSortedByScore(): List<Score> = getScoresSortedBy("score DESC")
    suspend fun getScoresSortedByDate(): List<Score> = getScoresSortedBy("date DESC")

    suspend fun getHighestScoreByName(name: String): Score? {
        val db = dbManager.getDatabase()

        db?.let { database ->
            try {
                database.query(
                    DBManager.DatabaseHelper.TABLE_LEADERBOARD,
                    null,
                    "playerName = ?",
                    arrayOf(name),
                    null,
                    null,
                    "score DESC",
                    "1"
                ).use { cursor ->

                    if (cursor != null && cursor.moveToFirst()) {
                        val playerNameIndex = cursor.getColumnIndex("playerName")
                        val scoreIndex = cursor.getColumnIndex("score")
                        val dateIndex = cursor.getColumnIndex("date")

                        if (playerNameIndex < 0 || scoreIndex < 0 || dateIndex < 0) {
                            return null
                        }

                        val playerName = cursor.getString(playerNameIndex)
                        val score = cursor.getInt(scoreIndex)
                        val date = Date(cursor.getLong(dateIndex))

                        return Score(playerName = playerName, score = score, date = date)
                    }

                }
            } finally {
                dbManager.releaseDatabase(database)
            }
        }

        return null
    }
}
