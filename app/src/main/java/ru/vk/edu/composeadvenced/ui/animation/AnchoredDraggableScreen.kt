package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.vk.edu.composeadvenced.screens.animation.AnchoredDraggableComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle
import kotlin.math.roundToInt

enum class DragAnchors {
    Start, Center, End
}

enum class SwipeAnchors {
    Left, Center, Right
}

@Composable
fun AnchoredDraggableScreen(component: AnchoredDraggableComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "AnchoredDraggable",
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
            SimpleAnchoredDraggableExample()
            
            SectionTitle(title = "Свайп элемента списка")
            SwipeableListItemExample()
            
            SectionTitle(title = "Сложный пример")
            AdvancedAnchoredDraggableExample()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SimpleAnchoredDraggableExample() {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { 100f },
            snapAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            decayAnimationSpec = exponentialDecay(),
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    DragAnchors.Start at -100f
                    DragAnchors.Center at 0f
                    DragAnchors.End at 100f
                }
            )
        }
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = dragState.offset.roundToInt(),
                            y = 0
                        )
                    }
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .anchoredDraggable(
                        state = dragState,
                        orientation = Orientation.Horizontal
                    )
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    scope.launch {
                        dragState.animateTo(DragAnchors.Start)
                    }
                }
            ) {
                Text("Влево")
            }
            
            Button(
                onClick = {
                    scope.launch {
                        dragState.animateTo(DragAnchors.Center)
                    }
                }
            ) {
                Text("Центр")
            }
            
            Button(
                onClick = {
                    scope.launch {
                        dragState.animateTo(DragAnchors.End)
                    }
                }
            ) {
                Text("Вправо")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwipeableListItemExample() {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    
    val swipeState = remember {
        AnchoredDraggableState(
            initialValue = SwipeAnchors.Center,
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { 100f },
            snapAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            decayAnimationSpec = exponentialDecay()
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    SwipeAnchors.Left at -200f
                    SwipeAnchors.Center at 0f
                    SwipeAnchors.Right at 200f
                }
            )
        }
    }
    
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Фоновые действия
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Левые действия
            Row(
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Избранное",
                    modifier = Modifier.padding(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    modifier = Modifier.padding(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Правые действия
            Row(
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.error),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    modifier = Modifier.padding(16.dp),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }

        // Передний элемент списка
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .offset {
                    IntOffset(
                        x = swipeState.offset.roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(
                    state = swipeState,
                    orientation = Orientation.Horizontal
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Свайпните элемент влево или вправо",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                scope.launch {
                    swipeState.animateTo(SwipeAnchors.Center)
                }
            }
        ) {
            Text("Сбросить")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AdvancedAnchoredDraggableExample() {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Создаем состояние для горизонтального перетаскивания
    val horizontalDragState = remember {
        AnchoredDraggableState(
            initialValue = 0,
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { 100f },
            snapAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            decayAnimationSpec = exponentialDecay()
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    -2 at -200f
                    -1 at -100f
                    0 at 0f
                    1 at 100f
                    2 at 200f
                }
            )
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
            // Фоновые индикаторы позиций
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in -2..2) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(
                                if (horizontalDragState.currentValue == i) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    Color.LightGray
                            )
                    )
                }
            }

            // Перетаскиваемый элемент
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = horizontalDragState.offset.roundToInt(),
                            y = 0
                        )
                    }
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        when (horizontalDragState.currentValue) {
                            -2 -> Color.Red
                            -1 -> Color.Blue
                            0 -> MaterialTheme.colorScheme.primary
                            1 -> Color.Green
                            2 -> Color.Magenta
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                    .anchoredDraggable(
                        state = horizontalDragState,
                        orientation = Orientation.Horizontal
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${horizontalDragState.currentValue}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Текущая позиция: ${horizontalDragState.currentValue}",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    scope.launch {
                        horizontalDragState.animateTo(horizontalDragState.currentValue - 1)
                    }
                },
                enabled = horizontalDragState.currentValue > -2
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }
            
            Button(
                onClick = {
                    scope.launch {
                        horizontalDragState.animateTo(0)
                    }
                }
            ) {
                Text("Сбросить")
            }
            
            Button(
                onClick = {
                    scope.launch {
                        horizontalDragState.animateTo(horizontalDragState.currentValue + 1)
                    }
                },
                enabled = horizontalDragState.currentValue < 2
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Вперед")
            }
        }
    }
} 
