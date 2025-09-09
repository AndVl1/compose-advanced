package ru.vk.edu.composeadvenced.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Blue,
    inactiveColor: Color = Color.Gray,
    onIndicatorClick: (Int) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalPages) { index ->
            val isSelected = index == currentPage
            val size = if (isSelected) 12.dp else 8.dp
            
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(if (isSelected) activeColor else inactiveColor)
                    .clickable { onIndicatorClick(index) }
            )
        }
    }
}