package ru.vk.edu.composeadvenced.screens.custom

import com.arkivanov.decompose.ComponentContext

class CustomLayoutComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit
) : ComponentContext by componentContext {
    
    fun onBackClick() {
        onBack()
    }
} 