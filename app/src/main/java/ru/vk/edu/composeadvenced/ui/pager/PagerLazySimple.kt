package ru.vk.edu.composeadvenced.ui.pager

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val BasePageIndicationSize = 8.dp
private val SpaceSize = 10.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicatorLazyListSimple(
    state: PagerIndicatorState,
    modifier: Modifier = Modifier,
    itemSpace: Dp = SpaceSize,
) {
    val currentPage = state.pagerState.currentPage
    val listState = rememberLazyListState()
    val totalWidthExceptForAdd = BasePageIndicationSize * state.indicationCount +
            itemSpace * (state.indicationCount - 1)
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    LaunchedEffect(key1 = currentPage) {
        updateState(
            currentPage = currentPage,
            listState = listState,
            indicationCount = state.indicationCount
        )
    }

    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.widthIn(itemSpace / 2))
        LazyRow(
            state = listState,
            userScrollEnabled = false,
            modifier = Modifier
                .widthIn(max = totalWidthExceptForAdd)
                .align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.spacedBy(itemSpace),
        ) {
            indicationItems(
                itemsCount = state.pageCount,
                currentItem = currentPage,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
            )
        }
    }
}

@Immutable
data class PagerIndicatorState(
    val pagerState: PagerState,
    val pageCount: Int,
    val indicationCount: Int = 3,
)

private suspend fun updateState(
    currentPage: Int,
    listState: LazyListState,
    indicationCount: Int,
) {
    val targetPage = currentPage

    if (targetPage >= 0) {
        val visibleItems = listState.layoutInfo.visibleItemsInfo.filter { it.offset >= 0 }
        val firstVisibleItemIndex = visibleItems.firstOrNull()?.index ?: 0
        val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0

        try {
            if (targetPage < firstVisibleItemIndex) {
                listState.animateScrollToItem(targetPage)
            } else if (targetPage > lastVisibleItemIndex) {
                listState.animateScrollToItem(targetPage - (indicationCount - 1))
            }
        } catch (ex: IllegalArgumentException) {
            val newMessage = "targetPage $targetPage; " +
                    "lastVisibleItemIndex $lastVisibleItemIndex indicationCount $indicationCount"
            Log.e(
                "PagerIndicator",
                "Failed to animate scroll",
                IllegalArgumentException("${ex.message}. $newMessage")
            )
        }
    }
}

private fun LazyListScope.indicationItems(
    itemsCount: Int,
    currentItem: Int,
    activeColor: Color,
    inactiveColor: Color,
) {
    items(itemsCount) { index ->
        val isSelected = (index == currentItem)
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(BasePageIndicationSize)
                .background(if (isSelected) activeColor else inactiveColor)
        )
    }
}
