package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.vk.edu.composeadvenced.screens.animation.AnimatableComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle

@Composable
fun AnimatableScreen(component: AnimatableComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Animatable",
                showBackButton = true,
                onBackClick = { component.onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SectionTitle(title = "Простой пример")
            SimpleAnimatableExample()
            
            SectionTitle(title = "Анимация цвета")
            ColorAnimatableExample()
            
            SectionTitle(title = "Сложный пример")
            AdvancedAnimatableExample()
        }
    }
}

@Composable
private fun SimpleAnimatableExample() {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    
    var direction by remember { mutableStateOf(1f) }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = offsetX.value.dp.roundToPx(),
                        y = 0
                    )
                }
//                .offset(x = offsetX.value.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    scope.launch {
                        direction *= -1
                        offsetX.animateTo(
                            targetValue = 100f * direction,
                            animationSpec = tween(
                                durationMillis = 1000,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
            ) {
                Text("Анимировать")
            }
        }
    }
}

@Composable
private fun ColorAnimatableExample() {
    val scope = rememberCoroutineScope()
    val color = remember { Animatable(Color.Red) }
    var isRed by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(color.value)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                scope.launch {
                    isRed = !isRed
                    color.animateTo(
                        targetValue = if (isRed) Color.Red else Color.Blue,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
            }
        ) {
            Text("Изменить цвет")
        }
    }
}

@Composable
private fun AdvancedAnimatableExample() {
    val scale = remember { Animatable(1f) }
    val offsetY = remember { Animatable(0f) }
    
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    
    LaunchedEffect(key1 = true) {
        offsetY.animateTo(
            targetValue = 50f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = offsetY.value.dp.roundToPx(),
                    )
                }
//                .offset(y = offsetY.value.dp)
                .scale(scale.value)
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiary)
        )
    }
} 
