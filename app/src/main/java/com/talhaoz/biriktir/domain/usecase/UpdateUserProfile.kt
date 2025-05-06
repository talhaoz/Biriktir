package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.repository.UserProfileRepository
import javax.inject.Inject


class UpdateUserProfile @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(name: String, day: Int?) {
        repository.updateProfile(name, day)
    }
}
