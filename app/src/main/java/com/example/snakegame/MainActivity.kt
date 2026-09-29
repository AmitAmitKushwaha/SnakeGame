package com.example.snakegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import com.example.snakegame.ui1.GameScreen
import com.example.snakegame.ui1.HomeScreen
import com.example.snakegame.ui1.SettingsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var currentScreen by remember {
                mutableStateOf("home")
            }

            Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize()
            ) {

                // GAME SCREEN
                GameScreen(
                    visible = currentScreen == "game",
                    onBackClick = {
                        currentScreen = "home"
                    }
                )

                // HOME SCREEN
                if (currentScreen == "home") {

                    HomeScreen(
                        onPlayClick = {
                            currentScreen = "game"
                        },
                        onSettingsClick = {
                            currentScreen = "settings"
                        }
                    )
                }

                // SETTINGS SCREEN
                if (currentScreen == "settings") {

                    SettingsScreen(
                        onBackClick = {
                            currentScreen = "home"
                        }
                    )
                }
            }
        }
    }
}