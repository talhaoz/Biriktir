package com.talhaoz.biriktir.di

import com.talhaoz.biriktir.data.local.dao.SavingEntryDao
import com.talhaoz.biriktir.data.local.dao.SavingGoalDao
import com.talhaoz.biriktir.data.repository.SavingRepositoryImpl
import com.talhaoz.biriktir.domain.repository.SavingRepository
import com.talhaoz.biriktir.domain.usecase.DeleteEntry
import com.talhaoz.biriktir.domain.usecase.DeleteGoal
import com.talhaoz.biriktir.domain.usecase.GetAllGoals
import com.talhaoz.biriktir.domain.usecase.GetGoalWithEntries
import com.talhaoz.biriktir.domain.usecase.InsertEntry
import com.talhaoz.biriktir.domain.usecase.InsertGoal
import com.talhaoz.biriktir.domain.usecase.SavingUseCases
import com.talhaoz.biriktir.domain.usecase.UpdateSavedAmount
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Repository
    @Provides
    @Singleton
    fun provideSavingRepository(
        goalDao: SavingGoalDao,
        entryDao: SavingEntryDao
    ): SavingRepository {
        return SavingRepositoryImpl(goalDao, entryDao)
    }

    @Provides
    @Singleton
    fun provideSavingUseCases(repository: SavingRepository): SavingUseCases {
        return SavingUseCases(
            getAllGoals = GetAllGoals(repository),
            insertGoal = InsertGoal(repository),
            deleteGoal = DeleteGoal(repository),
            insertEntry = InsertEntry(repository),
            getGoalWithEntries = GetGoalWithEntries(repository),
            updateSavedAmount = UpdateSavedAmount(repository),
            deleteEntry = DeleteEntry(repository)
        )
    }
}
