package ru.vk.edu.composeadvenced.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import ru.vk.edu.composeadvenced.navigation.Configuration
import ru.vk.edu.composeadvenced.screens.AnimationsComponent
import ru.vk.edu.composeadvenced.screens.CustomComponentsComponent
import ru.vk.edu.composeadvenced.screens.MainComponent
import ru.vk.edu.composeadvenced.screens.animation.*
import ru.vk.edu.composeadvenced.screens.custom.*
import ru.vk.edu.composeadvenced.screens.viewmodel.ViewModelComponent

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, BackHandlerOwner {

    private val navigation = StackNavigation<Configuration>()
    
    private val stack = childStack(
        source = navigation,
        initialStack = { listOf(Configuration.Main) },
        serializer = Configuration.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    )

    val childStack: Value<ChildStack<*, Child>> = stack

    fun onBackClicked() {
        navigation.pop()
    }

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): Child = when (configuration) {
        is Configuration.Main -> Child.Main(
            MainComponent(
                componentContext = componentContext,
                onNavigateToAnimations = { navigation.pushNew(Configuration.Animations) },
                onNavigateToCustomComponents = { navigation.pushNew(Configuration.CustomComponents) },
                onNavigateToViewModelClick = { navigation.pushNew(Configuration.ViewModel) }
            )
        )
        is Configuration.Animations -> Child.Animations(
            AnimationsComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() },
                onNavigateToAnimatable = { navigation.pushNew(Configuration.AnimatableScreen) },
                onNavigateToAnimateAsState = { navigation.pushNew(Configuration.AnimateAsStateScreen) },
                onNavigateToUpdateTransition = { navigation.pushNew(Configuration.UpdateTransitionScreen) },
                onNavigateToAnimatedVisibility = { navigation.pushNew(Configuration.AnimatedVisibilityScreen) },
                onNavigateToAnimatedContent = { navigation.pushNew(Configuration.AnimatedContentScreen) },
                onNavigateToCrossfade = { navigation.pushNew(Configuration.CrossfadeScreen) },
                onNavigateToAnimateContentSize = { navigation.pushNew(Configuration.AnimateContentSizeScreen) },
                onNavigateToAnchoredDraggable = { navigation.pushNew(Configuration.AnchoredDraggableScreen) },
                onNavigateToAnimateItemPlacement = { navigation.pushNew(Configuration.AnimateItemPlacementScreen) },
                onNavigateToCardStack = { navigation.pushNew(Configuration.CardStackScreen) }
            )
        )
        is Configuration.AnimatableScreen -> Child.Animatable(
            AnimatableComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.AnimateAsStateScreen -> Child.AnimateAsState(
            AnimateAsStateComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.UpdateTransitionScreen -> Child.UpdateTransition(
            UpdateTransitionComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.AnimatedVisibilityScreen -> Child.AnimatedVisibility(
            AnimatedVisibilityComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.AnimatedContentScreen -> Child.AnimatedContent(
            AnimatedContentComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.CrossfadeScreen -> Child.Crossfade(
            CrossfadeComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.AnimateContentSizeScreen -> Child.AnimateContentSize(
            AnimateContentSizeComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.AnchoredDraggableScreen -> Child.AnchoredDraggable(
            AnchoredDraggableComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.AnimateItemPlacementScreen -> Child.AnimateItemPlacement(
            AnimateItemPlacementComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.CardStackScreen -> Child.CardStack(
            CertificatesStackComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.CustomComponents -> Child.CustomComponents(
            CustomComponentsComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() },
                onNavigateToCustomModifier = { navigation.push(Configuration.CustomModifierScreen) },
                onNavigateToCustomDraw = { navigation.push(Configuration.CustomDrawScreen) },
                onNavigateToCustomLayout = { navigation.push(Configuration.CustomLayoutScreen) },
                onNavigateToComplexCustomComponent = { navigation.push(Configuration.ComplexCustomComponentScreen) }
            )
        )
        is Configuration.CustomModifierScreen -> Child.CustomModifier(
            CustomModifierComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.CustomDrawScreen -> Child.CustomDraw(
            CustomDrawComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.CustomLayoutScreen -> Child.CustomLayout(
            CustomLayoutComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.ComplexCustomComponentScreen -> Child.ComplexCustomComponent(
            ComplexCustomComponentComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
        is Configuration.ViewModel -> Child.ViewModel(
            ViewModelComponent(
                componentContext = componentContext,
                onBack = { navigation.pop() }
            )
        )
    }

    sealed class Child {
        data class Main(val component: MainComponent) : Child()
        data class Animations(val component: AnimationsComponent) : Child()
        data class Animatable(val component: AnimatableComponent) : Child()
        data class AnimateAsState(val component: AnimateAsStateComponent) : Child()
        data class UpdateTransition(val component: UpdateTransitionComponent) : Child()
        data class AnimatedVisibility(val component: AnimatedVisibilityComponent) : Child()
        data class AnimatedContent(val component: AnimatedContentComponent) : Child()
        data class Crossfade(val component: CrossfadeComponent) : Child()
        data class AnimateContentSize(val component: AnimateContentSizeComponent) : Child()
        data class AnchoredDraggable(val component: AnchoredDraggableComponent) : Child()
        data class AnimateItemPlacement(val component: AnimateItemPlacementComponent) : Child()
        data class CardStack(val component: CertificatesStackComponent) : Child()
        data class CustomComponents(val component: CustomComponentsComponent) : Child()
        data class CustomModifier(val component: CustomModifierComponent) : Child()
        data class CustomDraw(val component: CustomDrawComponent) : Child()
        data class CustomLayout(val component: CustomLayoutComponent) : Child()
        data class ComplexCustomComponent(val component: ComplexCustomComponentComponent) : Child()
        data class ViewModel(val component: ViewModelComponent) : Child()
    }
} 
