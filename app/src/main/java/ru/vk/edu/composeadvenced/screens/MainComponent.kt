package ru.vk.edu.composeadvenced.screens

import com.arkivanov.decompose.ComponentContext

class MainComponent(
    componentContext: ComponentContext,
    private val onNavigateToAnimations: () -> Unit,
    private val onNavigateToCustomComponents: () -> Unit,
    private val onNavigateToViewModelClick: () -> Unit,
) : ComponentContext by componentContext {
    
    fun onAnimationsClick() {
        onNavigateToAnimations()
    }
    
    fun onCustomComponentsClick() {
        onNavigateToCustomComponents()
    }

    fun onViewModelClick() {
        onNavigateToViewModelClick()
    }
} 