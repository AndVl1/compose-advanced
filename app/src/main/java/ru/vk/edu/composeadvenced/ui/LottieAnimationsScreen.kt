package ru.vk.edu.composeadvenced.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import ru.vk.edu.composeadvenced.screens.LottieAnimationsComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar

data class LottieAnimationItem(
    val title: String,
    val description: String,
    val url: String,
    val isPlaying: Boolean = true,
    val speed: Float = 1f,
    val iterations: Int = LottieConstants.IterateForever,
    val backgroundColor: Color = Color.Transparent
)

@Composable
fun LottieAnimationsScreen(component: LottieAnimationsComponent) {
    val animationItems = remember {
        listOf(
            LottieAnimationItem(
                title = "Loading Animation",
                description = "Стандартная анимация загрузки с обычной скоростью",
                url = "https://raw.githubusercontent.com/airbnb/lottie-android/refs/heads/master/sample/src/main/assets/Lottie%20Logo%201.json",
                speed = 1f
            ),
            LottieAnimationItem(
                title = "Fast Loading",
                description = "Другая анимация загрузки, скорость  х2",
                url = "https://raw.githubusercontent.com/airbnb/lottie-android/refs/heads/master/sample/src/main/assets/Lottie%20Logo%202.json",
                speed = 2f
            ),
            LottieAnimationItem(
                title = "Slow Motion",
                description = "Замедленная анимация для детального просмотра",
                url = "https://raw.githubusercontent.com/airbnb/lottie-android/refs/heads/master/sample/src/main/assets/Lottie%20Logo%201.json",
                speed = 0.5f
            ),
            LottieAnimationItem(
                title = "Calendar",
                description = "Анимация календаря",
                url = "https://raw.githubusercontent.com/thesvbd/Lottie-examples/refs/heads/master/assets/animations/calendar.json",
                speed = 1f,
                iterations = 5
            ),
        )
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Lottie Animations",
                showBackButton = true,
                onBackClick = { component.onBackClicked() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(animationItems) { item ->
                LottieAnimationCard(item)
            }
        }
    }
}

@Composable
fun LottieAnimationCard(item: LottieAnimationItem) {
    var isPlaying by remember { mutableStateOf(item.isPlaying) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Animation container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(item.backgroundColor)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Url(item.url)
                )
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    isPlaying = isPlaying,
                    speed = item.speed,
                    iterations = item.iterations,
                    restartOnPlay = false
                )

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(120.dp)
                )
            }

            // Info section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Animation parameters
                    Column {
                        Text(
                            text = "Speed: ${item.speed}x",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (item.iterations == LottieConstants.IterateForever) "∞ loops" else "${item.iterations} loop(s)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Play/Pause button
                    Button(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = if (isPlaying) "Pause" else "Play",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}
