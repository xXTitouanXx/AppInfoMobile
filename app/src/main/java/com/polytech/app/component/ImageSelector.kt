package com.polytech.app.component

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.polytech.app.R
import java.io.File

@Composable
fun MyImageArea(
    uri: Uri? = null,
    directory: File? = null,
    onSetUri: (Uri) -> Unit = {},
    onImageAdded: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    fun getTempUri(): Uri? {
        directory?.let {
            it.mkdirs()
            val file =
                File.createTempFile("image_" + System.currentTimeMillis().toString(), ".jpg", it)
            return FileProvider.getUriForFile(context, authority, file)
        }
        return null
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onSetUri.invoke(it)
                onImageAdded?.invoke()
            }
        }
    )

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSaved ->
            tempUri.value?.let {
                if (isSaved) {
                    onSetUri.invoke(it)
                    onImageAdded?.invoke()
                }
            }
        }
    )
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val tmpUri = getTempUri()
            tempUri.value = tmpUri
            tmpUri?.let { takePhotoLauncher.launch(it) }
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        MyModalBottomSheet(
            onDismiss = { showBottomSheet = false },
            onTakePhotoClick = {
                showBottomSheet = false
                val permission = Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
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
            Text(text = "Gallerie / Prendre une photo")
        }

//        uri?.let {
//            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                AsyncImage(
//                    model = it,
//                    modifier = Modifier.size(160.dp),
//                    contentDescription = null,
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))
        //     }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalBottomSheet(
    onDismiss: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPhotoGalleryClick: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column {
            Button(onClick = onTakePhotoClick) {
                Text("Prendre une photo")
            }
            Button(onClick = onPhotoGalleryClick) {
                Text("Choisir dans la gallerie")
            }
        }
    }
}
