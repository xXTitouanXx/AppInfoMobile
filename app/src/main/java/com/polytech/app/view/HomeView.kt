package com.polytech.app.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.polytech.app.component.DisplayProduct
import com.polytech.app.model.FormData
import com.polytech.app.view.destinations.EditProductViewDestination
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

    var products by rememberSaveable { mutableStateOf<List<FormData>>(emptyList()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    resultRecipient.onNavResult {
        if (it is NavResult.Value) {
            products = products + it.value
        }
    }

    // Séparer les produits favoris des autres produits
    val favoriteProducts = products.filter { it.isFavorite }
    val otherProducts = products.filterNot { it.isFavorite }

    // Filtrer les produits en fonction de la requête de recherche
    val filteredFavoriteProducts = favoriteProducts.filter {
        it.productName?.contains(searchQuery, ignoreCase = true) ?:
    }
    val filteredOtherProducts = otherProducts.filter {
        it.productName?.contains(searchQuery, ignoreCase = true) ?:
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Champ de recherche
        TextField(
            value = searchQuery,
            onValueChange = { newQuery -> searchQuery = newQuery },
            label = { Text("Rechercher") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Afficher les produits favoris en LazyRow
        if (filteredFavoriteProducts.isNotEmpty()) {
            Text("Produits Favoris", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(filteredFavoriteProducts) { product ->
                    DisplayProduct(
                        product = product,
                        onItemClick = {
                            navigator.navigate(EditProductViewDestination(product)) { result ->
                                result.onSuccess { updatedProduct ->
                                    products = products.map {
                                        if (it.id == updatedProduct.id) updatedProduct else it
                                    }
                                }
                            }
                        },
                        onItemLongClick = {
                            products = products.filterNot { it == product }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Afficher les autres produits en LazyVerticalGrid
        if (filteredOtherProducts.isNotEmpty()) {
            Text("Tous les Produits", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // Nombre de colonnes dans la grille
                modifier = Modifier.weight(1f)
            ) {
                items(filteredOtherProducts) { product ->
                    DisplayProduct(
                        product = product,
                        onItemClick = {
                            navigator.navigate(EditProductViewDestination(product)) { result ->
                                result.onSuccess { updatedProduct ->
                                    products = products.map {
                                        if (it.id == updatedProduct.id) updatedProduct else it
                                    }
                                }
                            }
                        },
                        onItemLongClick = {
                            products = products.filterNot { it == product }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navigator.navigate(FormViewDestination) }) {
            Text("Open Form")
        }
    }
}

