package ru.vk.edu.composeadvenced.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed interface Configuration : Parcelable {
    @Parcelize
    data object Main : Configuration

    @Parcelize
    data object Animations : Configuration
    
    // Добавляем новые экраны для разных типов анимаций
    @Parcelize
    data object AnimatableScreen : Configuration
    
    @Parcelize
    data object AnimateAsStateScreen : Configuration
    
    @Parcelize
    data object UpdateTransitionScreen : Configuration
    
    @Parcelize
    data object AnimatedVisibilityScreen : Configuration
    
    @Parcelize
    data object AnimatedContentScreen : Configuration
    
    @Parcelize
    data object CrossfadeScreen : Configuration
    
    @Parcelize
    data object AnimateContentSizeScreen : Configuration
    
    @Parcelize
    data object AnchoredDraggableScreen : Configuration
    
    @Parcelize
    data object AnimateItemPlacementScreen : Configuration
    
    // Экраны для кастомных компонентов
    @Parcelize
    data object CustomComponents : Configuration
    
    @Parcelize
    data object CustomModifierScreen : Configuration
    
    @Parcelize
    data object CustomDrawScreen : Configuration
    
    @Parcelize
    data object CustomLayoutScreen : Configuration
    
    @Parcelize
    data object ComplexCustomComponentScreen : Configuration

    // Демонстрация Decompose
    @Parcelize
    data object ViewModel : Configuration
}