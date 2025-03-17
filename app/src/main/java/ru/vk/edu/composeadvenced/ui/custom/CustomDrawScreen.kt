package ru.vk.edu.composeadvenced.ui.custom

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.custom.CustomDrawComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CustomDrawScreen(component: CustomDrawComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Кастомная отрисовка",
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
            SectionTitle(title = "Простой Canvas")
            SimpleCanvasExample()
            
            SectionTitle(title = "Рисование фигур")
            ShapesCanvasExample()
            
            SectionTitle(title = "Интерактивный Canvas")
            InteractiveCanvasExample()
            
            SectionTitle(title = "Сложный пример - аналоговые часы")
            ClockCanvasExample()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SimpleCanvasExample() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            // Рисуем линии
            drawLine(
                color = Color.Gray,
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = 2f
            )
            
            drawLine(
                color = Color.Gray,
                start = Offset(0f, size.height),
                end = Offset(size.width, 0f),
                strokeWidth = 2f
            )
            
            // Рисуем круг в центре
            drawCircle(
                color = Color.Green,
                radius = 30f,
                center = Offset(size.width / 2, size.height / 2)
            )
        }
    }
}

@Composable
private fun ShapesCanvasExample() {
    var rotation by remember { mutableFloatStateOf(0f) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            // Рисуем прямоугольник
            drawRect(
                color = Color.Blue.copy(alpha = 0.5f),
                topLeft = Offset(size.width / 4, size.height / 4),
                size = Size(size.width / 2, size.height / 2)
            )
            
            // Рисуем круг
            drawCircle(
                color = Color.Red.copy(alpha = 0.5f),
                radius = size.minDimension / 4,
                center = Offset(size.width / 2, size.height / 2)
            )
            
            // Рисуем путь (треугольник)
            val trianglePath = Path().apply {
                moveTo(size.width / 2, size.height / 4)
                lineTo(size.width / 4, 3 * size.height / 4)
                lineTo(3 * size.width / 4, 3 * size.height / 4)
                close()
            }
            
            // Вращаем треугольник
            rotate(rotation) {
                drawPath(
                    path = trianglePath,
                    color = Color.Green.copy(alpha = 0.7f),
                    style = Stroke(
                        width = 5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                )
            }
        }
        
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
private fun InteractiveCanvasExample() {
    // Список точек для рисования
    val points = remember { mutableStateListOf<Offset>() }
    var strokeWidth by remember { mutableFloatStateOf(5f) }
    var selectedColor by remember { mutableStateOf(Color.Black) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Холст для рисования
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val newPoint = change.position
                        points.add(newPoint)
                    }
                }
        ) {
            // Рисуем линии между точками
            if (points.size > 1) {
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = selectedColor,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Управление толщиной линии
        Text("Толщина линии: ${strokeWidth.toInt()}px")
        
        Slider(
            value = strokeWidth,
            onValueChange = { strokeWidth = it },
            valueRange = 1f..20f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Выбор цвета
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val colors = listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Magenta)
            
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color)
                        .padding(2.dp)
                        .then(
                            if (color == selectedColor) {
                                Modifier.border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
                            } else {
                                Modifier
                            }
                        )
                        .clickable { selectedColor = color }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Кнопка очистки
        Button(onClick = { points.clear() }) {
            Text("Очистить")
        }
    }
}

@Composable
private fun ClockCanvasExample() {
    var hours by remember { mutableFloatStateOf(10f) }
    var minutes by remember { mutableFloatStateOf(10f) }
    var seconds by remember { mutableFloatStateOf(30f) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2 - 10
            
            // Рисуем циферблат
            drawCircle(
                color = Color.White,
                radius = radius,
                center = center
            )
            
            drawCircle(
                color = Color.Black,
                radius = radius,
                center = center,
                style = Stroke(width = 4f)
            )
            
            // Рисуем метки часов
            for (i in 1..12) {
                val angle = PI / 6 * (i - 3)
                val markerLength = if (i % 3 == 0) 20f else 10f
                val startRadius = radius - markerLength
                val endRadius = radius
                
                val startX = cos(angle).toFloat() * startRadius + center.x
                val startY = sin(angle).toFloat() * startRadius + center.y
                val endX = cos(angle).toFloat() * endRadius + center.x
                val endY = sin(angle).toFloat() * endRadius + center.y
                
                drawLine(
                    color = Color.Black,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (i % 3 == 0) 3f else 1.5f
                )
            }
            
            // Рисуем стрелки
            
            // Часовая стрелка
            val hourAngle = (hours * 30 + minutes * 0.5 - 90) * (PI / 180)
            val hourHandLength = radius * 0.5f
            val hourX = cos(hourAngle).toFloat() * hourHandLength + center.x
            val hourY = sin(hourAngle).toFloat() * hourHandLength + center.y
            
            drawLine(
                color = Color.Black,
                start = center,
                end = Offset(hourX, hourY),
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
            
            // Минутная стрелка
            val minuteAngle = (minutes * 6 - 90) * (PI / 180)
            val minuteHandLength = radius * 0.7f
            val minuteX = cos(minuteAngle).toFloat() * minuteHandLength + center.x
            val minuteY = sin(minuteAngle).toFloat() * minuteHandLength + center.y
            
            drawLine(
                color = Color.Black,
                start = center,
                end = Offset(minuteX, minuteY),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
            
            // Секундная стрелка
            val secondAngle = (seconds * 6 - 90) * (PI / 180)
            val secondHandLength = radius * 0.8f
            val secondX = cos(secondAngle).toFloat() * secondHandLength + center.x
            val secondY = sin(secondAngle).toFloat() * secondHandLength + center.y
            
            drawLine(
                color = Color.Red,
                start = center,
                end = Offset(secondX, secondY),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
            
            // Центральная точка
            drawCircle(
                color = Color.Red,
                radius = 5f,
                center = center
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Управление часами
        Text("Часы: ${hours.toInt()}")
        
        Slider(
            value = hours,
            onValueChange = { hours = it },
            valueRange = 0f..11f,
            steps = 11,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Минуты: ${minutes.toInt()}")
        
        Slider(
            value = minutes,
            onValueChange = { minutes = it },
            valueRange = 0f..59f,
            steps = 59,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Секунды: ${seconds.toInt()}")
        
        Slider(
            value = seconds,
            onValueChange = { seconds = it },
            valueRange = 0f..59f,
            steps = 59,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
} 