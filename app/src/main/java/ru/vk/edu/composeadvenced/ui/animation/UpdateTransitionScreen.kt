package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.animation.UpdateTransitionComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle

@Composable
fun UpdateTransitionScreen(component: UpdateTransitionComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "updateTransition",
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
            SimpleTransitionExample()
            
            SectionTitle(title = "Множественные анимации")
            MultiplePropertiesExample()
            
            SectionTitle(title = "Сложный пример")
            AdvancedTransitionExample()
        }
    }
}

enum class BoxState { Small, Large }

@Composable
private fun SimpleTransitionExample() {
    var currentState by remember { mutableStateOf(BoxState.Small) }
    
    val transition = updateTransition(
        targetState = currentState,
        label = "Box Size Transition"
    )
    
    val size by transition.animateDp(
        transitionSpec = { 
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ) 
        },
        label = "size"
    ) { state ->
        when (state) {
            BoxState.Small -> 64.dp
            BoxState.Large -> 128.dp
        }
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                currentState = when (currentState) {
                    BoxState.Small -> BoxState.Large
                    BoxState.Large -> BoxState.Small
                }
            }
        ) {
            Text(
                text = when (currentState) {
                    BoxState.Small -> "Увеличить"
                    BoxState.Large -> "Уменьшить"
                }
            )
        }
    }
}

enum class BoxAnimState { Inactive, Active }

@Composable
private fun MultiplePropertiesExample() {
    var currentState by remember { mutableStateOf(BoxAnimState.Inactive) }
    
    val transition = updateTransition(
        targetState = currentState,
        label = "Box Animation Transition"
    )
    
    val color by transition.animateColor(
        transitionSpec = { tween(durationMillis = 500) },
        label = "color"
    ) { state ->
        when (state) {
            BoxAnimState.Inactive -> MaterialTheme.colorScheme.primary
            BoxAnimState.Active -> MaterialTheme.colorScheme.tertiary
        }
    }
    
    val size by transition.animateDp(
        transitionSpec = { 
            tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            ) 
        },
        label = "size"
    ) { state ->
        when (state) {
            BoxAnimState.Inactive -> 80.dp
            BoxAnimState.Active -> 120.dp
        }
    }
    
    val cornerRadius by transition.animateDp(
        transitionSpec = { tween(durationMillis = 500) },
        label = "corner"
    ) { state ->
        when (state) {
            BoxAnimState.Inactive -> 8.dp
            BoxAnimState.Active -> 40.dp
        }
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(cornerRadius))
                .background(color)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                currentState = when (currentState) {
                    BoxAnimState.Inactive -> BoxAnimState.Active
                    BoxAnimState.Active -> BoxAnimState.Inactive
                }
            }
        ) {
            Text(
                text = when (currentState) {
                    BoxAnimState.Inactive -> "Активировать"
                    BoxAnimState.Active -> "Деактивировать"
                }
            )
        }
    }
}

enum class ComplexState { State1, State2, State3 }

@Composable
private fun AdvancedTransitionExample() {
    var currentState by remember { mutableStateOf(ComplexState.State1) }
    
    val transition = updateTransition(
        targetState = currentState,
        label = "Complex Animation"
    )
    
    val color by transition.animateColor(
        transitionSpec = { tween(durationMillis = 500) },
        label = "complex color"
    ) { state ->
        when (state) {
            ComplexState.State1 -> MaterialTheme.colorScheme.primary
            ComplexState.State2 -> MaterialTheme.colorScheme.secondary
            ComplexState.State3 -> MaterialTheme.colorScheme.tertiary
        }
    }
    
    val size by transition.animateDp(
        transitionSpec = { 
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ) 
        },
        label = "complex size"
    ) { state ->
        when (state) {
            ComplexState.State1 -> 80.dp
            ComplexState.State2 -> 100.dp
            ComplexState.State3 -> 120.dp
        }
    }
    
    val offset by transition.animateDp(
        transitionSpec = { tween(durationMillis = 700) },
        label = "complex offset"
    ) { state ->
        when (state) {
            ComplexState.State1 -> 0.dp
            ComplexState.State2 -> 50.dp
            ComplexState.State3 -> (-50).dp
        }
    }
    
    val rotation by transition.animateFloat(
        transitionSpec = { 
            tween(
                durationMillis = 1000,
                easing = LinearEasing
            ) 
        },
        label = "complex rotation"
    ) { state ->
        when (state) {
            ComplexState.State1 -> 0f
            ComplexState.State2 -> 180f
            ComplexState.State3 -> 360f
        }
    }
    
    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 800) },
        label = "complex scale"
    ) { state ->
        when (state) {
            ComplexState.State1 -> 1f
            ComplexState.State2 -> 1.2f
            ComplexState.State3 -> 0.8f
        }
    }
    
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
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    }
                    .size(size)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { currentState = ComplexState.State1 }) {
                Text("Состояние 1")
            }
            
            Button(onClick = { currentState = ComplexState.State2 }) {
                Text("Состояние 2")
            }
            
            Button(onClick = { currentState = ComplexState.State3 }) {
                Text("Состояние 3")
            }
        }
    }
} 