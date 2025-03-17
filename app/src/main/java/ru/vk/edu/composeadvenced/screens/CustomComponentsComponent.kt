package ru.vk.edu.composeadvenced.screens

import com.arkivanov.decompose.ComponentContext

class CustomComponentsComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val onNavigateToCustomModifier: () -> Unit,
    private val onNavigateToCustomDraw: () -> Unit,
    private val onNavigateToCustomLayout: () -> Unit,
    private val onNavigateToComplexCustomComponent: () -> Unit
) : ComponentContext by componentContext {
    
    fun onBackClick() {
        onBack()
    }
    
    fun onCustomModifierClick() {
        onNavigateToCustomModifier()
    }
    
    fun onCustomDrawClick() {
        onNavigateToCustomDraw()
    }
    
    fun onCustomLayoutClick() {
        onNavigateToCustomLayout()
    }
    
    fun onComplexCustomComponentClick() {
        onNavigateToComplexCustomComponent()
    }
} 