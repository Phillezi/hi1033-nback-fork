package mobappdev.example.nback_cimpl.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.concurrent.ArrayBlockingQueue

class DBManager private constructor(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val connectionPool: ArrayBlockingQueue<SQLiteDatabase> = ArrayBlockingQueue(POOL_SIZE)

    init {
        for (i in 1..POOL_SIZE) {
            connectionPool.offer(dbHelper.writableDatabase)
        }
    }

    companion object {
        private const val POOL_SIZE = 5
        @Volatile
        private var INSTANCE: DBManager? = null

        fun getInstance(context: Context): DBManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DBManager(context).also { INSTANCE = it }
            }
    }

    @Synchronized
    fun getDatabase(): SQLiteDatabase? {
        return connectionPool.poll() ?: dbHelper.writableDatabase
    }

    @Synchronized
    fun releaseDatabase(database: SQLiteDatabase) {
        if (!connectionPool.offer(database)) {
            database.close()
        }
    }

    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS $TABLE_LEADERBOARD (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_PLAYER_NAME TEXT NOT NULL,
                    $COLUMN_SCORE INTEGER NOT NULL,
                    $COLUMN_DATE INTEGER NOT NULL
                )
                """
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_LEADERBOARD")
            onCreate(db)
        }

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "leaderboard.db"

            const val TABLE_LEADERBOARD = "leaderboard"
            const val COLUMN_ID = "id"
            const val COLUMN_PLAYER_NAME = "player_name"
            const val COLUMN_SCORE = "score"
            const val COLUMN_DATE = "date"
        }
    }
}
