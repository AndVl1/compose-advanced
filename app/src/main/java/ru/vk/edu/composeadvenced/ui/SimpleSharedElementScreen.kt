package ru.vk.edu.composeadvenced.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vk.edu.composeadvenced.data.ImageData
import ru.vk.edu.composeadvenced.data.ImageItem
import ru.vk.edu.composeadvenced.screens.SharedElementListComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar

sealed class SharedElementScreen {
    object List : SharedElementScreen()
    data class Detail(val imageId: String, val title: String) : SharedElementScreen()
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SimpleSharedElementScreen(
    component: SharedElementListComponent
) {
    var currentScreen by remember { mutableStateOf<SharedElementScreen>(SharedElementScreen.List) }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = currentScreen,
            /*transitionSpec = {
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) togetherWith fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },*/
            label = "SharedElementTransition"
        ) { screen ->
            when (screen) {
                is SharedElementScreen.List -> {
                    SimpleImageListScreen(
                        onImageClick = { imageId, title ->
                            currentScreen = SharedElementScreen.Detail(imageId, title)
                        },
                        onBackClick = { component.onBackClicked() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )
                }
                is SharedElementScreen.Detail -> {
                    SimpleImageDetailScreen(
                        imageId = screen.imageId,
                        title = screen.title,
                        onBackClick = { currentScreen = SharedElementScreen.List },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SimpleImageListScreen(
    onImageClick: (String, String) -> Unit,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: androidx.compose.animation.AnimatedVisibilityScope
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Shared Element Gallery",
                showBackButton = true,
                onBackClick = onBackClick,
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(ImageData.sampleImages) { image ->
                with(sharedTransitionScope) {
                    SimpleImageCard(
                        image = image,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onClick = { onImageClick(image.id, image.title) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SimpleImageCard(
    image: ImageItem,
    animatedVisibilityScope: androidx.compose.animation.AnimatedVisibilityScope,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = image.imageUrl,
                contentDescription = image.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image-${image.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = image.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(key = "title-${image.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = image.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SimpleImageDetailScreen(
    imageId: String,
    title: String,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: androidx.compose.animation.AnimatedVisibilityScope
) {
    val image = ImageData.sampleImages.find { it.id == imageId }

    if (image == null) {
        Scaffold(
            topBar = {
                AppToolbar(
                    title = "Image Not Found",
                    showBackButton = true,
                    onBackClick = onBackClick
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Image not found")
            }
        }
        return
    }

    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                AppToolbar(
                    title = title,
                    showBackButton = true,
                    onBackClick = onBackClick
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Hero image with shared element transition
                AsyncImage(
                    model = image.imageUrl,
                    contentDescription = image.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 10f)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image-${image.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    contentScale = ContentScale.Crop
                )

                // Content section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Title with shared element transition
                    Text(
                        text = image.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = "title-${image.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = image.description,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Additional content sections
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Image Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            SimpleDetailRow("Image ID", image.id)
                            SimpleDetailRow("Title", image.title)
                            SimpleDetailRow("URL", image.imageUrl)
                        }
                    }

                    // Bottom spacing
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun SimpleDetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
