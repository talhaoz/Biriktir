package com.talhaoz.biriktir.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talhaoz.biriktir.R

@Composable
fun HeaderTitle(
    title: String,
    modifier: Modifier = Modifier,
    isMenuVisible: Boolean = false,
    onBackButtonClicked: () -> Unit,
    editModeSwitched: ((Boolean) -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onBackButtonClicked) {
            Icon(
                modifier = Modifier.size(22.dp),
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))

        if(isMenuVisible){
            MenuView(
                editModeSwitched = { editModeSwitched?.invoke(it) },
                onDeleteClick = { onDeleteClick?.invoke() }
            )
        }

    }
}

@Composable
fun MenuView(
    editModeSwitched: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var _isInEditMode by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(onClick = {
            if(!_isInEditMode)
                expanded = true
            else {
                _isInEditMode = false
                editModeSwitched(false)
            }
        }) {
            Icon(
                imageVector = if(!_isInEditMode) Icons.Default.MoreVert else Icons.Default.Close,
                contentDescription = "Menü"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Düzenle") },
                onClick = {
                    expanded = false
                    _isInEditMode = true
                    editModeSwitched(true)
                }
            )
            DropdownMenuItem(
                text = { Text("Sil") },
                onClick = {
                    expanded = false
                    onDeleteClick()
                }
            )
        }
    }

}
