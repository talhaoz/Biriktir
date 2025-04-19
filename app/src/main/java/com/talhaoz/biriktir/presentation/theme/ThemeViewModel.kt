package com.talhaoz.biriktir.presentation.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhaoz.biriktir.data.local.ThemePreferenceDataStore
import com.talhaoz.biriktir.ui.theme.AppTheme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val themePreference: ThemePreferenceDataStore
) : ViewModel() {
    val themeState: StateFlow<AppTheme> = themePreference.themeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AppTheme.Green
    )

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            themePreference.saveTheme(theme)
        }
    }
}

