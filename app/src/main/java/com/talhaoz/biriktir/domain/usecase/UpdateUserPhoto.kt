package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateUserPhoto @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(uri: String?) {
        repository.updatePhoto(uri)
    }
}
