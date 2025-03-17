package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.animation.AnimatedVisibilityComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle

@Composable
fun AnimatedVisibilityScreen(component: AnimatedVisibilityComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "AnimatedVisibility",
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
            SimpleAnimatedVisibilityExample()
            
            SectionTitle(title = "Различные анимации")
            DifferentAnimationsExample()
            
            SectionTitle(title = "Сложный пример")
            AdvancedAnimatedVisibilityExample()
        }
    }
}

@Composable
private fun SimpleAnimatedVisibilityExample() {
    var isVisible by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = isVisible,
            modifier = Modifier,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { isVisible = !isVisible }) {
            Text(if (isVisible) "Скрыть" else "Показать")
        }
    }
}

enum class AnimationType {
    FADE, SLIDE, SCALE, EXPAND
}

@Composable
private fun DifferentAnimationsExample() {
    var isVisible by remember { mutableStateOf(true) }
    var selectedAnimationType by remember { mutableStateOf(AnimationType.FADE) }
    
    val enterTransition: EnterTransition = when (selectedAnimationType) {
        AnimationType.FADE -> fadeIn(animationSpec = tween(durationMillis = 500))
        AnimationType.SLIDE -> slideInHorizontally(animationSpec = tween(durationMillis = 500))
        AnimationType.SCALE -> scaleIn(animationSpec = tween(durationMillis = 500))
        AnimationType.EXPAND -> expandIn(animationSpec = tween(durationMillis = 500), expandFrom = Alignment.Center)
    }
    
    val exitTransition: ExitTransition = when (selectedAnimationType) {
        AnimationType.FADE -> fadeOut(animationSpec = tween(durationMillis = 500))
        AnimationType.SLIDE -> slideOutHorizontally(animationSpec = tween(durationMillis = 500))
        AnimationType.SCALE -> scaleOut(animationSpec = tween(durationMillis = 500))
        AnimationType.EXPAND -> shrinkOut(animationSpec = tween(durationMillis = 500), shrinkTowards = Alignment.Center)
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Анимация
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            this@Column.AnimatedVisibility(
                visible = isVisible,
                modifier = Modifier,
                enter = enterTransition,
                exit = exitTransition
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }
        }
        
        // Выбор типа анимации
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Тип анимации:",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnimationType == AnimationType.FADE,
                    onClick = { selectedAnimationType = AnimationType.FADE }
                )
                Text("Fade")
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnimationType == AnimationType.SLIDE,
                    onClick = { selectedAnimationType = AnimationType.SLIDE }
                )
                Text("Slide")
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnimationType == AnimationType.SCALE,
                    onClick = { selectedAnimationType = AnimationType.SCALE }
                )
                Text("Scale")
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnimationType == AnimationType.EXPAND,
                    onClick = { selectedAnimationType = AnimationType.EXPAND }
                )
                Text("Expand")
            }
        }
        
        Button(onClick = { isVisible = !isVisible }) {
            Text(if (isVisible) "Скрыть" else "Показать")
        }
    }
}

@Composable
private fun AdvancedAnimatedVisibilityExample() {
    var showCard by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showCard = !showCard }) {
            Text(if (showCard) "Скрыть карточку" else "Показать карточку")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AnimatedVisibility(
            visible = showCard,
            modifier = Modifier,
            enter = fadeIn(animationSpec = tween(300)) + 
                    expandIn(
                        animationSpec = tween(500),
                        expandFrom = Alignment.TopCenter,
                        initialSize = { IntSize(it.width, 0) }
                    ),
            exit = fadeOut(animationSpec = tween(300)) + 
                   shrinkOut(
                       animationSpec = tween(500),
                       shrinkTowards = Alignment.BottomCenter,
                       targetSize = { IntSize(it.width, 0) }
                   )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Заголовок карточки",
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Это содержимое карточки с анимацией появления и исчезновения. " +
                               "Анимация комбинирует fade и expand/shrink эффекты для более " +
                               "плавного и интересного визуального перехода.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { showCard = false }
                        ) {
                            Text("Закрыть")
                        }
                    }
                }
            }
        }
    }
} 