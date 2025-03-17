package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.vk.edu.composeadvenced.screens.animation.AnimateAsStateComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.keyframesWithSpline
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutLinearInEasing

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
            
            SectionTitle(title = "Сравнение анимаций")
            BallsAnimationExample()
            
            SectionTitle(title = "Keyframes и KeyframesWithSpline")
            KeyframesAnimationExample()
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

@Composable
private fun BallsAnimationExample() {
    // Состояние, определяющее положение шариков (false - слева, true - справа)
    var isRight by remember { mutableStateOf(false) }
    
    // Определяем разные спецификации анимации
    val linearSpec = tween<Float>(
        durationMillis = 1500,
        easing = LinearEasing
    )
    
    val fastOutSlowInSpec = tween<Float>(
        durationMillis = 1500,
        easing = FastOutSlowInEasing
    )

    val linearOutSlowInSpec = tween<Float>(
        durationMillis = 1500,
        easing = LinearOutSlowInEasing
    )
    
    val springSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    val bounceSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioHighBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    // Анимируем позиции шариков с разными спецификациями
    val linearPosition by animateFloatAsState(
        targetValue = if (isRight) 1f else 0f,
        animationSpec = linearSpec,
        label = "linearBall"
    )
    
    val fastOutSlowInPosition by animateFloatAsState(
        targetValue = if (isRight) 1f else 0f,
        animationSpec = fastOutSlowInSpec,
        label = "fastOutSlowInBall"
    )

    val linearOutSlowInPosition by animateFloatAsState(
        targetValue = if (isRight) 1f else 0f,
        animationSpec = linearOutSlowInSpec,
        label = "linearOutSlowInBall"
    )
    
    val springPosition by animateFloatAsState(
        targetValue = if (isRight) 1f else 0f,
        animationSpec = springSpec,
        label = "springBall"
    )
    
    val bouncePosition by animateFloatAsState(
        targetValue = if (isRight) 1f else 0f,
        animationSpec = bounceSpec,
        label = "bounceBall"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Контейнер для шариков
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Увеличиваем высоту для лучшего размещения шариков
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // Линия для визуального ориентира
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            )
            
            // Шарик с линейной анимацией
            BallWithLabel(
                position = linearPosition,
                color = Color.Red,
                label = "Linear",
                verticalOffset = (-150).dp
            )
            
            // Шарик с FastOutSlowIn анимацией
            BallWithLabel(
                position = fastOutSlowInPosition,
                color = Color.Blue,
                label = "FastOutSlowIn",
                verticalOffset = (-75).dp
            )

            // Шарик с LinearOutSlowIn анимацией
            BallWithLabel(
                position = linearOutSlowInPosition,
                color = Color(0xFF3F51B5), // Используем другой оттенок синего
                label = "LinearOutSlowIn",
                verticalOffset = 0.dp
            )
            
            // Шарик с пружинной анимацией
            BallWithLabel(
                position = springPosition,
                color = Color.Green,
                label = "Spring",
                verticalOffset = 75.dp
            )
            
            // Шарик с отскакивающей анимацией
            BallWithLabel(
                position = bouncePosition,
                color = Color.Magenta,
                label = "Bounce",
                verticalOffset = 150.dp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Кнопка для переключения положения шариков
        Button(
            onClick = { isRight = !isRight },
            modifier = Modifier.width(200.dp)
        ) {
            Text(if (isRight) "Вернуть влево" else "Переместить вправо")
        }
    }
}

@Composable
private fun BallWithLabel(
    position: Float,
    color: Color,
    label: String,
    verticalOffset: Dp
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .offset(
                    x = ((LocalConfiguration.current.screenWidthDp.dp - 100.dp) * position) -
                        (LocalConfiguration.current.screenWidthDp.dp - 100.dp) / 2,
                    y = verticalOffset
                )
        ) {
            // Шарик
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            
            // Подпись
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalAnimationSpecApi::class)
@Composable
private fun KeyframesAnimationExample() {
    var isAnimating by remember { mutableStateOf(false) }
    
    // Определяем спецификации анимации с keyframes
    val keyframesSpec = keyframes<Float> {
        durationMillis = 2000
        0f at 0    // начальное положение
        0.2f at 200  // быстрый скачок до 20%
        0.4f at 400  // продолжение движения
        0.4f at 800  // пауза на 400мс
        0.8f at 1200 // быстрое движение до 80%
        0.9f at 1500 // замедление
        1f at 2000  // плавное завершение
    }
    
    // Определяем спецификации анимации с keyframesWithSpline
    val splineSpec = keyframesWithSpline<Float> {
        durationMillis = 2000
        0f at 0 using FastOutSlowInEasing
        0.3f at 300 using LinearEasing
        0.7f at 1000 using LinearOutSlowInEasing
        1f at 2000 using FastOutLinearInEasing
    }
    
    // Анимируем позиции шариков с разными спецификациями
    val keyframesPosition by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0f,
        animationSpec = keyframesSpec,
        label = "keyframesBall"
    )
    
    val splinePosition by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0f,
        animationSpec = splineSpec,
        label = "splineBall"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Контейнер для шариков
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // Линия для визуального ориентира
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            )
            
            // Шарик с keyframes анимацией
            BallWithLabel(
                position = keyframesPosition,
                color = Color.Cyan,
                label = "Keyframes",
                verticalOffset = (-40).dp
            )
            
            // Шарик с keyframesWithSpline анимацией
            BallWithLabel(
                position = splinePosition,
                color = Color.Yellow,
                label = "KeyframesWithSpline",
                verticalOffset = 40.dp
            )
            
            // Маркеры для keyframes
            if (!isAnimating) {
                // Маркеры для keyframes
                KeyframeMarker(position = 0.2f, label = "0.2f at 200ms")
                KeyframeMarker(position = 0.4f, label = "0.4f at 400-800ms")
                KeyframeMarker(position = 0.8f, label = "0.8f at 1200ms")
                KeyframeMarker(position = 0.9f, label = "0.9f at 1500ms")
                
                // Маркеры для spline
                KeyframeMarker(position = 0.3f, label = "0.3f at 300ms", verticalOffset = 70.dp)
                KeyframeMarker(position = 0.7f, label = "0.7f at 1000ms", verticalOffset = 70.dp)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Описание примера
        Text(
            text = "Keyframes позволяют точно контролировать положение объекта в определенные моменты времени. " +
                  "KeyframesWithSpline добавляет возможность указать тип интерполяции между ключевыми кадрами.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Кнопка для запуска анимации
        Button(
            onClick = { isAnimating = !isAnimating },
            modifier = Modifier.width(200.dp)
        ) {
            Text(if (isAnimating) "Сбросить" else "Запустить анимацию")
        }
    }
}

@Composable
private fun KeyframeMarker(
    position: Float,
    label: String,
    verticalOffset: Dp = 0.dp
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .offset(
                    x = ((LocalConfiguration.current.screenWidthDp.dp - 100.dp) * position) -
                        (LocalConfiguration.current.screenWidthDp.dp - 100.dp) / 2,
                    y = verticalOffset
                )
        ) {
            // Маркер
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            )
            
            // Подпись
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(80.dp)
            )
        }
    }
} 
