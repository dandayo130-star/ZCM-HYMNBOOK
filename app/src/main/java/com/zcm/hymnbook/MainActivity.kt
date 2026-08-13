package com.zcm.hymnbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.installSplashScreen
import com.zcm.hymnbook.ui.navigation.ZcmNavGraph
import com.zcm.hymnbook.ui.theme.ZcmHymnBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ZcmHymnBookApp

        setContent {
            val appTheme by app.userPreferencesRepository.theme.collectAsState(
                initial = com.zcm.hymnbook.data.preferences.AppTheme.SYSTEM
            )

            ZcmHymnBookTheme(appTheme = appTheme) {
                ZcmNavGraph(
                    hymnRepository = app.hymnRepository,
                    preferencesRepository = app.userPreferencesRepository
                )
            }
        }
    }
}
