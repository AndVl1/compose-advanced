package ru.vk.edu.composeadvenced.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.custom.CustomModifierComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CustomModifierScreen(component: CustomModifierComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Кастомные модификаторы",
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
            SectionTitle(title = "drawBehind - простой пример")
            SimpleDrawBehindExample()
            
            SectionTitle(title = "drawWithContent - наложение эффектов")
            DrawWithContentExample()
            
            SectionTitle(title = "drawWithCache - оптимизация отрисовки")
            DrawWithCacheExample()
            
            SectionTitle(title = "Комбинирование модификаторов")
            CombinedModifiersExample()
            
            SectionTitle(title = "Создание собственного модификатора")
            CustomModifierExample()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SimpleDrawBehindExample() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.White)
                .drawBehind {
                    // Рисуем круг
                    drawCircle(
                        color = Color.Red,
                        radius = 60.dp.toPx(),
                        style = Stroke(width = 5.dp.toPx())
                    )
                    
                    // Рисуем линии
                    for (i in 0 until 12) {
                        val angle = i * 30f
                        val radians = Math.toRadians(angle.toDouble())
                        val startRadius = 60.dp.toPx()
                        val endRadius = 70.dp.toPx()
                        
                        val startX = cos(radians).toFloat() * startRadius
                        val startY = sin(radians).toFloat() * startRadius
                        val endX = cos(radians).toFloat() * endRadius
                        val endY = sin(radians).toFloat() * endRadius
                        
                        drawLine(
                            color = Color.Blue,
                            start = Offset(center.x + startX, center.y + startY),
                            end = Offset(center.x + endX, center.y + endY),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
        ) {
            Text(
                text = "drawBehind",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun DrawWithContentExample() {
    var alpha by remember { mutableFloatStateOf(0.5f) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .drawWithContent {
                    // Сначала рисуем содержимое
                    drawContent()
                    
                    // Затем накладываем полупрозрачный градиент поверх
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Red.copy(alpha = alpha)
                            )
                        ),
                        size = size
                    )
                }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Этот текст находится под градиентом",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Прозрачность градиента: ${(alpha * 100).toInt()}%")
        
        Slider(
            value = alpha,
            onValueChange = { alpha = it },
            valueRange = 0f..1f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun DrawWithCacheExample() {
    var rotation by remember { mutableFloatStateOf(0f) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .drawWithCache {
                    // Кэшируем сложную отрисовку
                    val brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Yellow,
                            Color.Red
                        )
                    )
                    
                    onDrawBehind {
                        // Рисуем солнце с лучами
                        rotate(rotation) {
                            // Круг солнца
                            drawCircle(
                                brush = brush,
                                radius = 50.dp.toPx(),
                                center = center
                            )
                            
                            // Лучи солнца
                            for (i in 0 until 12) {
                                val angle = i * 30f
                                val radians = Math.toRadians(angle.toDouble())
                                val startRadius = 50.dp.toPx()
                                val endRadius = 80.dp.toPx()
                                
                                val startX = cos(radians).toFloat() * startRadius
                                val startY = sin(radians).toFloat() * startRadius
                                val endX = cos(radians).toFloat() * endRadius
                                val endY = sin(radians).toFloat() * endRadius
                                
                                drawLine(
                                    color = Color.Yellow,
                                    start = Offset(center.x + startX, center.y + startY),
                                    end = Offset(center.x + endX, center.y + endY),
                                    strokeWidth = 5.dp.toPx()
                                )
                            }
                        }
                    }
                }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Поворот: ${rotation.toInt()}°")
        
        Slider(
            value = rotation,
            onValueChange = { rotation = it },
            valueRange = 0f..360f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun CombinedModifiersExample() {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                }
                .drawWithContent {
                    // Рисуем фон с узором
                    drawRect(Color.White)
                    
                    // Рисуем узор из точек
                    val dotSize = 4.dp.toPx()
                    val spacing = 20.dp.toPx()
                    
                    for (x in 0..size.width.toInt() step spacing.toInt()) {
                        for (y in 0..size.height.toInt() step spacing.toInt()) {
                            drawCircle(
                                color = Color.LightGray,
                                radius = dotSize / 2,
                                center = Offset(x.toFloat(), y.toFloat())
                            )
                        }
                    }
                    
                    // Рисуем содержимое
                    drawContent()
                    
                    // Рисуем рамку
                    drawRect(
                        color = primaryColor,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Комбинированные модификаторы",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Масштаб: ${(scale * 100).toInt()}%")
        
        Slider(
            value = scale,
            onValueChange = { scale = it },
            valueRange = 0.5f..1.5f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Поворот: ${rotation.toInt()}°")
        
        Slider(
            value = rotation,
            onValueChange = { rotation = it },
            valueRange = 0f..360f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

// Создаем собственный модификатор
fun Modifier.dashedBorder(
    width: Float,
    color: Color,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    dashWidth: Float = 10f,
    gapWidth: Float = 10f
) = this.drawWithCache {
    // Создаем путь на основе формы
    val path = Path()
    val outline = shape.createOutline(size, layoutDirection, this)
    
    // Преобразуем Outline в Path
    if (outline is androidx.compose.ui.graphics.Outline.Generic) {
        path.addPath(outline.path)
    } else if (outline is androidx.compose.ui.graphics.Outline.Rounded) {
        path.addRoundRect(outline.roundRect)
    } else if (outline is androidx.compose.ui.graphics.Outline.Rectangle) {
        path.addRect(outline.rect)
    }
    
    // Создаем пунктирную обводку
    val stroke = Stroke(
        width = width,
        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth, gapWidth),
            phase = 0f
        )
    )
    
    onDrawWithContent {
        // Рисуем содержимое
        drawContent()
        
        // Рисуем пунктирную рамку
        drawPath(
            path = path,
            color = color,
            style = stroke
        )
    }
}

@Composable
private fun CustomModifierExample() {
    var cornerRadius by remember { mutableFloatStateOf(16f) }
    var dashWidth by remember { mutableFloatStateOf(10f) }
    var gapWidth by remember { mutableFloatStateOf(10f) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .dashedBorder(
                    width = 2f,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(cornerRadius.dp),
                    dashWidth = dashWidth,
                    gapWidth = gapWidth
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Кастомный модификатор\nс пунктирной рамкой",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Радиус скругления: ${cornerRadius.toInt()}dp")
        
        Slider(
            value = cornerRadius,
            onValueChange = { cornerRadius = it },
            valueRange = 0f..50f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Длина штриха: ${dashWidth.toInt()}px")
        
        Slider(
            value = dashWidth,
            onValueChange = { dashWidth = it },
            valueRange = 1f..30f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Длина промежутка: ${gapWidth.toInt()}px")
        
        Slider(
            value = gapWidth,
            onValueChange = { gapWidth = it },
            valueRange = 1f..30f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
} 