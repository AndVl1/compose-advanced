package ru.vk.edu.composeadvenced.ui.custom

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.custom.ComplexCustomComponentComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ComplexCustomComponentScreen(component: ComplexCustomComponentComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Сложный кастомный компонент",
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
            SectionTitle(title = "Круговой прогресс-индикатор")
            CircularProgressExample()
            
            SectionTitle(title = "Кастомный слайдер")
            CustomSliderExample()
            
            SectionTitle(title = "Кастомный график")
            CustomChartExample()
            
            SectionTitle(title = "Кастомный рейтинг")
            CustomRatingExample()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    strokeWidth: Float = 12f
) {
    Canvas(
        modifier = modifier
    ) {
        // Рисуем фоновый круг
        drawArc(
            color = backgroundColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        
        // Рисуем прогресс
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = progress * 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun CircularProgressExample() {
    var progress by remember { mutableFloatStateOf(0.75f) }
    var animated by remember { mutableStateOf(false) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "progress"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = if (animated) animatedProgress.value else progress,
                progressColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 16f
            )
            
            Text(
                text = "${((if (animated) animatedProgress.value else progress) * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Прогресс: ${(progress * 100).toInt()}%")
        
        Slider(
            value = progress,
            onValueChange = { progress = it },
            valueRange = 0f..1f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Анимация:")
            Switch(
                checked = animated,
                onCheckedChange = { animated = it }
            )
        }
    }
}

@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary
) {
    val density = LocalDensity.current
    var sliderWidth by remember { mutableStateOf(0) }
    
    // Используем LaunchedEffect для инициализации, чтобы избежать прыжка от 0 до 50
    LaunchedEffect(Unit) {
        // Ничего не делаем, просто предотвращаем прыжок
    }
    
    Box(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .onSizeChanged { sliderWidth = it.width }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val newValue = (offset.x / sliderWidth.toFloat()).coerceIn(0f, 1f)
                        onValueChange(newValue)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val newValue = (change.position.x / sliderWidth.toFloat()).coerceIn(0f, 1f)
                        onValueChange(newValue)
                    }
                )
            }
            .clickable { 
                // Обработка клика для мгновенного перемещения не нужна,
                // так как у нас уже есть detectDragGestures с onDragStart
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Фоновый трек
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(trackColor, RoundedCornerShape(2.dp))
        )
        
        // Прогресс
        Box(
            modifier = Modifier
                .fillMaxWidth(value.coerceIn(0f, 1f))
                .height(4.dp)
                .background(progressColor, RoundedCornerShape(2.dp))
        )
        
        // Ползунок - исправляем позиционирование
        // Используем абсолютное позиционирование с учетом размера ползунка
        Box(
            modifier = Modifier
                .offset {
                    // Вычисляем смещение так, чтобы центр ползунка был на конце прогресса
                    val thumbSizeInPx = with(density) { 24.dp.roundToPx() }
                    val thumbHalfSize = thumbSizeInPx / 2
                    val xOffset = (sliderWidth * value).toInt() - thumbHalfSize
                    IntOffset(xOffset, 0)
                }
                .size(24.dp)
                .shadow(4.dp, CircleShape)
                .background(thumbColor, CircleShape)
        )
    }
}

