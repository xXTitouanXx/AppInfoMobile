package com.polytech.app.ui.view

import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polytech.app.ui.component.AccommodationCard
import com.polytech.app.ui.view.destinations.HomeViewDestination
import com.polytech.app.viewmodel.MainViewModel

@Destination
@Composable
fun ListApiView(
    navigator: DestinationsNavigator,
) {
    val viewModel: MainViewModel = viewModel()
    val accommodations = viewModel.accommodations.value

    LaunchedEffect(Unit) {
        viewModel.getAccommodations()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth()
        ) {
            items(accommodations) { accommodation ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AccommodationCard(accommodation)
                }
            }
        }
        Button(
            onClick = { navigator.navigate(HomeViewDestination) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Retour")
        }
    }
}