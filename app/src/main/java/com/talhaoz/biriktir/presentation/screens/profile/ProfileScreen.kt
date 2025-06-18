package com.talhaoz.biriktir.presentation.screens.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.isGranted
import com.talhaoz.biriktir.R
import com.talhaoz.biriktir.notification.scheduleSalaryReminderWorker
import com.talhaoz.biriktir.presentation.components.NotificationSettingsDialog
import com.talhaoz.biriktir.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


@Composable
fun ProfileScreen(
    onAvatarClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    onThemeSelected: (AppTheme) -> Unit
) {
    val state by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val notificationSettingsState by viewModel.notificationSettingsState.collectAsState(initial = false)
    val themePreferenceState by viewModel.themePreferenceState.collectAsState(initial = AppTheme.GreenDark)

    var showNotificationSettingsDialog by remember { mutableStateOf(false) }
    var showPhotoPicker by remember { mutableStateOf(false) }

    var showSettingsDialog by remember { mutableStateOf(false) }

    val savedName = state?.fullName ?: ""
    val savedSalaryDay: Int? = state?.salaryDay
    var fullName by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf(0) }
    var isModified by remember(fullName, selectedDay) {
        mutableStateOf(
            fullName != savedName || selectedDay != (savedSalaryDay ?: 0)
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out -> it.compress(Bitmap.CompressFormat.JPEG, 90, out) }
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", file
            )
            viewModel.updateProfilePhoto(uri)
        }
    }

    LaunchedEffect(savedName, savedSalaryDay) {
        fullName = savedName
        selectedDay = savedSalaryDay ?: 0
    }

    var showDayDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF6F6F6))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFEDEDED))
                .clickable(onClick = {
                    showPhotoPicker = true
                }),
            contentAlignment = Alignment.BottomEnd
        ) {
            if(state?.photo != null){
                AsyncImage(
                    model = state?.photo?.let(Uri::parse),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = (-8).dp, y = (-8).dp)
            .clip(CircleShape)
            .background(Color.White),
            contentAlignment = Alignment.Center
            ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Fotoğraf Seç",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "İsim Soyisim",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("İsim Soyisim") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Maaş Günü",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = if(selectedDay != 0) selectedDay.toString() else "",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    interactionSource = interactionSource
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showDayDialog = true
                                    }
                                }
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("Gün Seç") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFBDBDBD),
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        disabledBorderColor = Color(0xFFBDBDBD),
                        cursorColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )

                if(isModified){
                    Button(
                        onClick = {
                            saveSalaryDay(context, selectedDay)
                            viewModel.updateUserProfile(fullName = fullName, salaryDay = selectedDay )
                            Toast.makeText(context, "Profil başarıyla güncellendi", Toast.LENGTH_SHORT).show()
                            isModified = false
                            focusManager.clearFocus()
                        },
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .width(100.dp)
                            .height(40.dp)
                            .align(Alignment.End),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) { Text(text = "Kaydet", fontSize = 16.sp, fontWeight = FontWeight.Medium) }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Uygulama Ayarları",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            SettingRow(
                title = "Bildirim Ayarları",
                onClick = {
                    showNotificationSettingsDialog = true
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingRow(
                startingIconRes = R.drawable.ic_palette,
                title = "Tema Seçimi",
                onClick = { showThemeDialog = true }
            )
        }

        SalaryDatePickerDialog(
            show = showDayDialog,
            selectedDay = selectedDay,
            onConfirm = {
                selectedDay = it
                showDayDialog = false
            },
            onDismiss = {
                showDayDialog = false
            }
        )

        ThemeSelectionDialog(
            show = showThemeDialog,
            onDismiss = { showThemeDialog = false },
            selectedTheme = themePreferenceState,
            onThemeSelected = { onThemeSelected(it) }
        )

        NotificationSettingsDialog(
            show = showNotificationSettingsDialog,
            notificationEnabled = notificationSettingsState,
            onDismiss = { showNotificationSettingsDialog = false },
            onSave = {
                viewModel.updateNotificationSettings(it)
                showNotificationSettingsDialog = false
            }
        )

        /*if (showPhotoPicker) {
            ProfilePhotoPicker {

            }
        }*/
    }

    /*if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Kamera İzni Gerekli") },
            text = { Text("Fotoğraf çekebilmek için kamera izni gerekli. Ayarlardan izni açmak ister misiniz?") },
            confirmButton = {
                TextButton(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    showSettingsDialog = false
                }) {
                    Text("Ayarlar’a Git")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }*/
}

@Composable
private fun SettingRow(
    startingIconRes: Int? = null,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(startingIconRes == null){
            Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        } else {
            Icon(painter = painterResource(startingIconRes), modifier = Modifier.size(20.dp), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun SalaryDatePickerDialog(
    show: Boolean,
    selectedDay: Int?,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    if (!show) return

    var tempSelected by remember { mutableStateOf(selectedDay) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // ekran kenar boşluğu
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp), // iç padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Maaş Gününü Seç",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Takvim 1–31
                val days = (1..31).toList()
                val rows = days.chunked(7)

                Column {
                    rows.forEach { week ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            week.forEach { day ->
                                val isSelected = day == tempSelected
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary
                                            else Color.Transparent
                                        )
                                        .clickable { tempSelected = day }
                                ) {
                                    Text(
                                        text = day.toString(),
                                        color = if (isSelected) Color.White else Color.Black
                                    )
                                }
                            }

                            if (week.size < 7) {
                                repeat(7 - week.size) {
                                    Spacer(modifier = Modifier.size(36.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Alt Butonlar (eşit genişlikte)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("İptal", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            tempSelected?.let { onConfirm(it) }
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Tamam", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

fun saveSalaryDay(context: Context, day: Int) {
    val prefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    prefs.edit().putInt("salary_day", day).apply()

    scheduleSalaryReminderWorker(context)
}






