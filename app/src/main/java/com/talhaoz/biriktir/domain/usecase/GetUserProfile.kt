package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.model.UserProfile
import com.talhaoz.biriktir.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserProfile @Inject constructor(
    private val repository: UserProfileRepository
) {
    operator fun invoke(): Flow<UserProfile?> =
        flow {
           emit(repository.getProfile())
        }
}
