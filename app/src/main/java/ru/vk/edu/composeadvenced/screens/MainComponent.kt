package ru.vk.edu.composeadvenced.screens

import com.arkivanov.decompose.ComponentContext

class MainComponent(
    componentContext: ComponentContext,
    private val onNavigateToAnimations: () -> Unit,
    private val onNavigateToCustomComponents: () -> Unit,
    private val onNavigateToViewModelClick: () -> Unit,
    private val onNavigateToPagerIndicator: () -> Unit,
    private val onNavigateToSharedElement: () -> Unit,
    private val onNavigateToLottieAnimations: () -> Unit,
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

    fun onPagerIndicatorClick() {
        onNavigateToPagerIndicator()
    }

    fun onSharedElementClick() {
        onNavigateToSharedElement()
    }

    fun onLottieAnimationsClick() {
        onNavigateToLottieAnimations()
    }
} 