package com.talhaoz.biriktir.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.talhaoz.biriktir.data.local.converters.CurrencyConverter
import com.talhaoz.biriktir.data.local.dao.SavingEntryDao
import com.talhaoz.biriktir.data.local.dao.SavingGoalDao
import com.talhaoz.biriktir.data.local.dao.UserProfileDao
import com.talhaoz.biriktir.data.local.entity.SavingEntryStorageModel
import com.talhaoz.biriktir.data.local.entity.SavingGoalStorageModel
import com.talhaoz.biriktir.data.local.entity.UserProfileStorageModel


@Database(
    entities = [
    SavingGoalStorageModel::class,
    SavingEntryStorageModel::class,
    UserProfileStorageModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CurrencyConverter::class)
abstract class BiriktirDatabase : RoomDatabase() {
    abstract fun savingGoalDao(): SavingGoalDao
    abstract fun savingEntryDao(): SavingEntryDao
    abstract fun userProfileDao(): UserProfileDao
}

