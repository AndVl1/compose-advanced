package ru.vk.edu.composeadvenced.screens.viewmodel

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.serialization.Serializable

class ViewModelComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit
) : ComponentContext by componentContext {

    private val viewModel = instanceKeeper.getOrCreate { ViewModel() }
    
    // Создаем навигацию для слота
    private val commentSlotNavigation = SlotNavigation<CommentConfig>()
    
    // Создаем childSlot для комментариев
    val commentSlot: Value<ChildSlot<CommentConfig, CommentChild>> = childSlot(
        source = commentSlotNavigation,
        serializer = CommentConfig.serializer(),
        handleBackButton = true, // Автоматически обрабатывать кнопку "Назад"
        childFactory = ::createCommentChild
    )
    
    // Восстанавливаем состояние, если оно было сохранено
    init {
        stateKeeper.consume(
            SAVED_STATE_KEY, SavedState.serializer()
        )?.let { savedState ->
            viewModel.restore(savedState)
        }

        // Регистрируем функцию сохранения состояния
        stateKeeper.register(
            SAVED_STATE_KEY, SavedState.serializer()
        ) {
            viewModel.save()
        }
    }
    
    val state: Value<State> get() = viewModel.state
    
    fun onBackClick() {
        onBack()
    }
    
    fun incrementCounter(index: Int) {
        viewModel.incrementCounter(index)
    }
    
    fun decrementCounter(index: Int) {
        viewModel.decrementCounter(index)
    }
    
    fun updateText(text: String) {
        viewModel.updateText(text)
    }
    
    fun addCounter() {
        viewModel.addCounter()
    }
    
    fun removeCounter(index: Int) {
        viewModel.removeCounter(index)
    }
    
    // Методы для работы с комментариями через childSlot
    fun showCommentSheet() {
        commentSlotNavigation.activate(CommentConfig)
    }
    
    fun hideCommentSheet() {
        commentSlotNavigation.dismiss()
    }
    
    fun updateCommentText(text: String) {
        viewModel.updateCommentText(text)
    }
    
    fun addComment() {
        viewModel.addComment()
    }
    
    // Фабрика для создания дочернего компонента комментариев
    private fun createCommentChild(
        config: CommentConfig,
        componentContext: ComponentContext
    ): CommentChild {
        return CommentChild.Comments(
            CommentComponent(
                componentContext = componentContext,
                state = state,
                onDismiss = ::hideCommentSheet,
                onCommentTextChanged = ::updateCommentText,
                onAddComment = ::addComment
            )
        )
    }
    
    // Класс для хранения состояния
    data class State(
        val counters: List<Int> = listOf(0, 0, 0),
        val text: String = "",
        val commentText: String = "",
        val comments: List<String> = emptyList()
    )
    
    // Класс для сохранения состояния
    @Serializable
    private data class SavedState(
        val counters: List<Int>,
        val text: String
    )
    
    // Конфигурация для слота комментариев
    @Serializable
    data object CommentConfig
    
    // Дочерний компонент для слота комментариев
    sealed class CommentChild {
        data class Comments(val component: CommentComponent) : CommentChild()
    }

    // ViewModel, которая будет сохраняться в InstanceKeeper
    private class ViewModel : InstanceKeeper.Instance {
        private val _state = MutableValue(State())
        val state: Value<State> = _state
        
        fun incrementCounter(index: Int) {
            _state.update { currentState ->
                val newCounters = currentState.counters.toMutableList()
                if (index < newCounters.size) {
                    newCounters[index] = newCounters[index] + 1
                }
                currentState.copy(counters = newCounters)
            }
        }
        
        fun decrementCounter(index: Int) {
            _state.update { currentState ->
                val newCounters = currentState.counters.toMutableList()
                if (index < newCounters.size) {
                    newCounters[index] = newCounters[index] - 1
                }
                currentState.copy(counters = newCounters)
            }
        }
        
        fun updateText(text: String) {
            _state.update { it.copy(text = text) }
        }
        
        fun addCounter() {
            _state.update { currentState ->
                val newCounters = currentState.counters.toMutableList()
                newCounters.add(0)
                currentState.copy(counters = newCounters)
            }
        }
        
        fun removeCounter(index: Int) {
            _state.update { currentState ->
                val newCounters = currentState.counters.toMutableList()
                if (index < newCounters.size && newCounters.size > 1) {
                    newCounters.removeAt(index)
                }
                currentState.copy(counters = newCounters)
            }
        }
        
        // Методы для работы с комментариями
        fun updateCommentText(text: String) {
            _state.update { it.copy(commentText = text) }
        }
        
        fun addComment() {
            val commentText = _state.value.commentText.trim()
            if (commentText.isNotEmpty()) {
                _state.update { currentState ->
                    val newComments = currentState.comments.toMutableList()
                    newComments.add(commentText)
                    currentState.copy(
                        comments = newComments,
                        commentText = "" // Очищаем поле ввода после добавления
                    )
                }
            }
        }
        
        // Сохранение состояния
        fun save(): SavedState {
            return SavedState(
                counters = _state.value.counters,
                text = _state.value.text
            )
        }
        
        // Восстановление состояния
        fun restore(savedState: SavedState) {
            _state.update {
                it.copy(
                    counters = savedState.counters,
                    text = savedState.text
                )
            }
        }
        
        override fun onDestroy() {
            // Освобождаем ресурсы, если необходимо
        }
    }

    companion object {
        private const val SAVED_STATE_KEY = "saved_state"
    }
} 