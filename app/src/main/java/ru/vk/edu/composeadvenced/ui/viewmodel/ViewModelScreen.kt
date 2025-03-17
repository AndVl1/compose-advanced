package ru.vk.edu.composeadvenced.ui.viewmodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.vk.edu.composeadvenced.screens.viewmodel.ViewModelComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar

@Composable
fun ViewModelScreen(component: ViewModelComponent) {
    val state by component.state.subscribeAsState()
    val childState by component.commentSlot.subscribeAsState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = "ViewModel с Decompose",
                showBackButton = true,
                onBackClick = { component.onBackClick() }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { component.showCommentSheet() },
                icon = { Icon(Icons.Default.Edit, contentDescription = "Комментарии") },
                text = { Text("Комментарии") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Демонстрация сохранения состояния",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Text(
                text = "Состояние сохраняется при смене конфигурации",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Поле ввода текста
            OutlinedTextField(
                value = state.text,
                onValueChange = { component.updateText(it) },
                label = { Text("Введите текст") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Кнопка добавления счетчика
            Button(
                onClick = { component.addCounter() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить счетчик")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Добавить счетчик")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Счетчики
            state.counters.forEachIndexed { index, value ->
                CounterCard(
                    value = value,
                    onIncrement = { component.incrementCounter(index) },
                    onDecrement = { component.decrementCounter(index) },
                    onRemove = { component.removeCounter(index) },
                    canRemove = state.counters.size > 1
                )
            }
            
            // Информация о комментариях
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Комментарии (${state.comments.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = if (state.comments.isEmpty()) 
                            "Нет комментариев. Нажмите на кнопку внизу, чтобы добавить." 
                        else 
                            "Нажмите на кнопку внизу, чтобы просмотреть комментарии.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { component.showCommentSheet() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Комментарии")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Открыть комментарии")
                    }
                }
            }
        }

        // Отображаем модальное окно с комментариями через childSlot
        childState.child?.instance?.let { modal ->
            when (modal) {
                is ViewModelComponent.CommentChild.Comments -> CommentBottomSheet(modal.component)
            }
        }
    }
}

@Composable
private fun CounterCard(
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    canRemove: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Счетчик: $value",
                    style = MaterialTheme.typography.titleMedium
                )
                
                if (canRemove) {
                    IconButton(onClick = onRemove) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Удалить счетчик",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onDecrement) {
                    Text("-")
                }
                
                Button(onClick = onIncrement) {
                    Text("+")
                }
            }
        }
    }
} 
