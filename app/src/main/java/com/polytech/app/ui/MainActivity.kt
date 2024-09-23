package com.polytech.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polytech.app.ui.component.PostList
import com.polytech.app.ui.view.NavGraphs
import com.polytech.app.ui.view.ui.theme.AppInfoMobileTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.polytech.app.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppInfoMobileTheme {
                val viewModel: MainViewModel = viewModel()
                PostList(viewModel)
                DestinationsNavHost(
                    navGraph = NavGraphs.root
                )
            }
        }
    }
}