@Composable
private fun CustomSliderExample() {
    var value by remember { mutableFloatStateOf(0.5f) }
    var min by remember { mutableFloatStateOf(0f) }
    var max by remember { mutableFloatStateOf(100f) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Значение: ${(min + (max - min) * value).toInt()}",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        CustomSlider(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..1f,
            thumbColor = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            progressColor = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Минимум: ${min.toInt()}")
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { min = (min - 10).coerceAtLeast(0f) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Уменьшить")
                    }
                    
                    IconButton(onClick = { min = (min + 10).coerceAtMost(max - 10) }) {
                        Icon(Icons.Default.Add, contentDescription = "Увеличить")
                    }
                }
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Максимум: ${max.toInt()}")
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { max = (max - 10).coerceAtLeast(min + 10) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Уменьшить")
                    }
                    
                    IconButton(onClick = { max += 10 }) {
                        Icon(Icons.Default.Add, contentDescription = "Увеличить")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    showPoints: Boolean = true,
    pointColor: Color = MaterialTheme.colorScheme.primary,
    pointSize: Float = 8f
) {
    if (values.isEmpty()) return
    
    Canvas(
        modifier = modifier
    ) {
        val maxValue = values.maxOrNull() ?: 1f
        val minValue = values.minOrNull() ?: 0f
        val range = (maxValue - minValue).coerceAtLeast(0.01f)
        
        val xStep = size.width / (values.size - 1)
        val yStep = size.height / range
        
        // Создаем путь для линии
        val linePath = Path().apply {
            values.forEachIndexed { index, value ->
                val x = index * xStep
                val y = size.height - (value - minValue) * yStep
                
                if (index == 0) {
                    moveTo(x, y)
                } else {
                    lineTo(x, y)
                }
            }
        }
        
        // Создаем путь для заливки
        val fillPath = Path().apply {
            // Начинаем с нижнего левого угла
            moveTo(0f, size.height)
            
            // Добавляем точки графика
            values.forEachIndexed { index, value ->
                val x = index * xStep
                val y = size.height - (value - minValue) * yStep
                lineTo(x, y)
            }
            
            // Завершаем в нижнем правом углу
            lineTo(size.width, size.height)
            close()
        }
        
        // Рисуем заливку
        drawPath(
            path = fillPath,
            color = fillColor
        )
        
        // Рисуем линию
        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx())
        )
        
        // Рисуем точки
        if (showPoints) {
            values.forEachIndexed { index, value ->
                val x = index * xStep
                val y = size.height - (value - minValue) * yStep
                
                drawCircle(
                    color = pointColor,
                    radius = pointSize,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
private fun CustomChartExample() {
    var pointCount by remember { mutableIntStateOf(7) }
    var valuesRandomKey by remember { mutableIntStateOf(0) }
    var showPoints by remember { mutableStateOf(true) }
    val density = LocalDensity.current

    // Генерируем случайные значения для графика
    val values = remember(pointCount, valuesRandomKey) {
        List(pointCount) { index ->
            val base = sin(index * 0.5) * 50 + 50
            (base.toFloat() + Random.nextFloat() * 20).coerceIn(10f, 100f)
        }
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp)
        ) {
            CustomChart(
                values = values,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                lineColor = MaterialTheme.colorScheme.primary,
                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                showPoints = showPoints,
                pointColor = MaterialTheme.colorScheme.primary,
                pointSize = with(density) { 6.dp.toPx() }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Количество точек: $pointCount")
        
        Slider(
            value = pointCount.toFloat(),
            onValueChange = { pointCount = it.toInt() },
            valueRange = 3f..15f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Показывать точки:")
            Switch(
                checked = showPoints,
                onCheckedChange = { showPoints = it }
            )
        }
        
        Button(onClick = {
            // Триггерим пересоздание массива, изменяя значение, передаваемое в ключ remember
            valuesRandomKey = Random.nextInt()
        }) {
            Text("Обновить данные")
        }
    }
}

@Composable
fun CustomRating(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxValue: Int = 5,
    activeColor: Color = Color(0xFFFFD700),
    inactiveColor: Color = Color.Gray.copy(alpha = 0.5f),
    starSize: Int = 36
) {
    // Используем состояние для отслеживания текущего рейтинга во время свайпа
    var currentDragRating by remember { mutableIntStateOf(value) }
    // Флаг, указывающий, находимся ли мы в процессе свайпа
    var isDragging by remember { mutableStateOf(false) }
    // Сохраняем ширину каждой звезды с учетом отступов
    var starWidthWithSpacing by remember { mutableFloatStateOf(0f) }
    // Сохраняем общую ширину компонента
    var totalWidth by remember { mutableFloatStateOf(0f) }
    
    // Сбрасываем currentDragRating при изменении value извне
    LaunchedEffect(value) {
        if (!isDragging) {
            currentDragRating = value
        }
    }
    
    Box(
        modifier = modifier
            .onSizeChanged { size ->
                totalWidth = size.width.toFloat()
                // Вычисляем ширину одной звезды с учетом отступов
                starWidthWithSpacing = totalWidth / maxValue
            }
            .pointerInput(maxValue) { // Добавляем maxValue как ключ, чтобы обновлять обработчик при изменении
                // Обработка свайпа
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        // Сразу обновляем рейтинг при начале свайпа
                        val newRating = calculateRatingFromPosition(offset.x, starWidthWithSpacing, maxValue)
                        currentDragRating = newRating
                    },
                    onDragEnd = {
                        isDragging = false
                        // Применяем изменения только в конце свайпа
                        onValueChange(currentDragRating)
                    },
                    onDragCancel = {
                        isDragging = false
                        // Возвращаем исходное значение при отмене свайпа
                        currentDragRating = value
                    },
                    onHorizontalDrag = { change, _ ->
                        change.consume()
                        
                        // Вычисляем рейтинг на основе позиции
                        val newRating = calculateRatingFromPosition(change.position.x, starWidthWithSpacing, maxValue)
                        
                        // Обновляем текущий рейтинг для отображения
                        currentDragRating = newRating
                    }
                )
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 1..maxValue) {
                // Используем currentDragRating во время свайпа, иначе value
                val displayRating = if (isDragging) currentDragRating else value
                
                Icon(
                    imageVector = if (i <= displayRating) Icons.Default.Star else Icons.Rounded.Star,
                    contentDescription = "Звезда $i",
                    tint = if (i <= displayRating) activeColor else inactiveColor,
                    modifier = Modifier
                        .size(starSize.dp)
                        .clickable { onValueChange(i) }
                )
            }
        }
    }
}

// Выносим логику расчета рейтинга в отдельную функцию
private fun calculateRatingFromPosition(position: Float, starWidth: Float, maxValue: Int): Int {
    // Если позиция отрицательная, возвращаем минимальный рейтинг
    if (position <= 0f) return 1
    
    // Вычисляем рейтинг на основе позиции
    val calculatedRating = ((position / starWidth) + 0.5f).toInt()
    
    // Ограничиваем рейтинг в пределах допустимых значений
    return calculatedRating.coerceIn(1, maxValue)
}

@Composable
private fun CustomRatingExample() {
    var rating by remember { mutableIntStateOf(3) }
    var maxRating by remember { mutableIntStateOf(5) }
    var starSize by remember { mutableIntStateOf(36) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Рейтинг: $rating из $maxRating",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        CustomRating(
            value = rating,
            onValueChange = { rating = it },
            maxValue = maxRating,
            activeColor = Color(0xFFFFD700),
            inactiveColor = Color.Gray.copy(alpha = 0.5f),
            starSize = starSize,
            modifier = Modifier.padding(horizontal = 8.dp) // Добавляем отступы для лучшего восприятия
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Выберите рейтинг тапом по звезде или свайпом",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Максимальный рейтинг: $maxRating")
        
        Slider(
            value = maxRating.toFloat(),
            onValueChange = { 
                maxRating = it.toInt()
                rating = rating.coerceAtMost(maxRating)
            },
            valueRange = 3f..10f,
            steps = 7,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Размер звезды: ${starSize}dp")
        
        Slider(
            value = starSize.toFloat(),
            onValueChange = { starSize = it.toInt() },
            valueRange = 24f..48f,
            steps = 8,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
} 
