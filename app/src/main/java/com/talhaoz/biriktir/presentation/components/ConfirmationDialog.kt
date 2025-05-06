package com.talhaoz.biriktir.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancelClicked() },
        title = {
            Text(text = title, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(description)
        },
        confirmButton = {
            TextButton(onClick = { onConfirmClicked() }) {
                Text("Evet", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancelClicked() }) {
                Text("İptal")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
