package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.animation.AnimateContentSizeComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle

@Composable
fun AnimateContentSizeScreen(component: AnimateContentSizeComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "animateContentSize",
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
            SimpleAnimateContentSizeExample()
            
            SectionTitle(title = "Настраиваемая анимация")
            CustomAnimateContentSizeExample()
            
            SectionTitle(title = "Карточка с раскрывающимся текстом")
            ExpandableCardExample()
        }
    }
}

@Composable
private fun SimpleAnimateContentSizeExample() {
    var expanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
                .animateContentSize()
                .width(if (expanded) 200.dp else 100.dp)
                .height(if (expanded) 100.dp else 50.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Сжать" else "Расширить")
        }
    }
}

enum class AnimationSpec {
    SPRING, TWEEN
}

@Composable
private fun CustomAnimateContentSizeExample() {
    var expanded by remember { mutableStateOf(false) }
    var selectedAnimationSpec by remember { mutableStateOf(AnimationSpec.SPRING) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .animateContentSize(
                    animationSpec = when (selectedAnimationSpec) {
                        AnimationSpec.SPRING -> spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                        AnimationSpec.TWEEN -> tween(durationMillis = 500)
                    }
                )
                .width(if (expanded) 200.dp else 100.dp)
                .height(if (expanded) 100.dp else 50.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
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
                    selected = selectedAnimationSpec == AnimationSpec.SPRING,
                    onClick = { selectedAnimationSpec = AnimationSpec.SPRING }
                )
                Text("Spring (с отскоком)")
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnimationSpec == AnimationSpec.TWEEN,
                    onClick = { selectedAnimationSpec = AnimationSpec.TWEEN }
                )
                Text("Tween (линейная)")
            }
        }
        
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Сжать" else "Расширить")
        }
    }
}

@Composable
private fun ExpandableCardExample() {
    var expanded by remember { mutableStateOf(false) }
    
    val longText = "Это длинный текст, который будет отображаться в карточке. " +
            "При нажатии на карточку, она будет расширяться, чтобы показать весь текст. " +
            "Анимация изменения размера будет плавной благодаря модификатору animateContentSize. " +
            "Это очень полезно для создания раскрывающихся элементов интерфейса, таких как FAQ, " +
            "детали элементов списка и другие подобные компоненты, где нужно показать дополнительную " +
            "информацию по запросу пользователя."
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Раскрывающаяся карточка",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Свернуть" else "Развернуть"
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = longText,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { expanded = false },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Свернуть")
                }
            }
        }
    }
} 