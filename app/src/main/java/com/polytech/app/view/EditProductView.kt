package com.polytech.app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.polytech.app.model.FormData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun EditProductView(
    navigator: DestinationsNavigator,
    product: FormData,
    onSave: (FormData) -> Unit
) {
    var productName by remember { mutableStateOf(product.productName.orEmpty()) }
    var purchaseDate by remember { mutableStateOf(product.purchaseDate.orEmpty()) }
    var origin by remember { mutableStateOf(product.origin.orEmpty()) }
    var selectedProductType by remember { mutableStateOf(product.selectedProductType.orEmpty()) }
    var isFavorite by remember { mutableStateOf(product.isFavorite) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nom du produit") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = purchaseDate,
            onValueChange = { purchaseDate = it },
            label = { Text("Date d'achat") }
        )
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
            onSave(
                product.copy(
                    productName = productName,
                    purchaseDate = purchaseDate,
                    origin = origin,
                    selectedProductType = selectedProductType,
                    isFavorite = isFavorite
                )
            )
            navigator.popBackStack()
        }) {
            Text("Enregistrer")
        }
    }
}
