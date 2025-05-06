package com.talhaoz.biriktir.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.talhaoz.biriktir.MainActivity
import com.talhaoz.biriktir.R
import com.talhaoz.biriktir.data.local.datastore.NotificationSettingsDataStore
import com.talhaoz.biriktir.domain.model.UserProfile
import com.talhaoz.biriktir.domain.usecase.UserProfileUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@HiltWorker
class SalaryReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userProfileUseCases: UserProfileUseCases,
    private val notificationSettingsDataStore: NotificationSettingsDataStore
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val notificationsEnabled = notificationSettingsDataStore
            .notificationSettingFlow
            .first()

        if (!notificationsEnabled) return Result.success()

        val profile: UserProfile? = userProfileUseCases.getProfile().firstOrNull()
        val salaryDay = profile?.salaryDay ?: return Result.success()

        val today = LocalDate.now().dayOfMonth
        if (today == salaryDay) {

        }

        sendNotification()

        return Result.success()
    }

    private fun sendNotification() {
        val channelId = "salary_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Maaş Günü Bildirimi",
            NotificationManager.IMPORTANCE_DEFAULT
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
            .setContentText("Bugün maaş gününüz! Birikim eklemeyi unutmayın 💸")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1001, notification)
    }
}

fun scheduleSalaryReminderWorker(context: Context) {
    val delayMinutes = calculateInitialDelayFor9AM()

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

private fun calculateInitialDelayFor9AM(): Long {
    val now = LocalDateTime.now()
    val today9AM = now.toLocalDate().atTime(11, 35)

    val nextRun = if (now.isBefore(today9AM)) today9AM else today9AM.plusDays(1)
    val duration = Duration.between(now, nextRun)

    return duration.toMinutes()
}

