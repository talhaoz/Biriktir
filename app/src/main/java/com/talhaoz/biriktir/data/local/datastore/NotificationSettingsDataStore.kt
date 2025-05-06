package com.talhaoz.biriktir.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationSettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val NOTIFICATION_SETTINGS_KEY = booleanPreferencesKey("is_notification_enabled")

    val notificationSettingFlow: Flow<Boolean> = dataStore.data.map { prefs ->
        val notificationSettings = prefs[NOTIFICATION_SETTINGS_KEY] ?: false
        notificationSettings
    }

    suspend fun saveNotificationSetting(isEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[NOTIFICATION_SETTINGS_KEY] = isEnabled
        }
    }
}
