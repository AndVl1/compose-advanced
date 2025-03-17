package ru.vk.edu.composeadvenced.ui.viewmodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import ru.vk.edu.composeadvenced.screens.viewmodel.CommentComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(component: CommentComponent) {
    val state by component.state.subscribeAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    
    ModalBottomSheet(
        onDismissRequest = { component.onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Комментарии",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Поле ввода комментария
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.commentText,
                    onValueChange = { component.onCommentTextChanged(it) },
                    label = { Text("Добавить комментарий") },
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(onClick = { 
                    component.onAddComment()
                    // Можно добавить вибрацию или другую обратную связь
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Отправить"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Список комментариев
            if (state.comments.isEmpty()) {
                Text(
                    text = "Нет комментариев. Будьте первым!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(state.comments) { comment ->
                        CommentItem(comment)
                    }
                }
            }
            
            // Добавляем пространство внизу для лучшего UX
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CommentItem(comment: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = comment,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
} 