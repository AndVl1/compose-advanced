package ru.vk.edu.composeadvenced.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.custom.CustomLayoutComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun CustomLayoutScreen(component: CustomLayoutComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Кастомные лейауты",
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
            SectionTitle(title = "Простой кастомный модификатор layout")
            SimpleCustomLayoutModifierExample()
            
            SectionTitle(title = "Кастомный лейаут - FlowRow")
            FlowRowExample()
            
            SectionTitle(title = "Кастомный лейаут - CircularLayout")
            CircularLayoutExample()
            
            SectionTitle(title = "Сложный пример - StaggeredGrid")
            StaggeredGridExample()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Кастомный модификатор layout
fun Modifier.customPadding(
    start: Float = 0f,
    top: Float = 0f,
    end: Float = 0f,
    bottom: Float = 0f
) = layout { measurable, constraints ->
    // Измеряем содержимое с учетом ограничений
    val placeable = measurable.measure(constraints)
    
    // Вычисляем размеры с учетом отступов
    val width = placeable.width + (start + end).toInt()
    val height = placeable.height + (top + bottom).toInt()
    
    // Размещаем содержимое с учетом отступов
    layout(width, height) {
        placeable.placeRelative(start.toInt(), top.toInt())
    }
}

@Composable
private fun SimpleCustomLayoutModifierExample() {
    var startPadding by remember { mutableFloatStateOf(16f) }
    var topPadding by remember { mutableFloatStateOf(16f) }
    var endPadding by remember { mutableFloatStateOf(16f) }
    var bottomPadding by remember { mutableFloatStateOf(16f) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .customPadding(
                        start = startPadding,
                        top = topPadding,
                        end = endPadding,
                        bottom = bottomPadding
                    )
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = "Кастомные отступы",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Отступ слева: ${startPadding.toInt()}px")
        
        Slider(
            value = startPadding,
            onValueChange = { startPadding = it },
            valueRange = 0f..50f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Отступ сверху: ${topPadding.toInt()}px")
        
        Slider(
            value = topPadding,
            onValueChange = { topPadding = it },
            valueRange = 0f..50f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Отступ справа: ${endPadding.toInt()}px")
        
        Slider(
            value = endPadding,
            onValueChange = { endPadding = it },
            valueRange = 0f..50f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Отступ снизу: ${bottomPadding.toInt()}px")
        
        Slider(
            value = bottomPadding,
            onValueChange = { bottomPadding = it },
            valueRange = 0f..50f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

// Кастомный лейаут FlowRow
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalGap: Int = 0,
    verticalGap: Int = 0,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // Список размещаемых элементов
        val placeables = measurables.map { it.measure(Constraints()) }
        
        // Максимальная ширина строки
        val maxWidth = constraints.maxWidth
        
        // Текущая позиция
        var xPosition = 0
        var yPosition = 0
        var rowHeight = 0
        
        // Вычисляем позиции для каждого элемента
        val positions = placeables.map { placeable ->
            // Если элемент не помещается в текущую строку, переходим на новую
            if (xPosition + placeable.width > maxWidth) {
                xPosition = 0
                yPosition += rowHeight + verticalGap
                rowHeight = 0
            }
            
            // Запоминаем позицию элемента
            val position = Pair(xPosition, yPosition)
            
            // Обновляем текущую позицию
            xPosition += placeable.width + horizontalGap
            rowHeight = maxOf(rowHeight, placeable.height)
            
            position
        }
        
        // Вычисляем общую высоту
        val height = yPosition + rowHeight
        
        // Размещаем элементы
        layout(maxWidth, height) {
            placeables.forEachIndexed { index, placeable ->
                val (x, y) = positions[index]
                placeable.placeRelative(x, y)
            }
        }
    }
}

@Composable
private fun FlowRowExample() {
    var itemCount by remember { mutableIntStateOf(10) }
    var horizontalGap by remember { mutableIntStateOf(8) }
    var verticalGap by remember { mutableIntStateOf(8) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalGap = horizontalGap,
            verticalGap = verticalGap
        ) {
            repeat(itemCount) { index ->
                Box(
                    modifier = Modifier
                        .size(30.dp * ((index % 3) + 1))
                        .background(
                            color = when (index % 5) {
                                0 -> Color.Red
                                1 -> Color.Green
                                2 -> Color.Blue
                                3 -> Color.Yellow
                                else -> Color.Magenta
                            },
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = Color.White
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Количество элементов: $itemCount")
        
        Slider(
            value = itemCount.toFloat(),
            onValueChange = { itemCount = it.toInt() },
            valueRange = 1f..20f,
            steps = 19,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Горизонтальный отступ: ${horizontalGap}dp")
        
        Slider(
            value = horizontalGap.toFloat(),
            onValueChange = { horizontalGap = it.toInt() },
            valueRange = 0f..24f,
            steps = 8,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Вертикальный отступ: ${verticalGap}dp")
        
        Slider(
            value = verticalGap.toFloat(),
            onValueChange = { verticalGap = it.toInt() },
            valueRange = 0f..24f,
            steps = 8,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

// Кастомный лейаут CircularLayout
@Composable
fun CircularLayout(
    modifier: Modifier = Modifier,
    radius: Float,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // Список размещаемых элементов
        val placeables = measurables.map { it.measure(Constraints()) }
        
        // Вычисляем размер контейнера
        val diameter = (radius * 2).toInt()
        val width = minOf(constraints.maxWidth, diameter)
        val height = minOf(constraints.maxHeight, diameter)
        
        // Центр окружности
        val centerX = width / 2
        val centerY = height / 2
        
        // Размещаем элементы
        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                // Вычисляем угол для текущего элемента
                val angle = 2 * Math.PI * index / placeables.size
                
                // Вычисляем позицию элемента на окружности
                val x = centerX + (radius * cos(angle)).toInt() - placeable.width / 2
                val y = centerY + (radius * sin(angle)).toInt() - placeable.height / 2
                
                // Размещаем элемент
                placeable.placeRelative(x.toInt(), y.toInt())
            }
        }
    }
}

@Composable
private fun CircularLayoutExample() {
    var radius by remember { mutableFloatStateOf(120f) }
    var itemCount by remember { mutableIntStateOf(8) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularLayout(
                radius = radius,
                modifier = Modifier.fillMaxSize()
            ) {
                repeat(itemCount) { index ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                color = when (index % 5) {
                                    0 -> Color.Red
                                    1 -> Color.Green
                                    2 -> Color.Blue
                                    3 -> Color.Yellow
                                    else -> Color.Magenta
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Радиус: ${radius.toInt()}px")
        
        Slider(
            value = radius,
            onValueChange = { radius = it },
            valueRange = 50f..150f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Количество элементов: $itemCount")
        
        Slider(
            value = itemCount.toFloat(),
            onValueChange = { itemCount = it.toInt() },
            valueRange = 3f..12f,
            steps = 9,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

// Кастомный лейаут StaggeredGrid
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    verticalGap: Int = 0,
    horizontalGap: Int = 0,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // Проверяем, что у нас есть хотя бы одна колонка
        check(columns > 0) { "Columns must be greater than 0" }
        
        // Вычисляем ширину колонки с учетом отступов
        val columnWidth = (constraints.maxWidth - (columns - 1) * horizontalGap) / columns
        
        // Создаем ограничения для дочерних элементов
        val childConstraints = Constraints(
            maxWidth = columnWidth,
            minWidth = columnWidth
        )
        
        // Измеряем дочерние элементы
        val placeables = measurables.map { it.measure(childConstraints) }
        
        // Отслеживаем высоту каждой колонки
        val columnHeights = IntArray(columns) { 0 }
        
        // Распределяем элементы по колонкам
        val positions = placeables.map { placeable ->
            // Находим колонку с наименьшей высотой
            val column = columnHeights.indices.minByOrNull { columnHeights[it] } ?: 0
            
            // Вычисляем позицию элемента
            val x = column * (columnWidth + horizontalGap)
            val y = columnHeights[column]
            
            // Обновляем высоту колонки
            columnHeights[column] += placeable.height + verticalGap
            
            // Возвращаем позицию
            Pair(x, y)
        }
        
        // Вычисляем общую высоту
        val height = columnHeights.maxOrNull()?.let { it - verticalGap } ?: 0
        
        // Размещаем элементы
        layout(constraints.maxWidth, height) {
            placeables.forEachIndexed { index, placeable ->
                val (x, y) = positions[index]
                placeable.placeRelative(x, y)
            }
        }
    }
}

@Composable
private fun StaggeredGridExample() {
    var columns by remember { mutableIntStateOf(2) }
    var itemCount by remember { mutableIntStateOf(10) }
    
    // Генерируем случайные высоты для элементов
    val itemHeights = remember {
        List(20) { Random.nextInt(100, 200) }
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StaggeredGrid(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            columns = columns,
            verticalGap = 8,
            horizontalGap = 8
        ) {
            repeat(itemCount) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeights[index].dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = when (index % 5) {
                                    0 -> Color.Red.copy(alpha = 0.7f)
                                    1 -> Color.Green.copy(alpha = 0.7f)
                                    2 -> Color.Blue.copy(alpha = 0.7f)
                                    3 -> Color.Yellow.copy(alpha = 0.7f)
                                    else -> Color.Magenta.copy(alpha = 0.7f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Элемент ${index + 1}",
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Количество колонок: $columns")
        
        Slider(
            value = columns.toFloat(),
            onValueChange = { columns = it.toInt() },
            valueRange = 1f..4f,
            steps = 3,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text("Количество элементов: $itemCount")
        
        Slider(
            value = itemCount.toFloat(),
            onValueChange = { itemCount = it.toInt() },
            valueRange = 1f..20f,
            steps = 19,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
} 