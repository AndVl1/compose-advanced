package ru.vk.edu.composeadvenced

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import ru.vk.edu.composeadvenced.root.RootComponent
import ru.vk.edu.composeadvenced.ui.RootContent
import ru.vk.edu.composeadvenced.ui.theme.ComposeadvencedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Включаем edge-to-edge режим для учета системных отступов
        enableEdgeToEdge()
        
        val root = RootComponent(defaultComponentContext())
        
        setContent {
            ComposeadvencedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootContent(component = root)
                }
            }
        }
    }
}