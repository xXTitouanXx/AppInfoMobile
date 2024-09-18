package com.polytech.app.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.polytech.app.R
import com.polytech.app.model.FormData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayProduct(
    product: FormData, onItemClick: () -> Unit, onItemLongClick: () -> Unit
) {
    val imageResId = when (product.selectedProductType) {
        "Consommable" -> R.drawable.kotlin
        "Durable" -> R.drawable.swift
        else -> R.drawable.android
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = onItemClick, onLongClick = onItemLongClick
            ),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (product.imageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(product.imageUri)
                        .crossfade(true).build(),
                    contentDescription = "Image du produit",
                    modifier = Modifier
                        .size(150.dp)
                        .fillMaxWidth()
                )
            } else {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Image produit",
                    modifier = Modifier.size(150.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Nom du produit: ${product.productName}")
            Text("Date d'achat: ${product.purchaseDate}")
            Text("Pays d'origine: ${product.origin}")
            Text("Type de produit: ${product.selectedProductType}")
            Text("Favoris: ${if (product.isFavorite) "Oui" else "Non"}")
        }
    }
}