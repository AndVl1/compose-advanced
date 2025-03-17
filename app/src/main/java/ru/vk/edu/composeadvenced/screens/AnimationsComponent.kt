package ru.vk.edu.composeadvenced.screens

import com.arkivanov.decompose.ComponentContext

class AnimationsComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val onNavigateToAnimatable: () -> Unit,
    private val onNavigateToAnimateAsState: () -> Unit,
    private val onNavigateToUpdateTransition: () -> Unit,
    private val onNavigateToAnimatedVisibility: () -> Unit,
    private val onNavigateToAnimatedContent: () -> Unit,
    private val onNavigateToCrossfade: () -> Unit,
    private val onNavigateToAnimateContentSize: () -> Unit,
    private val onNavigateToAnchoredDraggable: () -> Unit,
    private val onNavigateToAnimateItemPlacement: () -> Unit
) : ComponentContext by componentContext {
    
    fun onBackClick() {
        onBack()
    }
    
    fun onAnimatableClick() {
        onNavigateToAnimatable()
    }
    
    fun onAnimateAsStateClick() {
        onNavigateToAnimateAsState()
    }
    
    fun onUpdateTransitionClick() {
        onNavigateToUpdateTransition()
    }
    
    fun onAnimatedVisibilityClick() {
        onNavigateToAnimatedVisibility()
    }
    
    fun onAnimatedContentClick() {
        onNavigateToAnimatedContent()
    }
    
    fun onCrossfadeClick() {
        onNavigateToCrossfade()
    }
    
    fun onAnimateContentSizeClick() {
        onNavigateToAnimateContentSize()
    }
    
    fun onAnchoredDraggableClick() {
        onNavigateToAnchoredDraggable()
    }
    
    fun onAnimateItemPlacementClick() {
        onNavigateToAnimateItemPlacement()
    }
} 