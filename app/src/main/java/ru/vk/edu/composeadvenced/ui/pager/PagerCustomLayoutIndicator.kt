package ru.vk.edu.composeadvenced.ui.pager

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val BasePageIndicationSize = 8.dp
private val SpaceSize = 10.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerCustomLayoutIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    itemSpace: Dp = SpaceSize,
) {
    val density = LocalDensity.current
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    // Calculate the smooth animated position
    // можно просто val position = pagerState.currentPage + pagerState.currentPageOffsetFraction,
    // если не нужна анимация
    val animatedPosition by animateFloatAsState(
        targetValue = pagerState.currentPage + pagerState.currentPageOffsetFraction,
        animationSpec = tween(durationMillis = 0),
        label = "indicatorPosition"
    )

    Layout(
        modifier = modifier,
        content = {
            // Static dots (inactive)
            repeat(pageCount) { index ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(BasePageIndicationSize)
                        .background(inactiveColor)
                )
            }

            // Animated active dot
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(BasePageIndicationSize)
                    .background(activeColor)
            )
        }
    ) { measurables, constraints ->
        customPagerIndicatorMeasurePolicy(
            measurables = measurables,
            constraints = constraints,
            pageCount = pageCount,
            animatedPosition = animatedPosition,
            itemSpacePx = with(density) { itemSpace.toPx() },
            dotSizePx = with(density) { BasePageIndicationSize.toPx() }
        )
    }
}

private fun MeasureScope.customPagerIndicatorMeasurePolicy(
    measurables: List<Measurable>,
    constraints: Constraints,
    pageCount: Int,
    animatedPosition: Float,
    itemSpacePx: Float,
    dotSizePx: Float
): MeasureResult {
    // Measure all dots with the same constraints
    val dotConstraints = Constraints.fixed(dotSizePx.roundToInt(), dotSizePx.roundToInt())
    val placeables = measurables.map { it.measure(dotConstraints) }

    // Calculate total width needed
    val totalWidth = (pageCount * dotSizePx + (pageCount - 1) * itemSpacePx).roundToInt()
    val totalHeight = dotSizePx.roundToInt()

    return layout(totalWidth, totalHeight) {
        // Place static dots
        repeat(pageCount) { index ->
            val x = (index * (dotSizePx + itemSpacePx)).roundToInt()
            placeables[index].place(IntOffset(x, 0))
        }

        // Place animated active dot
        val activeX = (animatedPosition * (dotSizePx + itemSpacePx)).roundToInt()
        placeables[pageCount].place(IntOffset(activeX, 0)) // Active dot is the last measurable
    }
}
