package com.talhaoz.biriktir.presentation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.talhaoz.biriktir.MainActivity
import com.talhaoz.biriktir.R
import com.talhaoz.biriktir.notification.SalaryReminderWorker
import com.talhaoz.biriktir.notification.scheduleSalaryReminderWorker
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@Composable
fun DebugNotificationTestScreen(context: Context = LocalContext.current) {
    val scope = rememberCoroutineScope()
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Debug Tools", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Button(onClick = {
            // 1️⃣ Anında test bildirimi gönder
            val channelId = "salary_channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Salary Reminder",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Test Notification")
                .setContentText("This is a test notification from debug panel.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(999, notification)
        }) {
            Text("Send Test Notification")
        }

        Button(onClick = {
            // 2️⃣ Tek seferlik worker ile test bildirimi
            /*val request = OneTimeWorkRequestBuilder<SalaryReminderWorker>()
                .addTag("SalaryReminder")
                .build()
            WorkManager.getInstance(context).enqueue(request)*/
            scheduleSalaryReminderWorker(context)
        }) {
            Text("Trigger SalaryReminderWorker")
        }

        Button(onClick = {
            // 3️⃣ Planlanan işleri görüntüle
            scope.launch {
                val works = WorkManager.getInstance(context)
                    .getWorkInfosByTag("SalaryReminder")
                    .await()
                works.forEach {
                    Log.d("WORK_STATUS", "Worker ID: ${it.id}, State: ${it.state}")
                }
            }
        }) {
            Text("Check WorkManager Status")
        }

        Button(onClick = {
            // 4️⃣ Tüm SalaryReminder işlerini iptal et
            WorkManager.getInstance(context).cancelAllWorkByTag("SalaryReminder")
        }) {
            Text("Cancel Scheduled Worker")
        }
    }
}
