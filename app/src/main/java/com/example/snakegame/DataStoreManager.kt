package com.example.snakegame

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "game_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val HIGH_SCORE = intPreferencesKey("high_score")
        val SOUND = booleanPreferencesKey("sound")
        val VIBRATION = booleanPreferencesKey("vibration")
        val SHOW_GRID = booleanPreferencesKey("show_grid")
        val SNAKE_COLOR = stringPreferencesKey("snake_color")
    }

    // High Score
    val highScore: Flow<Int> = context.dataStore.data.map {
        it[HIGH_SCORE] ?: 0
    }

    suspend fun saveHighScore(score: Int) {
        context.dataStore.edit {
            it[HIGH_SCORE] = score
        }
    }

    // Sound
    val sound: Flow<Boolean> = context.dataStore.data.map {
        it[SOUND] ?: true
    }

    suspend fun saveSound(enabled: Boolean) {
        context.dataStore.edit {
            it[SOUND] = enabled
        }
    }

    // Vibration
    val vibration: Flow<Boolean> = context.dataStore.data.map {
        it[VIBRATION] ?: true
    }

    suspend fun saveVibration(enabled: Boolean) {
        context.dataStore.edit {
            it[VIBRATION] = enabled
        }
    }

    // Show Grid
    val showGrid: Flow<Boolean> = context.dataStore.data.map {
        it[SHOW_GRID] ?: true
    }

    suspend fun saveShowGrid(enabled: Boolean) {
        context.dataStore.edit {
            it[SHOW_GRID] = enabled
        }
    }

    // Snake Color
    val snakeColor: Flow<String> = context.dataStore.data.map {
        it[SNAKE_COLOR] ?: "Green"
    }

    suspend fun saveSnakeColor(color: String) {
        context.dataStore.edit {
            it[SNAKE_COLOR] = color
        }
    }
}