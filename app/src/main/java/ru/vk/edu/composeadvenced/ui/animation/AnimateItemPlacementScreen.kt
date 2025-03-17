package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.animation.AnimateItemPlacementComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle

@Composable
fun AnimateItemPlacementScreen(component: AnimateItemPlacementComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "animateItemPlacement",
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
            SectionTitle(title = "Сортировка списка")
            AnimatedSortableListExample()
            
            SectionTitle(title = "Добавление и удаление элементов")
            AnimatedListWithAddRemoveExample()
            
            SectionTitle(title = "Горизонтальный список с анимацией")
            AnimatedHorizontalListExample()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimatedSortableListExample() {
    var items by remember { mutableStateOf(List(5) { "Элемент ${it + 1}" }) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { items = items.shuffled() }) {
                Text("Перемешать")
            }
            
            Button(onClick = { items = items.sorted() }) {
                Text("Сортировать")
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items, key = { it }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .animateItemPlacement(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimatedListWithAddRemoveExample() {
    var items by remember { mutableStateOf(List(3) { "Элемент ${it + 1}" }) }
    var nextItem by remember { mutableIntStateOf(4) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { 
                    items = items + "Элемент $nextItem"
                    nextItem++
                }
            ) {
                Text("Добавить")
            }
            
            Button(
                onClick = { 
                    if (items.isNotEmpty()) {
                        items = items.dropLast(1)
                    }
                },
                enabled = items.isNotEmpty()
            ) {
                Text("Удалить последний")
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items, key = { it }) { item ->
                    ListItem(
                        headlineContent = { Text(item) },
                        trailingContent = {
                            IconButton(onClick = { items = items - item }) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(
                                animationSpec = tween(durationMillis = 300)
                            )
                    )
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimatedHorizontalListExample() {
    val colors = remember {
        listOf(
            Color.Red, Color.Blue, Color.Green, Color.Magenta, 
            Color.Cyan, Color.Yellow, Color.DarkGray
        )
    }
    
    var items by remember { mutableStateOf(colors.take(5)) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { items = items.shuffled() }) {
                Text("Перемешать")
            }
            
            Button(
                onClick = { 
                    if (items.size < colors.size) {
                        // Добавляем новый цвет из доступных
                        val availableColors = colors - items.toSet()
                        if (availableColors.isNotEmpty()) {
                            items = items + availableColors.first()
                        }
                    }
                },
                enabled = items.size < colors.size
            ) {
                Text("Добавить")
            }
            
            Button(
                onClick = { 
                    if (items.size > 1) {
                        items = items.dropLast(1)
                    }
                },
                enabled = items.size > 1
            ) {
                Text("Удалить")
            }
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.hashCode() }) { color ->
                val isSelected = items.indexOf(color) == selectedIndex
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1.0f,
                    label = "scale"
                )
                
                Box(
                    modifier = Modifier
                        .size(60.dp * scale)
                        .clip(if (isSelected) CircleShape else RoundedCornerShape(8.dp))
                        .background(color)
                        .animateItemPlacement(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .padding(4.dp)
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.7f))
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { 
                    selectedIndex = if (selectedIndex >= items.size - 1) 0 else selectedIndex + 1
                }
            ) {
                Text("Выбрать следующий")
            }
        }
    }
} 