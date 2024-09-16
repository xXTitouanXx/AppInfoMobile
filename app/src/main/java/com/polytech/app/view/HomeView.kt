package com.polytech.app.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.polytech.app.component.DisplayProduct
import com.polytech.app.model.FormData
import com.polytech.app.view.destinations.FormViewDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination(start = true)
@Composable
fun HomeView(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<FormViewDestination, FormData>
) {
    val context = LocalContext.current

    // Liste mutable pour stocker les produits
    var products by remember { mutableStateOf<List<FormData>>(emptyList()) }

    // Ajouter les produits lorsqu'une nouvelle valeur est reçue
    resultRecipient.onNavResult {
        if (it is NavResult.Value) {
            products = products + it.value
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // LazyColumn pour afficher la liste des produits
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(products) { product ->
                DisplayProduct(
                    product = product,
                    onItemClick = {
                        Toast.makeText(
                            context,
                            "Produit cliqué: ${product.productName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onItemLongClick = {
                        // Supprimer le produit de la liste
                        products = products.filterNot { it == product }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour ouvrir le formulaire
        Button(onClick = { navigator.navigate(FormViewDestination) }) {
            Text("Open Form")
        }
    }
}
