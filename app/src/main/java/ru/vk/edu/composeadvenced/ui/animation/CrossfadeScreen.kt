package ru.vk.edu.composeadvenced.ui.animation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.vk.edu.composeadvenced.screens.animation.CrossfadeComponent
import ru.vk.edu.composeadvenced.ui.components.AppToolbar
import ru.vk.edu.composeadvenced.ui.components.SectionTitle

@Composable
fun CrossfadeScreen(component: CrossfadeComponent) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Crossfade",
                showBackButton = true,
                onBackClick = { component.onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SectionTitle(title = "Простой пример")
            SimpleCrossfadeExample()
            
            SectionTitle(title = "Настраиваемая длительность")
            CustomDurationCrossfadeExample()
            
            SectionTitle(title = "Пример навигации")
            NavigationCrossfadeExample()
        }
    }
}

enum class CrossfadeState {
    Red, Green, Blue
}

@Composable
private fun SimpleCrossfadeExample() {
    var currentState by remember { mutableStateOf(CrossfadeState.Red) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(
            targetState = currentState,
            animationSpec = tween(durationMillis = 500),
            label = "color crossfade"
        ) { state ->
            when (state) {
                CrossfadeState.Red -> Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Red)
                )
                CrossfadeState.Green -> Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
                CrossfadeState.Blue -> Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Blue)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { currentState = CrossfadeState.Red }) {
                Text("Красный")
            }
            
            Button(onClick = { currentState = CrossfadeState.Green }) {
                Text("Зеленый")
            }
            
            Button(onClick = { currentState = CrossfadeState.Blue }) {
                Text("Синий")
            }
        }
    }
}

@Composable
private fun CustomDurationCrossfadeExample() {
    var currentIndex by remember { mutableIntStateOf(0) }
    var animationDuration by remember { mutableIntStateOf(500) }
    
    val shapes = listOf(
        RoundedCornerShape(0.dp),
        RoundedCornerShape(8.dp),
        RoundedCornerShape(16.dp),
        CircleShape,
        RoundedCornerShape(50)
    )
    
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.surfaceVariant
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(
            targetState = currentIndex,
            animationSpec = tween(durationMillis = animationDuration),
            label = "custom duration crossfade"
        ) { index ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(shapes[index])
                    .background(colors[index])
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Длительность анимации: $animationDuration мс")
        
        Slider(
            value = animationDuration.toFloat(),
            onValueChange = { animationDuration = it.toInt() },
            valueRange = 100f..2000f,
            steps = 19,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                currentIndex = (currentIndex + 1) % shapes.size
            }
        ) {
            Text("Следующая форма")
        }
    }
}

enum class Screen {
    Home, Favorites, Settings
}

@Composable
private fun NavigationCrossfadeExample() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(
                targetState = currentScreen,
                animationSpec = tween(durationMillis = 300),
                label = "navigation crossfade"
            ) { screen ->
                when (screen) {
                    Screen.Home -> HomeScreen()
                    Screen.Favorites -> FavoritesScreen()
                    Screen.Settings -> SettingsScreen()
                }
            }
        }
        
        NavigationBar {
            NavigationBarItem(
                selected = currentScreen == Screen.Home,
                onClick = { currentScreen = Screen.Home },
                icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
                label = { Text("Главная") }
            )
            
            NavigationBarItem(
                selected = currentScreen == Screen.Favorites,
                onClick = { currentScreen = Screen.Favorites },
                icon = { Icon(Icons.Default.Favorite, contentDescription = "Избранное") },
                label = { Text("Избранное") }
            )
            
            NavigationBarItem(
                selected = currentScreen == Screen.Settings,
                onClick = { currentScreen = Screen.Settings },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Настройки") },
                label = { Text("Настройки") }
            )
        }
    }
}

@Composable
private fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Главная страница",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun FavoritesScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Избранное",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.headlineSmall
        )
    }
} 