package com.talhaoz.biriktir.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talhaoz.biriktir.domain.model.UserProfile

@Entity(tableName = "user_profile")
data class UserProfileStorageModel(
    @PrimaryKey val id: Int = 0,
    val fullName: String,
    val salaryDay: Int?,
    val photo: String? = null
)  {
    internal fun toDomain() =
        UserProfile(
            id = id,
            fullName = fullName,
            salaryDay = salaryDay,
            photo = photo
        )

    companion object {
        fun  fromDomain(
            userProfile: UserProfile
        ): UserProfileStorageModel =
            UserProfileStorageModel(
                fullName = userProfile.fullName,
                salaryDay = userProfile.salaryDay,
                photo = userProfile.photo
            )
    }
}

