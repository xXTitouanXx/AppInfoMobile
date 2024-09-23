package com.polytech.app.ui.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.polytech.app.data.model.Accommodation

@Composable
fun AccommodationCard(accommodation: Accommodation) {
    val context = LocalContext.current
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
            InformationRow(
                label = "Étoiles:",
                value = accommodation.stars?.let { it.toString() } ?: "N/A")
            InformationRow(
                label = "Capacité:",
                value = accommodation.capacity?.let { it.toString() } ?: "N/A")
            InformationRow(label = "Internet:", value = accommodation.internetAccess ?: "N/A")
            InformationRow(
                label = "Téléphone:",
                value = accommodation.phone ?: "N/A",
                clickable = {
                    openPhone(context, accommodation.phone)
                },
                color = Color.Blue
            )

            // Rendre le site web cliquable
            InformationRow(
                label = "Site web:",
                value = buildAnnotatedString {
                    append(accommodation.website ?: "N/A")
                    if (accommodation.website != null) {
                        addStyle(
                            SpanStyle(textDecoration = TextDecoration.LineThrough),
                            length - accommodation.website.length, length
                        )
                    }
                }.toString(),
                clickable = {
                    openWebsite(context, accommodation.website)
                },
                color = Color.Blue
            )

            InformationRow(
                label = "Adresse:",
                value = accommodation.metaNameCom + "(" + accommodation.metaCodeCom + ")" + ", " + accommodation.metaNameDep + "(" + accommodation.metaCodeDep + ")" + ", " + accommodation.metaNameReg
            )
        }
    }
}

@Composable
fun InformationRow(
    label: String,
    value: String?,
    clickable: (() -> Unit)? = null,
    color: Color? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (clickable != null) Modifier.clickable { clickable() } else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value ?: "N/A",
            style = MaterialTheme.typography.bodyMedium,
            color = color ?: MaterialTheme.colorScheme.onBackground
        )
    }
}

fun openWebsite(context: Context, url: String?) {
    if (!url.isNullOrEmpty()) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}

fun openPhone(context: Context, phoneNumber: String?) {
    if (!phoneNumber.isNullOrEmpty()) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        context.startActivity(intent)
    }
}