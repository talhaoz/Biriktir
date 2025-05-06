package com.talhaoz.biriktir.presentation.screens.profile

import com.talhaoz.biriktir.domain.model.UserProfile

sealed interface UserProfileState {
    data object Loading : UserProfileState

    data class Error(val message: String) : UserProfileState

    data class Ready(val savingGoals: UserProfile) : UserProfileState
}