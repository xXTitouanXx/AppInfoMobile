package com.polytech.app.view

import androidx.compose.foundation.layout.*
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
   // resultRecipient: ResultRecipient<FormViewDestination, FormData?>
) {
    var displayText by remember { mutableStateOf("Welcome to Home View!") }

//    resultRecipient.onNavResult { result ->
//        val data = when (result) {
//            is NavResult.Value -> result.value
//            else -> null
//        }
//
//        displayText = if (data != null) {
//            """
//            Nom: ${data.productName ?: "Non spécifié"}
//            Date d'achat: ${data.purchaseDate ?: "Non spécifié"}
//            Pays d'origine: ${data.origin ?: "Non spécifié"}
//            Type: ${data.selectedProductType ?: "Non spécifié"}
//                        Favoris: ${if (data.isFavorite) "Oui" else "Non"}
//
//            """.trimIndent()
//        } else {
//            "Aucune donnée reçue"
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(displayText)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navigator.navigate(FormViewDestination) }) {
            Text("Open Form")
        }
    }
}

