package com.talhaoz.biriktir.presentation.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhaoz.biriktir.data.local.datastore.ThemePreferenceDataStore
import com.talhaoz.biriktir.ui.theme.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
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

