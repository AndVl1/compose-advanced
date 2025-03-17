package ru.vk.edu.composeadvenced.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.CustomComponentsComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar

@Composable
fun CustomComponentsScreen(component: CustomComponentsComponent) {
    val customComponentItems = listOf(
        CustomComponentItem("Кастомные модификаторы", component::onCustomModifierClick),
        CustomComponentItem("Кастомная отрисовка", component::onCustomDrawClick),
        CustomComponentItem("Кастомные лейауты", component::onCustomLayoutClick),
        CustomComponentItem("Сложный кастомный компонент", component::onComplexCustomComponentClick)
    )

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Кастомные компоненты",
                showBackButton = true,
                onBackClick = { component.onBackClick() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(customComponentItems) { item ->
                CustomComponentItemCard(item)
            }
        }
    }
}

@Composable
private fun CustomComponentItemCard(item: CustomComponentItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

private data class CustomComponentItem(
    val title: String,
    val onClick: () -> Unit
) 