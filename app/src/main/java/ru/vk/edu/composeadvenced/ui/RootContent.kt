package ru.vk.edu.composeadvenced.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.vk.edu.composeadvenced.root.RootComponent
import ru.vk.edu.composeadvenced.ui.animation.AnchoredDraggableScreen
import ru.vk.edu.composeadvenced.ui.animation.AnimatableScreen
import ru.vk.edu.composeadvenced.ui.animation.AnimateAsStateScreen
import ru.vk.edu.composeadvenced.ui.animation.AnimateContentSizeScreen
import ru.vk.edu.composeadvenced.ui.animation.AnimateItemPlacementScreen
import ru.vk.edu.composeadvenced.ui.animation.AnimatedContentScreen
import ru.vk.edu.composeadvenced.ui.animation.AnimatedVisibilityScreen
import ru.vk.edu.composeadvenced.ui.animation.CrossfadeScreen
import ru.vk.edu.composeadvenced.ui.animation.UpdateTransitionScreen
import ru.vk.edu.composeadvenced.ui.custom.ComplexCustomComponentScreen
import ru.vk.edu.composeadvenced.ui.custom.CustomDrawScreen
import ru.vk.edu.composeadvenced.ui.custom.CustomLayoutScreen
import ru.vk.edu.composeadvenced.ui.custom.CustomModifierScreen
import ru.vk.edu.composeadvenced.ui.viewmodel.ViewModelScreen
import ru.vk.edu.composeadvenced.ui.animation.CertificatesStackScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.childStack,
        animation = predictiveBackAnimation(
            component.backHandler,
            fallbackAnimation = stackAnimation(fade() + slide()),
            onBack = component::onBackClicked
        )
    ) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Main -> MainScreen(instance.component)
            is RootComponent.Child.Animations -> AnimationsScreen(instance.component)
            // Анимации
            is RootComponent.Child.Animatable -> AnimatableScreen(instance.component)
            is RootComponent.Child.AnimateAsState -> AnimateAsStateScreen(instance.component)
            is RootComponent.Child.UpdateTransition -> UpdateTransitionScreen(instance.component)
            is RootComponent.Child.AnimatedVisibility -> AnimatedVisibilityScreen(instance.component)
            is RootComponent.Child.AnimatedContent -> AnimatedContentScreen(instance.component)
            is RootComponent.Child.Crossfade -> CrossfadeScreen(instance.component)
            is RootComponent.Child.AnimateContentSize -> AnimateContentSizeScreen(instance.component)
            is RootComponent.Child.AnchoredDraggable -> AnchoredDraggableScreen(instance.component)
            is RootComponent.Child.AnimateItemPlacement -> AnimateItemPlacementScreen(instance.component)
            is RootComponent.Child.CardStack -> CertificatesStackScreen(instance.component)
            // Кастомные компоненты
            is RootComponent.Child.CustomComponents -> CustomComponentsScreen(instance.component)
            is RootComponent.Child.CustomModifier -> CustomModifierScreen(instance.component)
            is RootComponent.Child.CustomDraw -> CustomDrawScreen(instance.component)
            is RootComponent.Child.CustomLayout -> CustomLayoutScreen(instance.component)
            is RootComponent.Child.ComplexCustomComponent -> ComplexCustomComponentScreen(instance.component)
            is RootComponent.Child.ViewModel -> ViewModelScreen(instance.component)
        }
    }
}

