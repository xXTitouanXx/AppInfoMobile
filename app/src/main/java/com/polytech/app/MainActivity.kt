package com.polytech.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.polytech.app.view.ui.theme.AppInfoMobileTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.polytech.app.view.NavGraphs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppInfoMobileTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                )
            }
        }
    }
}
