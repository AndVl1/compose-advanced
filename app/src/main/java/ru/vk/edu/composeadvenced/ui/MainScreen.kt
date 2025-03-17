package ru.vk.edu.composeadvenced.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.MainComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar

@Composable
fun MainScreen(component: MainComponent) {
    Scaffold(
        topBar = {
            AppToolbar(title = "Compose Advanced")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { component.onAnimationsClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Анимации")
            }
            
            Button(
                onClick = { component.onCustomComponentsClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Кастомные компоненты")
            }
            
            Button(
                onClick = { component.onViewModelClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ViewModel с Decompose")
            }
        }
    }
} 