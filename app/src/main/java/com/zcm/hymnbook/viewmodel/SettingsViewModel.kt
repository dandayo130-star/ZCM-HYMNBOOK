package com.zcm.hymnbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zcm.hymnbook.data.preferences.AppTheme
import com.zcm.hymnbook.data.preferences.TextSize
import com.zcm.hymnbook.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val theme: StateFlow<AppTheme> = preferencesRepository.theme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.SYSTEM)

    val textSize: StateFlow<TextSize> = preferencesRepository.textSize
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TextSize.MEDIUM)

    val readerFontScalePercent: StateFlow<Int> = preferencesRepository.readerFontScalePercent
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 100)

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch { preferencesRepository.setTheme(theme) }
    }

    fun setTextSize(size: TextSize) {
        viewModelScope.launch { preferencesRepository.setTextSize(size) }
    }

    fun increaseReaderFontScale() {
        viewModelScope.launch {
            preferencesRepository.setReaderFontScalePercent(readerFontScalePercent.value + 10)
        }
    }

    fun decreaseReaderFontScale() {
        viewModelScope.launch {
            preferencesRepository.setReaderFontScalePercent(readerFontScalePercent.value - 10)
        }
    }

    class Factory(
        private val preferencesRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(preferencesRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
