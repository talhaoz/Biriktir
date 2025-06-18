package com.talhaoz.biriktir.domain.repository

import com.talhaoz.biriktir.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getProfile(): UserProfile?
    suspend fun updateProfile(name: String, salaryDay: Int?)
    suspend fun updatePhoto(uri: String?)
}

