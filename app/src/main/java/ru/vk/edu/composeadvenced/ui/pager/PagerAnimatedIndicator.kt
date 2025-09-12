package ru.vk.edu.composeadvenced.ui.pager

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val BasePageIndicationSize = 8.dp
private val SpaceSize = 10.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerAnimatedIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    itemSpace: Dp = SpaceSize,
) {
    val density = LocalDensity.current
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    // Calculate the offset for the animated dot based on currentPageOffsetFraction
    // можно просто val offset = (pagerState.currentPage + pagerState.currentPageOffsetFraction) * (BasePageIndicationSize + itemSpace).toPx()
    // если не нужна анимация
    val animatedOffset by animateFloatAsState(
        targetValue = with(density) {
            (pagerState.currentPage + pagerState.currentPageOffsetFraction) * (BasePageIndicationSize + itemSpace).toPx()
        },
        animationSpec = tween(durationMillis = 0),
        label = "indicatorOffset"
    )

    Box(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(itemSpace),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Static dots (inactive)
            repeat(pageCount) { index ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(BasePageIndicationSize)
                        .background(inactiveColor)
                )
            }
        }

        // Animated active dot that moves smoothly
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(
                    x = with(density) { animatedOffset.toDp() - (pageCount - 1) * (BasePageIndicationSize + itemSpace) / 2 }
                )
                .clip(CircleShape)
                .size(BasePageIndicationSize)
                .background(activeColor)
        )
    }
}

private operator fun Int.times(other: Dp): Dp = other * this
