package ru.vk.edu.composeadvenced.screens

import com.arkivanov.decompose.ComponentContext

class PagerIndicatorComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit
) : ComponentContext by componentContext {
    
    fun onBackClicked() {
        onBack()
    }
}