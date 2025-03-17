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
import ru.vk.edu.composeadvenced.screens.AnimationsComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar

@Composable
fun AnimationsScreen(component: AnimationsComponent) {
    val animationItems = listOf(
        AnimationItem("Animatable", component::onAnimatableClick),
        AnimationItem("animate*asState", component::onAnimateAsStateClick),
        AnimationItem("updateTransition", component::onUpdateTransitionClick),
        AnimationItem("CertificatesStack", component::onCertificatesStackClick),
        AnimationItem("AnimatedVisibility", component::onAnimatedVisibilityClick),
        AnimationItem("AnimatedContent", component::onAnimatedContentClick),
        AnimationItem("Crossfade", component::onCrossfadeClick),
        AnimationItem("Modifier.animateContentSize", component::onAnimateContentSizeClick),
        AnimationItem("AnchoredDraggable", component::onAnchoredDraggableClick),
        AnimationItem("animateItemPlacement", component::onAnimateItemPlacementClick),
    )

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Анимации",
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
            items(animationItems) { item ->
                AnimationItemCard(item)
            }
        }
    }
}

@Composable
private fun AnimationItemCard(item: AnimationItem) {
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

private data class AnimationItem(
    val title: String,
    val onClick: () -> Unit
) 
