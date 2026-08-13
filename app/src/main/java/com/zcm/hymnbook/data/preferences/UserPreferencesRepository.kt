package com.zcm.hymnbook.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "zcm_hymn_book_settings")

enum class AppTheme { SYSTEM, LIGHT, DARK }

enum class TextSize(val scale: Float, val label: String) {
    SMALL(0.85f, "Small"),
    MEDIUM(1.0f, "Medium"),
    LARGE(1.2f, "Large"),
    EXTRA_LARGE(1.45f, "Extra Large")
}

/**
 * Wraps Jetpack DataStore to persist user preferences: theme mode and
 * hymn-reader text size. Both survive app restarts, satisfying the
 * "remember the selected text size" requirement.
 */
class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val THEME = stringPreferencesKey("app_theme")
        val TEXT_SIZE = stringPreferencesKey("text_size")
        val READER_FONT_SCALE = intPreferencesKey("reader_font_scale_percent")
    }

    val theme: Flow<AppTheme> = context.dataStore.data.map { prefs ->
        prefs[Keys.THEME]?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() } ?: AppTheme.SYSTEM
    }

    val textSize: Flow<TextSize> = context.dataStore.data.map { prefs ->
        prefs[Keys.TEXT_SIZE]?.let { runCatching { TextSize.valueOf(it) }.getOrNull() } ?: TextSize.MEDIUM
    }

    /**
     * Fine-grained reader zoom (A- / A+ buttons), stored as a percentage
     * (e.g. 100 = 100%) on top of the base TextSize setting, so the
     * hymn reader can be nudged independently of the global Settings
     * text-size choice.
     */
    val readerFontScalePercent: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.READER_FONT_SCALE] ?: 100
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { it[Keys.THEME] = theme.name }
    }

    suspend fun setTextSize(size: TextSize) {
        context.dataStore.edit { it[Keys.TEXT_SIZE] = size.name }
    }

    suspend fun setReaderFontScalePercent(percent: Int) {
        val clamped = percent.coerceIn(70, 200)
        context.dataStore.edit { it[Keys.READER_FONT_SCALE] = clamped }
    }
}
