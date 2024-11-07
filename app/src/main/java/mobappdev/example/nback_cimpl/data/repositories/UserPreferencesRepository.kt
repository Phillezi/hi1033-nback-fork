package mobappdev.example.nback_cimpl.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * This repository provides a way to interact with the DataStore api,
 * with this API you can save key:value pairs
 *
 * Currently this file contains only one thing: getting the highscore as a flow
 * and writing to the highscore preference.
 * (a flow is like a waterpipe; if you put something different in the start,
 * the end automatically updates as long as the pipe is open)
 *
 * Date: 25-08-2023
 * Version: Skeleton code version 1.0
 * Author: Yeetivity
 *
 */
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val HIGHSCORE = intPreferencesKey("highscore")
        val SIDELENGTH = intPreferencesKey("sideLength")
        val N = intPreferencesKey("n")
        val TURNS = intPreferencesKey("turns")
        val PERCENT = intPreferencesKey("percent")
        val TIME = longPreferencesKey("time")
        const val TAG = "UserPreferencesRepo"
    }

    private fun <T> Flow<Preferences>.getPreference(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "Error reading preferences", exception)
                    emit(emptyPreferences())
                } else throw exception
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    val highScore: Flow<Int> = dataStore.data.getPreference(HIGHSCORE, 0)
    val sideLength: Flow<Int> = dataStore.data.getPreference(SIDELENGTH, 3)
    val n: Flow<Int> = dataStore.data.getPreference(N, 2)
    val turns: Flow<Int> = dataStore.data.getPreference(TURNS, 10)
    val percent: Flow<Int> = dataStore.data.getPreference(PERCENT, 30)
    val time: Flow<Long> = dataStore.data.getPreference(TIME, 2000L)

    private suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun saveHighScore(score: Int) = savePreference(HIGHSCORE, score)
    suspend fun saveSideLength(sideLength: Int) = savePreference(SIDELENGTH, sideLength)
    suspend fun saveN(n: Int) = savePreference(N, n)
    suspend fun saveTurns(turns: Int) = savePreference(TURNS, turns)
    suspend fun savePercent(percent: Int) = savePreference(PERCENT, percent)
    suspend fun saveTime(time: Long) = savePreference(TIME, time)
}