package ru.vk.edu.composeadvenced.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.ui.pager.PagerIndicatorLazyListSimple
import ru.vk.edu.composeadvenced.ui.pager.PagerIndicatorState
import ru.vk.edu.composeadvenced.screens.PagerIndicatorComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicatorScreen(
    component: PagerIndicatorComponent
) {
    val pageCount = 5
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Pager Indicator Demo",
                onBackClick = { component.onBackClicked() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Swipe the pages below to see the indicator in action",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { page ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (page) {
                            0 -> Color(0xFF6200EE)
                            1 -> Color(0xFF03DAC6)
                            2 -> Color(0xFFFF6200)
                            3 -> Color(0xFF4CAF50)
                            4 -> Color(0xFFFF9800)
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Page ${page + 1}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Pager Indicator",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            PagerIndicatorLazyListSimple(
                state = PagerIndicatorState(
                    pagerState = pagerState,
                    pageCount = pageCount,
                    indicationCount = 3
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
