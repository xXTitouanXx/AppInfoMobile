package com.polytech.app

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.polytech.app.ui.theme.AppInfoMobileTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppInfoMobileTheme {
                FormScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume")
    }
}

@Composable
fun Color.toHex(): String {
    return "#${Integer.toHexString(toArgb()).uppercase()}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen() {
    var isFavorite by remember { mutableStateOf(false) }
    var selectedProductType by remember { mutableStateOf("Consommable") }
    var productName by remember { mutableStateOf("") }
    var purchaseDate by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Black) }
    var origin by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val datePickerState = remember { mutableStateOf(Calendar.getInstance()) }


    fun showDatePickerDialog(currentDate: Calendar) {
        val datePickerDialog = DatePickerDialog(
            currentDate.context, // You need to pass context here
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                purchaseDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
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
            Image(
                painter = painterResource(id = R.drawable.photo),
                contentDescription = "image produit",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

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

            TextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Nom du produit") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = purchaseDate,
                onValueChange = { purchaseDate = it },
                label = { Text("Date d’achat") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePickerDialog = true }
            )

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

            OutlinedTextField(
                value = origin,
                onValueChange = { origin = it },
                label = { Text("Pays d'origine du produit") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeartCheckbox(
                    isFavorite = isFavorite,
                    onCheckedChange = { isFavorite = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ajouter au favoris")
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            // Confirmation Dialog
            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = { Text("Confirmer") },
                    text = {
                        Text("Êtes-vous sûr de vouloir valider les informations ?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // Finaliser l'action ici
                                showConfirmationDialog = false
                                showDialog = true
                            }
                        ) {
                            Text("Oui")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmationDialog = false }) { Text("Annuler") }
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

            // DatePickerDialog
            if (showDatePickerDialog) {
                val datePicker = DatePickerDialog(
                    onDismissRequest = { showDatePickerDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // Get the selected date and update the state
                                val selectedDate = datePickerState.value.time
                                purchaseDate =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        selectedDate
                                    )
                                showDatePickerDialog = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePickerDialog = false }) { Text("Annuler") }
                    }
                )
                datePicker.show()
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

@Preview
@Composable
fun FormScreenPreview() {
    AppInfoMobileTheme {
        FormScreen()
    }
}
