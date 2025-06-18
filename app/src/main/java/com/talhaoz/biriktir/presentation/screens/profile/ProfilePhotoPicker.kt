package com.talhaoz.biriktir.presentation.screens.profile

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfilePhotoPicker(
    onPhotoPicked: (Uri) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 1️⃣ ActivityResult launchers
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let(onPhotoPicked) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out -> it.compress(Bitmap.CompressFormat.JPEG, 90, out) }
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", file
            )
            onPhotoPicked(uri)
        }
    }

    // 2️⃣ Kamera izni
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // 3️⃣ Ayarlar dialogu
    var showSettingsDialog by remember { mutableStateOf(false) }

    // 4️⃣ Bottom sheet state
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.PartiallyExpanded
        }
    )

    // 5️⃣ BottomSheetLayout
    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        sheetState = sheetState
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Fotoğraf Seç", style = MaterialTheme.typography.titleMedium)
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            // Kamera
            ListItem(
                headlineContent = { Text("Fotoğraf Çek") },
                leadingContent = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
                modifier = Modifier.clickable {
                    scope.launch { sheetState.hide() }
                    // İzin kontrolü
                    when {
                        cameraPermissionState.status.isGranted ->
                            cameraLauncher.launch()

                        cameraPermissionState.status.shouldShowRationale ->
                            cameraPermissionState.launchPermissionRequest()

                        else ->
                            showSettingsDialog = true
                    }
                }
            )
            // Galeri
            ListItem(
                headlineContent = { Text("Galeriden Seç") },
                leadingContent = { Icon(Icons.Default.PhotoLibrary, contentDescription = null) },
                modifier = Modifier.clickable {
                    scope.launch { sheetState.hide() }
                    galleryLauncher.launch("image/*")
                }
            )
        }
    }

    // 7️⃣ Ayarlara yönlendirme dialogu
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Kamera İzni Gerekli") },
            text = { Text("Fotoğraf çekebilmek için kamera izni gerekli. Ayarlardan izni açmak ister misiniz?") },
            confirmButton = {
                TextButton(onClick = {
                    showSettingsDialog = false
                    // Ayarlar'a yönlendir
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
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
    }
}

/*@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfilePhotoPicker(
    visible: Boolean,
    onDismiss: () -> Unit,
    onTakePhotoClicked: () -> Unit,
    onPhotoPicked: (Uri) -> Unit
) {
    if (!visible) return

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onPhotoPicked(it)
            onDismiss()
        }
    }

    var showSettingsDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.PartiallyExpanded
        }
    )

    LaunchedEffect(Unit) {
        scope.launch {
            sheetState.show()
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { btm: Bitmap? ->
        this.bitmap = btm
        this.imageUri = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (isCameraSelected) {
                cameraLauncher.launch()
            } else {
                galleryLauncher.launch("image/*")
            }
            coroutineScope.launch {
                bottomSheetModalState.hide()
            }
        } else {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Fotoğraf Seç", style = MaterialTheme.typography.titleMedium)
            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            ListItem(
                headlineContent = { Text("Fotoğraf Çek") },
                leadingContent = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
                modifier = Modifier.clickable {
                    onTakePhotoClicked()
                    onDismiss()
                }
            )

            ListItem(
                headlineContent = { Text("Galeriden Seç") },
                leadingContent = { Icon(Icons.Default.PhotoLibrary, contentDescription = null) },
                modifier = Modifier.clickable {
                    galleryLauncher.launch("image/*")
                    onDismiss()
                }
            )
        }
    }

    if (showSettingsDialog) {
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
                    onDismiss()
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
    }
}*/




/*@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfilePhotoPicker(
    currentPhotoUri: Uri?,
    onPhotoPicked: (Uri) -> Unit
) {
    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Galeri seçici
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let(onPhotoPicked)
    }

    // Kamera seçici (Bitmap döner)
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let { bmp ->
            // Cache'e kaydet ve Uri döndür
            val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            onPhotoPicked(uri)
        }
    }

    var showOptions by remember { mutableStateOf(false) }
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    Box(contentAlignment = Alignment.Center) {
        // Profil ikonu/avatar
        AsyncImage(
            model = currentPhotoUri ?: R.drawable.ic_avatar,
            contentDescription = "Profil Fotoğrafı",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { showOptions = true },
            contentScale = ContentScale.Crop
        )

        if (showOptions) {
            AlertDialog(
                onDismissRequest = { showOptions = false },
                title = { Text("Profil Fotoğrafı Seç") },
                text = {
                    Column {
                        TextButton({
                            showOptions = false
                            if (cameraPermission.status.isGranted) {
                                cameraLauncher.launch()
                            } else {
                                cameraPermission.launchPermissionRequest()
                            }
                        }) { Text("Fotoğraf Çek") }

                        TextButton({
                            showOptions = false
                            galleryLauncher.launch("image/*")
                        }) { Text("Galeriden Seç") }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showOptions = false }) {
                        Text("İptal")
                    }
                }
            )
        }
    }

    // Permission denied durumunda gösterilecek dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Kamera İzni Gerekli") },
            text = { Text("Fotoğraf çekebilmek için Kamera izni gerekli. Ayarlardan izni açmak ister misiniz?") },
            confirmButton = {
                TextButton(onClick = {
                    // Uygulama ayarlarına git
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
    }

    LaunchedEffect(cameraPermission.status) {
        if (!cameraPermission.status.isGranted && !cameraPermission.status.shouldShowRationale) {
            showSettingsDialog = true
        }
    }
}
*/