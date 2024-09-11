package com.polytech.app.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.polytech.app.R
import com.polytech.app.model.FormData
import com.polytech.app.ui.theme.AppInfoMobileTheme
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

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val imageResId = when (selectedProductType) {
        "Consommable" -> R.drawable.kotlin // Remplace par le bon ID pour kotlin.svg
        "Durable" -> R.drawable.swift // Remplace par le bon ID pour swift.svg
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
            // Image du produit
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "image produit",
                modifier = Modifier.size(150.dp)
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
            TextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Nom du produit") },
                modifier = Modifier.fillMaxWidth()
            )

            // Sélecteur de date
            OutlinedTextField(
                value = purchaseDate,
                onValueChange = {},
                label = { Text("Date d'achat*") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { isDatePickerDialogOpen = true }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Sélectionner une date"
                        )
                    }
                }
            )

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
                    if (productName.isEmpty() || purchaseDate.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Veuillez remplir tous les champs obligatoires")
                        }
                    } else {
                        showConfirmationDialog = true
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