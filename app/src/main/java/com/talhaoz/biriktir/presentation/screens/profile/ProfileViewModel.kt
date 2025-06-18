package com.talhaoz.biriktir.presentation.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhaoz.biriktir.data.local.datastore.SalaryDaySettingsDataStore
import com.talhaoz.biriktir.data.local.datastore.ThemePreferenceDataStore
import com.talhaoz.biriktir.domain.model.UserProfile
import com.talhaoz.biriktir.domain.usecase.UserProfileUseCases
import com.talhaoz.biriktir.ui.theme.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases,
    private val salaryDaySettingsDataStore: SalaryDaySettingsDataStore,
    private val themePreferenceDataStore: ThemePreferenceDataStore
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    val notificationSettingsState: Flow<Boolean> = salaryDaySettingsDataStore.notificationSettingFlow
    val themePreferenceState: Flow<AppTheme> = themePreferenceDataStore.themeFlow

/*    private val _checkCameraPermission = MutableFlow<Unit>()
    val checkCameraPermission: Flow<Unit> = _checkCameraPermission

    private val _cameraPermissionResult = MutableSharedFlow<CameraPermissionState>()
    val cameraPermissionResult: SharedFlow<CameraPermissionState> = _cameraPermissionResult*/

    init{
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            userProfileUseCases.getProfile()
                .collect { userProfile ->
                    userProfile?.let {
                        _userProfile.value = it
                    }
                }
        }
    }

    fun updateUserProfile(fullName: String, salaryDay: Int?) {
        viewModelScope.launch {
            userProfileUseCases.updateProfile(fullName, salaryDay)
            salaryDaySettingsDataStore.saveSalaryDay(salaryDay)
            getUserProfile()
        }
    }

    fun updateNotificationSettings(isEnabled: Boolean) {
        viewModelScope.launch {
            salaryDaySettingsDataStore.saveNotificationSetting(isEnabled)
        }
    }

    fun updateProfilePhoto(uri: Uri?) {
        viewModelScope.launch {
            uri?.toString().let {
                userProfileUseCases.updateUserPhoto(it)
                _userProfile.value = _userProfile.value?.copy(photo = it)
            }
        }
    }

    fun setCameraPermissionResult(cameraPermissionState: CameraPermissionState) {
        viewModelScope.launch {
            println(cameraPermissionState)
        }
    }

    /*   fun takePhotoClicked() {
           viewModelScope.launch {
               _checkCameraPermission.emit(Unit)
           }
       }*/
}

enum class CameraPermissionState{
    GRANTED,
    SHOW_RATIONALE,
    DENIED
}

