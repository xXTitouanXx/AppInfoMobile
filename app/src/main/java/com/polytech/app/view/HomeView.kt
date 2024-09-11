package com.polytech.app.view

import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    val scrollState = rememberScrollState()
    var displayText by remember { mutableStateOf("Welcome to Home View!") }
    var isFavorite by remember { mutableStateOf(false) }
    var selectedProductType by remember { mutableStateOf<String?>(null) }
    var productName by remember { mutableStateOf<String?>(null) }
    var purchaseDate by remember { mutableStateOf<String?>(null) }
    var origin by remember { mutableStateOf<String?>(null) }

    resultRecipient.onNavResult {
        if (it is NavResult.Value) {
            productName = it.value.productName.toString()
            purchaseDate = it.value.purchaseDate.toString()
            origin = it.value.origin.toString()
            selectedProductType = it.value.selectedProductType.toString()
            isFavorite = it.value.isFavorite
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(displayText)
        if (productName != null) {
            Text("Nom du produit: $productName")
        }
        if (purchaseDate != null) {
            Text("Date d'achat: $purchaseDate")
        }
        if (origin != null) {
            Text("Pays d'origine: $origin")
        }
        if (selectedProductType != null) {
            Text("Type de produit: $selectedProductType")
        }
        if (isFavorite) {
            Text("Favoris: Oui")
        } else {
            Text("Favoris: Non")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navigator.navigate(FormViewDestination) }) {
            Text("Open Form")
        }
    }
}

