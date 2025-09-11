package ru.vk.edu.composeadvenced.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed interface Configuration {
    data object Main : Configuration

    data object Animations : Configuration

    // Добавляем новые экраны для разных типов анимаций
    data object AnimatableScreen : Configuration

    data object AnimateAsStateScreen : Configuration

    data object UpdateTransitionScreen : Configuration

    data object AnimatedVisibilityScreen : Configuration

    data object AnimatedContentScreen : Configuration

    data object CrossfadeScreen : Configuration

    data object AnimateContentSizeScreen : Configuration

    data object AnchoredDraggableScreen : Configuration

    data object AnimateItemPlacementScreen : Configuration

    data object CardStackScreen : Configuration

    // Экраны для кастомных компонентов
    data object CustomComponents : Configuration

    data object CustomModifierScreen : Configuration

    data object CustomDrawScreen : Configuration

    data object CustomLayoutScreen : Configuration

    data object ComplexCustomComponentScreen : Configuration

    // Демонстрация Decompose
    data object ViewModel : Configuration

    // Pager Indicator
    data object PagerIndicator : Configuration

    // Shared Element Transition
    data object SharedElementList : Configuration
}
