package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.animation.AnimatedContentComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle

@Composable
fun AnimatedContentScreen(component: AnimatedContentComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "AnimatedContent",
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
            SectionTitle(title = "Простой счетчик")
            CounterAnimatedContentExample()
            
            SectionTitle(title = "Различные анимации")
            DifferentContentAnimationsExample()
            
            SectionTitle(title = "Сложный пример")
            AdvancedAnimatedContentExample()
        }
    }
}

@Composable
private fun CounterAnimatedContentExample() {
    var count by remember { mutableIntStateOf(0) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                // Анимация для чисел: если новое число больше, то оно появляется снизу,
                // если меньше - то сверху
                if (targetState > initialState) {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                    slideOutVertically { height -> -height } + fadeOut()
                } else {
                    slideInVertically { height -> -height } + fadeIn() togetherWith
                    slideOutVertically { height -> height } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            },
            label = "counter"
        ) { targetCount ->
            Text(
                text = "$targetCount",
                style = MaterialTheme.typography.displayLarge
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { count-- }) {
                Text("-")
            }
            
            Button(onClick = { count++ }) {
                Text("+")
            }
        }
    }
}

enum class ContentAnimationType {
    SLIDE_HORIZONTAL, SLIDE_VERTICAL, FADE, COMBINED
}

@Composable
private fun DifferentContentAnimationsExample() {
    var currentContent by remember { mutableIntStateOf(0) }
    var selectedAnimationType by remember { mutableStateOf(ContentAnimationType.SLIDE_HORIZONTAL) }
    
    val contentTransform: ContentTransform = when (selectedAnimationType) {
        ContentAnimationType.SLIDE_HORIZONTAL -> {
            slideInHorizontally { width -> if (currentContent % 2 == 0) width else -width } + fadeIn() togetherWith
            slideOutHorizontally { width -> if (currentContent % 2 == 0) -width else width } + fadeOut()
        }
        ContentAnimationType.SLIDE_VERTICAL -> {
            slideInVertically { height -> if (currentContent % 2 == 0) height else -height } + fadeIn() togetherWith
            slideOutVertically { height -> if (currentContent % 2 == 0) -height else height } + fadeOut()
        }
        ContentAnimationType.FADE -> {
            fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith
            fadeOut(animationSpec = tween(durationMillis = 500))
        }
        ContentAnimationType.COMBINED -> {
            (slideInHorizontally { it } + fadeIn()) togetherWith
            (slideOutHorizontally { it } + fadeOut())
        }
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = currentContent,
                transitionSpec = { contentTransform.using(SizeTransform(clip = false)) },
                label = "different content"
            ) { targetContent ->
                when (targetContent % 3) {
                    0 -> Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    1 -> Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                    2 -> Column(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.tertiary),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Текст",
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
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
                    selected = selectedAnimationType == ContentAnimationType.SLIDE_HORIZONTAL,
                    onClick = { selectedAnimationType = ContentAnimationType.SLIDE_HORIZONTAL }
                )
                Text("Горизонтальное смещение")
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnimationType == ContentAnimationType.SLIDE_VERTICAL,
                    onClick = { selectedAnimationType = ContentAnimationType.SLIDE_VERTICAL }
                )
                Text("Вертикальное смещение")
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnimationType == ContentAnimationType.FADE,
                    onClick = { selectedAnimationType = ContentAnimationType.FADE }
                )
                Text("Затухание")
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnimationType == ContentAnimationType.COMBINED,
                    onClick = { selectedAnimationType = ContentAnimationType.COMBINED }
                )
                Text("Комбинированная")
            }
        }
        
        Button(onClick = { currentContent++ }) {
            Text("Следующий контент")
        }
    }
}

@Composable
private fun AdvancedAnimatedContentExample() {
    var currentPage by remember { mutableIntStateOf(0) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                slideInHorizontally { width -> direction * width } + fadeIn(
                    animationSpec = tween(durationMillis = 300)
                ) togetherWith slideOutHorizontally { width -> -direction * width } + fadeOut(
                    animationSpec = tween(durationMillis = 300)
                ) using SizeTransform(clip = false)
            },
            label = "advanced content"
        ) { page ->
            when (page) {
                0 -> PageOne(onNextClick = { currentPage = 1 })
                1 -> PageTwo(
                    onPrevClick = { currentPage = 0 },
                    onNextClick = { currentPage = 2 }
                )
                2 -> PageThree(onPrevClick = { currentPage = 1 })
            }
        }
    }
}

@Composable
private fun PageOne(onNextClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Страница 1",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "1",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onNextClick) {
            Text("Далее")
        }
    }
}

@Composable
private fun PageTwo(onPrevClick: () -> Unit, onNextClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Страница 2",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "2",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = onPrevClick) {
                Text("Назад")
            }
            
            Button(onClick = onNextClick) {
                Text("Далее")
            }
        }
    }
}

@Composable
private fun PageThree(onPrevClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Страница 3",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.tertiary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "3",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onPrevClick) {
            Text("Назад")
        }
    }
} 