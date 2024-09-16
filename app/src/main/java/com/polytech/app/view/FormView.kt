package com.polytech.app.view

import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.polytech.app.component.MyImageArea
import com.polytech.app.R
import com.polytech.app.model.FormData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormView(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<FormData>
) {
    var isFavorite by remember { mutableStateOf(false) }
    var selectedProductType by remember { mutableStateOf("Consommable") }
    var productName by remember { mutableStateOf("") }
    var purchaseDate by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Black) }
    var origin by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isDatePickerDialogOpen by remember { mutableStateOf(false) }

    var productNameError by remember { mutableStateOf<String?>(null) }
    var purchaseDateError by remember { mutableStateOf<String?>(null) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val imageResId = when (selectedProductType) {
        "Consommable" -> R.drawable.kotlin
        "Durable" -> R.drawable.swift
        else -> R.drawable.android
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Afficher l'image ajoutée ou celle par défaut
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "image produit",
                    modifier = Modifier.size(150.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "image produit",
                    modifier = Modifier.size(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            MyImageArea(
                uri = imageUri,
                directory = directory,
                onSetUri = { uri -> imageUri = uri },
                onImageAdded = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Image ajoutée avec succès")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Sélection du type de produit
            Column(
                Modifier.selectableGroup(),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedProductType == "Consommable",
                        onClick = { selectedProductType = "Consommable" }
                    )
                    Text("Consommable")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedProductType == "Durable",
                        onClick = { selectedProductType = "Durable" }
                    )
                    Text("Durable")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedProductType == "Autre",
                        onClick = { selectedProductType = "Autre" }
                    )
                    Text("Autre")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Champ pour le nom du produit
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = productName,
                    onValueChange = {
                        productName = it
                        productNameError =
                            if (it.isEmpty()) "Le nom du produit est requis" else null
                    },
                    label = { Text("Nom du produit*") },
                    isError = productNameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (productNameError != null) {
                    Text(
                        text = productNameError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Sélecteur de date
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = purchaseDate,
                    onValueChange = {},
                    label = { Text("Date d'achat*") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    isError = purchaseDateError != null,
                    trailingIcon = {
                        IconButton(onClick = { isDatePickerDialogOpen = true }) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Sélectionner une date"
                            )
                        }
                    }
                )
                if (purchaseDateError != null) {
                    Text(
                        text = purchaseDateError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // DatePickerDialog
            if (isDatePickerDialogOpen) {
                val datePickerState = rememberDatePickerState()
                val confirmEnabled =
                    remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                DatePickerDialog(
                    onDismissRequest = { isDatePickerDialogOpen = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isDatePickerDialogOpen = false
                                purchaseDate =
                                    dateFormatter.format(datePickerState.selectedDateMillis)
                            },
                            enabled = confirmEnabled.value
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isDatePickerDialogOpen = false }) {
                            Text("Annuler")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Choix de la couleur
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(150.dp),
                controller = rememberColorPickerController(),
                onColorChanged = { colorEnvelope ->
                    color = colorEnvelope.color
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Couleur du produit: ${color.toHex()}")

            // Champ pour le pays d'origine
            OutlinedTextField(
                value = origin,
                onValueChange = { origin = it },
                label = { Text("Pays d'origine du produit") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox "Ajouter aux favoris"
            Row(verticalAlignment = Alignment.CenterVertically) {
                HeartCheckbox(
                    isFavorite = isFavorite,
                    onCheckedChange = { isFavorite = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ajouter aux favoris")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton de validation
            Button(
                onClick = {
                    // Réinitialiser les messages d'erreur
                    productNameError =
                        if (productName.isEmpty()) "Le nom du produit est requis" else null
                    purchaseDateError =
                        if (purchaseDate.isEmpty()) "La date d'achat est requise" else null

                    if (productName.isNotEmpty() && purchaseDate.isNotEmpty()) {
                        showConfirmationDialog = true
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Veuillez remplir tous les champs obligatoires")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Valider")
            }

            // Confirmation dialog
            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = { Text("Confirmer") },
                    text = { Text("Êtes-vous sûr de vouloir valider les informations ?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showConfirmationDialog = false
                                showDialog = true
                                val formData = FormData(
                                    productName = productName,
                                    purchaseDate = purchaseDate,
                                    origin = origin,
                                    selectedProductType = selectedProductType,
                                    isFavorite = isFavorite
                                )
                                resultNavigator.navigateBack(formData)
                            }
                        ) {
                            Text("Oui")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showConfirmationDialog = false
                        }) { Text("Annuler") }
                    }
                )
            }

            // AlertDialog pour montrer les détails du formulaire
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Détails du Produit") },
                    text = {
                        Text("Nom: $productName\nDate d'achat: $purchaseDate\nCouleur: $color\nPays d'origine: $origin\nType: $selectedProductType\nFavoris: ${if (isFavorite) "Oui" else "Non"}")
                    }
                )
            }
        }
    }
}

@Composable
fun HeartCheckbox(isFavorite: Boolean, onCheckedChange: (Boolean) -> Unit) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = onCheckedChange
    ) {
        Icon(
            painter = painterResource(
                id = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
            ),
            contentDescription = "Favorite",
            tint = if (isFavorite) Color.Red else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun Color.toHex(): String {
    return "#${Integer.toHexString(toArgb()).uppercase()}"
}
