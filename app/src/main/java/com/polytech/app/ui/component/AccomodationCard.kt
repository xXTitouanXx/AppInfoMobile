package com.polytech.app.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.polytech.app.data.model.Accommodation
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AccommodationCard(accommodation: Accommodation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = accommodation.name ?: "Nom indisponible",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            InformationRow(label = "Type:", value = accommodation.type)
            InformationRow(label = "Opérateur:", value = accommodation.operator)
            InformationRow(label = "Étoiles:", value = accommodation.stars?.let { it.toString() } ?: "N/A")
            InformationRow(label = "Capacité:", value = accommodation.capacity?.let { it.toString() } ?: "N/A")
            InformationRow(label = "Internet:", value = accommodation.internetAccess ?: "N/A")
            InformationRow(label = "Téléphone:", value = accommodation.phone ?: "N/A")

            // Ajoute d'autres informations si nécessaire
        }
    }
}

@Composable
fun InformationRow(label: String, value: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value ?: "N/A", style = MaterialTheme.typography.bodyMedium)
    }
}
