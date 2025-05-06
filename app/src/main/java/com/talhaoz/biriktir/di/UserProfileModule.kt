package com.talhaoz.biriktir.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.talhaoz.biriktir.data.local.dao.UserProfileDao
import com.talhaoz.biriktir.data.local.database.BiriktirDatabase
import com.talhaoz.biriktir.data.local.datastore.NotificationSettingsDataStore
import com.talhaoz.biriktir.data.local.datastore.ThemePreferenceDataStore
import com.talhaoz.biriktir.data.repository.UserProfileRepositoryImpl
import com.talhaoz.biriktir.domain.repository.UserProfileRepository
import com.talhaoz.biriktir.domain.usecase.GetUserProfile
import com.talhaoz.biriktir.domain.usecase.UpdateUserProfile
import com.talhaoz.biriktir.domain.usecase.UserProfileUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserProfileModule {

    private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Provides
    @Singleton
    fun provideUserProfileDao(db: BiriktirDatabase): UserProfileDao =
        db.userProfileDao()

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        dao: UserProfileDao
    ): UserProfileRepository = UserProfileRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideUserProfileUseCases(
        repository: UserProfileRepository
    ): UserProfileUseCases = UserProfileUseCases(
        getProfile = GetUserProfile(repository),
        updateProfile = UpdateUserProfile(repository)
    )

    @Provides
    @Singleton
    fun provideNotificationSettingsDataStore(
        @ApplicationContext appContext: Context
    ): NotificationSettingsDataStore = NotificationSettingsDataStore(
        dataStore = appContext.settingsDataStore
    )

    @Provides
    @Singleton
    fun provideThemePreferenceDataStore(
        @ApplicationContext appContext: Context
    ): ThemePreferenceDataStore = ThemePreferenceDataStore(
        dataStore = appContext.settingsDataStore
    )
}
