package com.talhaoz.biriktir.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SalaryDaySettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val notificationSettingFlow: Flow<Boolean> = dataStore.data.map { prefs ->
        val notificationSettings = prefs[NOTIFICATION_SETTINGS_KEY] ?: true
        notificationSettings
    }
    val salaryDayFlow: Flow<Int> = dataStore.data.map { prefs ->
        val salaryDay = prefs[SALARY_DAY_KEY] ?: 0
        salaryDay
    }

    suspend fun saveNotificationSetting(isEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[NOTIFICATION_SETTINGS_KEY] = isEnabled
        }
    }

    suspend fun saveSalaryDay(day: Int?) {
        dataStore.edit { prefs ->
            prefs[SALARY_DAY_KEY] = day ?: 0
        }
    }

    companion object {
        val NOTIFICATION_SETTINGS_KEY = booleanPreferencesKey("is_notification_enabled")
        val SALARY_DAY_KEY = intPreferencesKey("salary_day")
    }
}
