package com.talhaoz.biriktir.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.talhaoz.biriktir.MainActivity
import com.talhaoz.biriktir.R
import com.talhaoz.biriktir.data.local.datastore.SalaryDaySettingsDataStore.Companion.NOTIFICATION_SETTINGS_KEY
import com.talhaoz.biriktir.data.local.datastore.SalaryDaySettingsDataStore.Companion.SALARY_DAY_KEY
import com.talhaoz.biriktir.di.UserProfileModule.settingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class SalaryReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val context = applicationContext

        val dataStore = context.settingsDataStore

        val notificationEnabled = dataStore.data
            .map { prefs -> prefs[NOTIFICATION_SETTINGS_KEY] ?: false }
            .first()

        val salaryDay = dataStore.data
            .map { prefs -> prefs[SALARY_DAY_KEY] ?: 0 }
            .first()

        if (!notificationEnabled) return Result.success()
        if (salaryDay == 0) return Result.success()

        val today = LocalDate.now().dayOfMonth
        if (today == salaryDay) {
            sendNotification()
        }

        return Result.success()
    }

    private fun sendNotification() {
        val channelId = "salary_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Salary Reminder",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Birikim Hatırlatması")
            .setContentText("Bugün maaş gününüz! Birikim yapmayı unutmayın 💸")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}


fun scheduleSalaryReminderWorker(context: Context) {
    val delayMinutes = calculateInitialDelayFor9PM()

    val request = PeriodicWorkRequestBuilder<SalaryReminderWorker>(
        1, TimeUnit.DAYS
    )
        .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
        .addTag("SalaryReminder")
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "MonthlySalaryReminder",
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )
}

private fun calculateInitialDelayFor9PM(): Long {
    val now = LocalDateTime.now()
    val today9PM = now.toLocalDate().atTime(21, 0)

    val nextRun = if (now.isBefore(today9PM)) today9PM else today9PM.plusDays(1)
    val duration = Duration.between(now, nextRun)

    return duration.toMinutes()
}

