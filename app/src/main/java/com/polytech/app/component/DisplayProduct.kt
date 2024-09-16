package com.polytech.app.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import com.polytech.app.model.FormData

@Composable
fun DisplayProduct(
    product: FormData,
    onItemClick: () -> Unit,
    onItemLongClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onItemClick)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onItemLongClick() }
                )
            },
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nom du produit: ${product.productName}")
            Text("Date d'achat: ${product.purchaseDate}")
            Text("Pays d'origine: ${product.origin}")
            Text("Type de produit: ${product.selectedProductType}")
            Text("Favoris: ${if (product.isFavorite) "Oui" else "Non"}")
        }
    }
}
