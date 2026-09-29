package com.example.snakegame.ui1
import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableIntStateOf
import com.example.snakegame.Direction
import com.example.snakegame.Position
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.snakegame.DataStoreManager
import com.example.snakegame.R
import androidx.compose.ui.draw.alpha

@Composable
fun GameScreen(
    onBackClick: () -> Unit,
    visible: Boolean
) {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }

    val soundEnabled by dataStoreManager.sound.collectAsState(initial = true)
    val gridEnabled by dataStoreManager.showGrid.collectAsState(initial = true)
    val snakeColor by dataStoreManager.snakeColor.collectAsState(initial = "Green")

    var snake by remember {
        mutableStateOf(
            listOf(
                Position(5, 5),
                Position(5, 6),
                Position(5, 7)
            )
        )
    }

    var isPaused by remember {
        mutableStateOf(false)
    }
    var direction by remember {
        mutableStateOf(Direction.RIGHT)
    }
    var food by remember {
        mutableStateOf(Position(6, 5))
    }
    var score by remember {
        mutableIntStateOf(0)
    }
    var gameOver by remember {
        mutableStateOf(false)
    }
    var restartKey by remember {
        mutableIntStateOf(0)
    }
    var highScore by remember {
        mutableIntStateOf(0)
    }

    fun playSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.eat)

        mediaPlayer?.setOnCompletionListener {
            it.release()
        }

        mediaPlayer?.start()
    }

    LaunchedEffect(direction, restartKey) {

        while (true) {
            if (isPaused) {
                delay(100.toLong().milliseconds)
                continue
            }
            val speed = maxOf(80, 300 - score * 10)
            delay(speed.toLong().milliseconds)

            val head = snake.first()

            val newHead = when (direction) {

                Direction.RIGHT ->
                    Position((head.x + 1) % 16, head.y)

                Direction.LEFT ->
                    Position((head.x - 1 + 16) % 16, head.y)

                Direction.UP ->
                    Position(head.x, (head.y - 1 + 16) % 16)

                Direction.DOWN ->
                    Position(head.x, (head.y + 1) % 16)
            }


            // Self collision
            if (newHead in snake) {

                if (newHead in snake) {

                    if (soundEnabled) {
                        playSound()
                    }

                    gameOver = true
                    break
                }

                gameOver = true
                break
            }

            if (newHead == food) {
                if (soundEnabled) {
                    playSound()
                }

                score++

                if (score > highScore) {
                    highScore = score
                }

                do {
                    food = Position(
                        (0..15).random(),
                        (0..15).random()
                    )
                } while (food in snake)

                snake = listOf(newHead) + snake
            } else {
                snake = listOf(newHead) + snake.dropLast(1)
            }

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .alpha(if (visible) 1f else 0f),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Text(
            text = "Score : $score",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "High Score : $highScore",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBackClick
        ) {
            Text("← Home")
        }

        Button(
            onClick = {
                isPaused = !isPaused
            }
        ) {
            Text(
                if (isPaused) "▶ Resume"
                else "⏸ Pause"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (gameOver) {
            Text(
                text = "GAME OVER",
                color = Color.Red,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Text(
            text = "Selected Color: $snakeColor"
        )

        Canvas(
            modifier = Modifier
                .size(320.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (gameOver || isPaused) return@detectDragGestures
                        val (x, y) = dragAmount

                        if (kotlin.math.abs(x) > kotlin.math.abs(y)) {

                            if (x > 0 && direction != Direction.LEFT) {
                                direction = Direction.RIGHT
                            } else if (x < 0 && direction != Direction.RIGHT) {
                                direction = Direction.LEFT
                            }

                        } else {

                            if (y > 0 && direction != Direction.UP) {
                                direction = Direction.DOWN
                            } else if (y < 0 && direction != Direction.DOWN) {
                                direction = Direction.UP
                            }
                        }

                        change.consume()
                    }
                }
        )
        {

            val cellSize = size.width / 16f
            if (gridEnabled) {

                // Vertical lines
                for (i in 0..16) {
                    drawLine(
                        color = Color(0xFF3F51B5),
                        start = Offset(i * cellSize, 0f),
                        end = Offset(i * cellSize, size.height),


                    )
                }

                // Horizontal lines
                for (i in 0..16) {
                    drawLine(
                        color = Color(0xFF3F51B5),
                        start = Offset(0f, i * cellSize),
                        end = Offset(size.width, i * cellSize),
                    )
                }
            }
            val bodyColor = when (snakeColor) {
                "Green" -> Color.Green
                "Blue" -> Color.Blue
                "Red" -> Color.Red
                else -> Color.Green
            }
            // Snake
            snake.forEachIndexed { index, part ->

                val partColor = if (index == 0) {
                    Color(0xFFFF5722)      // HEAD color
                } else {
                    bodyColor          // BODY color
                }

                drawRoundRect(
                    color = partColor,
                    topLeft = Offset(
                        part.x * cellSize + 2f,
                        part.y * cellSize + 2f
                    ),
                    size = Size(
                        cellSize - 4f,
                        cellSize - 4f
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(25f, 25f
                    )
                )
                // Food (Berry + Leaf)

                drawCircle(
                    color = Color(0xFFE53935),
                    radius = cellSize * 0.35f,
                    center = Offset(
                        food.x * cellSize + cellSize / 2,
                        food.y * cellSize + cellSize / 2
                    )
                )

// Leaf 1
                drawOval(
                    color = Color(0xFF4CAF50),
                    topLeft = Offset(
                        food.x * cellSize + cellSize * 0.45f,
                        food.y * cellSize + cellSize * 0.05f
                    ),
                    size = Size(
                        cellSize * 0.22f,
                        cellSize * 0.12f
                    )
                )

// Leaf 2
                drawOval(
                    color = Color(0xFF66BB6A),
                    topLeft = Offset(
                        food.x * cellSize + cellSize * 0.30f,
                        food.y * cellSize + cellSize * 0.12f
                    ),
                    size = Size(
                        cellSize * 0.18f,
                        cellSize * 0.10f
                    )
                )
            }

            }
            if (gameOver) {

                Button(
                    onClick = {
                        snake = listOf(
                            Position(5, 5),
                            Position(5, 6),
                            Position(5, 7)
                        )

                        direction = Direction.RIGHT
                        food = Position(6, 5)
                        score = 0
                        gameOver = false
                        isPaused = false
                        restartKey++
                    }
                ) {
                    Text("Play Again")
                }

            }

        }
    }


@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    GameScreen(
        onBackClick = {},
        true
    )
}