package com.polytech.app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    onProductUpdated: (FormData) -> Unit
) {
    var productName by remember { mutableStateOf(product.productName ?: "") }
    var productType by remember { mutableStateOf(product.selectedProductType ?: "") }
    var isFavorite by remember { mutableStateOf(product.isFavorite) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nom du produit") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = productType,
            onValueChange = { productType = it },
            label = { Text("Type de produit") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Favoris")
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val updatedProduct = product.copy(
                productName = productName,
                selectedProductType = productType,
                isFavorite = isFavorite
            )
            onProductUpdated(updatedProduct)
            navigator.navigateUp()
        }) {
            Text("Enregistrer")
        }
    }
}
