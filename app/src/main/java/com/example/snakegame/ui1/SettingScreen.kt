package com.example.snakegame.ui1

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.snakegame.DataStoreManager
import com.example.snakegame.R

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val scope = rememberCoroutineScope()

    // Read settings from DataStore
    val sound by dataStoreManager.sound.collectAsState(initial = true)
    val vibration by dataStoreManager.vibration.collectAsState(initial = true)
    val showGrid by dataStoreManager.showGrid.collectAsState(initial = true)
    val snakeColor by dataStoreManager.snakeColor.collectAsState(initial = "Green")

    // Play sound
    fun playSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.eat)

        mediaPlayer?.setOnCompletionListener {
            it.release()
        }

        mediaPlayer?.start()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // ---------------- SETTINGS TITLE ----------------

        Text(
            text = "⚙ Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // ---------------- SOUND ----------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Sound",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = sound,
                onCheckedChange = { enabled ->

                    scope.launch {
                        dataStoreManager.saveSound(enabled)
                    }

                    if (enabled) {
                        playSound()
                    }
                }
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ---------------- VIBRATION ----------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Vibration",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = vibration,
                onCheckedChange = { enabled ->

                    scope.launch {
                        dataStoreManager.saveVibration(enabled)
                    }
                }
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ---------------- SHOW GRID ----------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Show Grid",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = showGrid,
                onCheckedChange = { enabled ->

                    scope.launch {
                        dataStoreManager.saveShowGrid(enabled)
                    }
                }
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // ---------------- SNAKE COLOR ----------------

        Text(
            text = "🐍 Snake Color",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row {

            // GREEN

            Button(
                onClick = {
                    scope.launch {
                        dataStoreManager.saveSnakeColor("Green")
                    }
                }
            ) {
                Text("Green")
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            // BLUE

            Button(
                onClick = {
                    scope.launch {
                        dataStoreManager.saveSnakeColor("Blue")
                    }
                }
            ) {
                Text("Blue")
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            // RED

            Button(
                onClick = {
                    scope.launch {
                        dataStoreManager.saveSnakeColor("Red")
                    }
                }
            ) {
                Text("Red")
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // SELECTED COLOR

        Text(
            text = "Selected: $snakeColor",
            color = when (snakeColor) {
                "Green" -> Color.Green
                "Blue" -> Color.Blue
                "Red" -> Color.Red
                else -> Color.Green
            }
        )

        // Push Back button to bottom

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // ---------------- BACK BUTTON ----------------

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Back")
        }
    }
}


// ---------------- PREVIEW ----------------

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {

    SettingsScreen(
        onBackClick = {}
    )
}