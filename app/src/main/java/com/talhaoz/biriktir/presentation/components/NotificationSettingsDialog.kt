package com.talhaoz.biriktir.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NotificationSettingsDialog(
    show: Boolean,
    notificationEnabled: Boolean,
    onDismiss: () -> Unit,
    onSave: (Boolean) -> Unit
) {
    if (!show) return

    var notificationSettingsState by remember { mutableStateOf(notificationEnabled) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bildirim Ayarları", fontWeight = FontWeight.Bold) },
        text = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Maaş Bildirimi", modifier = Modifier.weight(1f))
                Switch(
                    checked = notificationSettingsState,
                    onCheckedChange = {
                        notificationSettingsState = it
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(notificationSettingsState) }) {
                Text("Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
