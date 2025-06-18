package com.talhaoz.biriktir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.talhaoz.biriktir.data.local.entity.UserProfileStorageModel

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 0")
    suspend fun getProfile(): UserProfileStorageModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileStorageModel)

    @Query("UPDATE user_profile SET fullName = :name, salaryDay = :day WHERE id = 0")
    suspend fun updateProfile(name: String, day: Int?)

    @Query("UPDATE user_profile SET photo = :uri WHERE id = 0")
    suspend fun updatePhoto(uri: String?)
}

