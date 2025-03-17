package ru.vk.edu.composeadvenced.screens.animation

import com.arkivanov.decompose.ComponentContext

class AnimatableComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit
) : ComponentContext by componentContext {
    
    fun onBackClick() {
        onBack()
    }
} 