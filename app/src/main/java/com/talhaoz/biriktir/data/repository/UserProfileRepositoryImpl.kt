package com.talhaoz.biriktir.data.repository

import com.talhaoz.biriktir.data.local.dao.UserProfileDao
import com.talhaoz.biriktir.data.local.entity.UserProfileStorageModel
import com.talhaoz.biriktir.domain.model.UserProfile
import com.talhaoz.biriktir.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val dao: UserProfileDao
) : UserProfileRepository {

    override suspend fun getProfile(): UserProfile? = dao.getProfile()?.toDomain()

    override suspend fun updateProfile(name: String, salaryDay: Int?) {
        dao.insertOrUpdate(UserProfileStorageModel.fromDomain(UserProfile(fullName = name, salaryDay = salaryDay)))
    }
}

