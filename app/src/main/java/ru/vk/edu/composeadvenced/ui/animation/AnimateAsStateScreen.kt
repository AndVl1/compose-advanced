package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.animation.AnimateAsStateComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle

@Composable
fun AnimateAsStateScreen(component: AnimateAsStateComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "animate*asState",
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
            SectionTitle(title = "animateDpAsState")
            DpAnimationExample()
            
            SectionTitle(title = "animateColorAsState")
            ColorAnimationExample()
            
            SectionTitle(title = "animateFloatAsState")
            FloatAnimationExample()
            
            SectionTitle(title = "Комбинированный пример")
            CombinedAnimationExample()
        }
    }
}

@Composable
private fun DpAnimationExample() {
    var expanded by remember { mutableStateOf(false) }
    val width by animateDpAsState(
        targetValue = if (expanded) 200.dp else 100.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "width"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Сжать" else "Расширить")
        }
    }
}

@Composable
private fun ColorAnimationExample() {
    var isColorChanged by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (isColorChanged) Color.Green else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 1000),
        label = "color"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(color)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Изменить цвет")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isColorChanged,
                onCheckedChange = { isColorChanged = it }
            )
        }
    }
}

@Composable
private fun FloatAnimationExample() {
    var rotation by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearOutSlowInEasing
        ),
        label = "rotation"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                //  .rotate(animatedRotation) // better use graphicsLayer
                .graphicsLayer {
                    rotationZ = animatedRotation
                }
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Поворот",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Slider(
            value = rotation,
            onValueChange = { rotation = it },
            valueRange = 0f..360f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Угол поворота: ${rotation.toInt()}°")
    }
}

@Composable
private fun CombinedAnimationExample() {
    var isActive by remember { mutableStateOf(false) }
    
    // Анимируем несколько свойств одновременно
    val color by animateColorAsState(
        targetValue = if (isActive) Color.Magenta else MaterialTheme.colorScheme.tertiary,
        animationSpec = tween(durationMillis = 500),
        label = "combined color"
    )
    
    val size by animateDpAsState(
        targetValue = if (isActive) 120.dp else 80.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "combined size"
    )
    
    val offset by animateDpAsState(
        targetValue = if (isActive) 50.dp else 0.dp,
        animationSpec = tween(durationMillis = 700),
        label = "combined offset"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .offset(y = offset)
                    .size(size)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        
        Button(onClick = { isActive = !isActive }) {
            Text(if (isActive) "Сбросить" else "Активировать")
        }
    }
} 