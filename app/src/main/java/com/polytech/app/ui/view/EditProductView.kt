package com.polytech.app.ui.view

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.polytech.app.data.model.FormData
import com.polytech.app.ui.component.MyImageArea
import com.polytech.app.ui.view.destinations.HomeViewDestination
import com.polytech.app.ui.viewmodel.ProductViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun EditProductView(
    navigator: DestinationsNavigator,
    product: FormData,
    viewModel: ProductViewModel = koinViewModel(),
) {
    var productName by remember { mutableStateOf(product.productName.orEmpty()) }
    var purchaseDate by remember { mutableStateOf(product.purchaseDate.orEmpty()) }
    var origin by remember { mutableStateOf(product.origin.orEmpty()) }
    var selectedProductType by remember { mutableStateOf(product.selectedProductType.orEmpty()) }
    var isFavorite by remember { mutableStateOf(product.isFavorite) }
    var imageUri by remember { mutableStateOf<Uri?>(product.imageUri) }
    var showDialog by remember { mutableStateOf(false) }
    var isDatePickerDialogOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Afficher l'image ajoutée ou celle par défaut
        AsyncImage(
            model = imageUri,
            contentDescription = "image produit",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Image Area
        MyImageArea(
            uri = imageUri,
            directory = context.cacheDir,
            onSetUri = { uri -> imageUri = uri },
            onImageAdded = {
                scope.launch {
                    snackbarHostState.showSnackbar("Image ajoutée avec succès")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nom du produit") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Sélecteur de date
        OutlinedTextField(
            value = purchaseDate,
            onValueChange = {},
            label = { Text("Date d'achat") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { isDatePickerDialogOpen = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Sélectionner une date")
                }
            }
        )

        // DatePickerDialog
        if (isDatePickerDialogOpen) {
            val datePickerState = rememberDatePickerState()
            val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            DatePickerDialog(
                onDismissRequest = { isDatePickerDialogOpen = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isDatePickerDialogOpen = false
                            purchaseDate = dateFormatter.format(datePickerState.selectedDateMillis)
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

        TextField(
            value = origin,
            onValueChange = { origin = it },
            label = { Text("Pays d'origine") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = selectedProductType,
            onValueChange = { selectedProductType = it },
            label = { Text("Type de produit") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text("Favori")
            Switch(
                checked = isFavorite,
                onCheckedChange = { isFavorite = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val updatedProduct = product.copy(
                productName = productName,
                purchaseDate = purchaseDate,
                origin = origin,
                selectedProductType = selectedProductType,
                isFavorite = isFavorite,
                imageUri = imageUri
            )
            viewModel.updateProduct(updatedProduct)
            navigator.navigate(HomeViewDestination)
        }) {
            Text("Enregistrer")
        }
    }
}

