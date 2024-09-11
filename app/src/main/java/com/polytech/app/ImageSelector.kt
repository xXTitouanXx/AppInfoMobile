package com.polytech.app

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun MyImageArea(
    uri: Uri? = null, // URI de l'image à afficher
    directory: File? = null, // Répertoire pour stocker les images
    onSetUri: (Uri) -> Unit = {} // Callback pour gérer l'URI de l'image sélectionnée ou prise
) {
    val context = LocalContext.current
    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    // Fonction pour obtenir l'URI temporaire pour stocker la photo
    fun getTempUri(): Uri? {
        directory?.let {
            it.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                it
            )
            return FileProvider.getUriForFile(
                context,
                authority,
                file
            )
        }
        return null
    }

    // Launcher pour la sélection d'image depuis la galerie
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onSetUri.invoke(it)
            }
        }
    )

    // Launcher pour la prise de photo
    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSaved ->
            tempUri.value?.let {
                if (isSaved) {
                    onSetUri.invoke(it)
                }
            }
        }
    )

    // Launcher pour demander la permission de la caméra
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val tmpUri = getTempUri()
            tempUri.value = tmpUri
            tmpUri?.let { takePhotoLauncher.launch(it) }
        } else {
            // Gérer le cas où la permission est refusée
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }

    // Afficher une feuille de bas pour choisir entre prendre une photo ou choisir depuis la galerie
    if (showBottomSheet) {
        MyModalBottomSheet(
            onDismiss = { showBottomSheet = false },
            onTakePhotoClick = {
                showBottomSheet = false
                val permission = Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    val tmpUri = getTempUri()
                    tempUri.value = tmpUri
                    tmpUri?.let { takePhotoLauncher.launch(it) }
                } else {
                    cameraPermissionLauncher.launch(permission)
                }
            },
            onPhotoGalleryClick = {
                showBottomSheet = false
                imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { showBottomSheet = true }) {
            Text(text = "Select / Take Photo")
        }

        uri?.let {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val painter: Painter = rememberImagePainter(it)
                Image(painter = painter, contentDescription = null, modifier = Modifier.size(160.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalBottomSheet(
    onDismiss: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPhotoGalleryClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column {
            Button(onClick = onTakePhotoClick) {
                Text("Take Photo")
            }
            Button(onClick = onPhotoGalleryClick) {
                Text("Choose from Gallery")
            }
        }
    }
}


