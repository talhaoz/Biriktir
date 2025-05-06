package com.talhaoz.biriktir.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhaoz.biriktir.data.local.datastore.NotificationSettingsDataStore
import com.talhaoz.biriktir.data.local.datastore.ThemePreferenceDataStore
import com.talhaoz.biriktir.domain.model.UserProfile
import com.talhaoz.biriktir.domain.usecase.UserProfileUseCases
import com.talhaoz.biriktir.ui.theme.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases,
    private val notificationSettingsDataStore: NotificationSettingsDataStore,
    private val themePreferenceDataStore: ThemePreferenceDataStore
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    val notificationSettingsState: Flow<Boolean> = notificationSettingsDataStore.notificationSettingFlow
    val themePreferenceState: Flow<AppTheme> = themePreferenceDataStore.themeFlow

    init{
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            userProfileUseCases.getProfile()
                .collect { userProfile ->
                    if(userProfile != null){
                        _userProfile.value = userProfile
                    }
                }
        }
    }

    fun updateUserProfile(fullName: String, salaryDay: Int?) {
        viewModelScope.launch {
            userProfileUseCases.updateProfile(fullName, salaryDay)
            getUserProfile()
        }
    }

    fun updateNotificationSettings(isEnabled: Boolean) {
        viewModelScope.launch {
            notificationSettingsDataStore.saveNotificationSetting(isEnabled)
        }
    }
}

