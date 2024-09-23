package com.polytech.app.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.polytech.app.data.model.FormData
import com.polytech.app.ui.view.destinations.HomeViewDestination
import com.polytech.app.ui.viewmodel.ProductViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun EditProductView(
    navigator: DestinationsNavigator,
    product: FormData,
    viewModel: ProductViewModel = koinViewModel(),
    //resultNavigator: ResultBackNavigator<FormData>
) {
    var productName by remember { mutableStateOf(product.productName.orEmpty()) }
    var purchaseDate by remember { mutableStateOf(product.purchaseDate.orEmpty()) }
    var origin by remember { mutableStateOf(product.origin.orEmpty()) }
    var selectedProductType by remember { mutableStateOf(product.selectedProductType.orEmpty()) }
    var isFavorite by remember { mutableStateOf(product.isFavorite) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            val updatedProduct = product.copy(
                productName = productName,
                purchaseDate = purchaseDate,
                origin = origin,
                selectedProductType = selectedProductType,
                isFavorite = isFavorite
            )
            viewModel.updateProduct(updatedProduct)
            navigator.navigate(HomeViewDestination)
            //resultNavigator.navigateBack(updatedProduct)
        }) {
            Text("Enregistrer")
        }
    }
}
