package com.polytech.app.ui.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.polytech.app.ui.component.DisplayProduct
import com.polytech.app.data.model.FormData
import com.polytech.app.ui.view.destinations.EditProductViewDestination
import com.polytech.app.ui.view.destinations.FormViewDestination
import com.polytech.app.ui.view.destinations.ListApiViewDestination
import com.polytech.app.ui.viewmodel.ProductViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import org.koin.androidx.compose.koinViewModel

@Destination(start = true)
@Composable
fun HomeView(
    navigator: DestinationsNavigator,
    productViewModel: ProductViewModel = koinViewModel(),
    //resultRecipient: ResultRecipient<FormViewDestination, FormData>
) {
    val context = LocalContext.current

    //var products by rememberSaveable { mutableStateOf<List<FormData>>(emptyList()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val products by productViewModel.products.collectAsState()
    LaunchedEffect(products) {
        Log.d("HomeView", "Products in HomeView: ${productViewModel.products}")
    }

    val favoriteProducts = products.filter { it.isFavorite }
    val otherProducts = products.filterNot { it.isFavorite }

    val filteredFavoriteProducts = favoriteProducts.filter {
        it.productName?.contains(searchQuery, ignoreCase = true) ?: false
    }
    val filteredOtherProducts = otherProducts.filter {
        it.productName?.contains(searchQuery, ignoreCase = true) ?: false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { newQuery -> searchQuery = newQuery },
                label = { Text("Rechercher") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

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
                                Toast.makeText(
                                    context,
                                    "${product.productName} sélectionné",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navigator.navigate(EditProductViewDestination(product))
                            },
                            onItemLongClick = {
                                productViewModel.removeProduct(product)
                                Toast.makeText(
                                    context,
                                    "${product.productName} supprimé",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredOtherProducts.isNotEmpty()) {
                Text("Tous les Produits", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(filteredOtherProducts) { product ->
                        DisplayProduct(
                            product = product,
                            onItemClick = {
                                Toast.makeText(
                                    context,
                                    "${product.productName} sélectionné",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navigator.navigate(EditProductViewDestination(product))
                            },
                            onItemLongClick = {
                                productViewModel.removeProduct(product)
                                Toast.makeText(
                                    context,
                                    "${product.productName} supprimé",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {navigator.navigate(ListApiViewDestination)},
                modifier = Modifier
            ) {
                Text("Liste d'hébergements")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { navigator.navigate(FormViewDestination) },
                modifier = Modifier
            ) {
                Text("Ouvrir le formulaire")
            }
        }
    }
}