package ru.vk.edu.composeadvenced.screens.viewmodel

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

class CommentComponent(
    componentContext: ComponentContext,
    val state: Value<ViewModelComponent.State>,
    val onDismiss: () -> Unit,
    val onCommentTextChanged: (String) -> Unit,
    val onAddComment: () -> Unit
) : ComponentContext by componentContext
