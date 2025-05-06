package com.talhaoz.biriktir.di

import android.content.Context
import androidx.room.Room
import com.talhaoz.biriktir.data.local.dao.SavingEntryDao
import com.talhaoz.biriktir.data.local.dao.SavingGoalDao
import com.talhaoz.biriktir.data.local.database.BiriktirDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BiriktirDatabase {
        return Room.databaseBuilder(
            context,
            BiriktirDatabase::class.java,
            "biriktir_db"
        )
        .build()
    }

    @Provides
    fun provideGoalDao(db: BiriktirDatabase): SavingGoalDao = db.savingGoalDao()

    @Provides
    fun provideEntryDao(db: BiriktirDatabase): SavingEntryDao = db.savingEntryDao()
}

