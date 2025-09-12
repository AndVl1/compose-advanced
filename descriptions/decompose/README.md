# Decompose в Jetpack Compose

Decompose - это библиотека для организации архитектуры приложения и навигации в мультиплатформенных Kotlin-приложениях. В контексте Jetpack Compose, Decompose предоставляет мощные инструменты для управления навигацией и состоянием компонентов.

## Основные концепции Decompose

### Component

Component - это основной строительный блок в Decompose. Компоненты инкапсулируют логику и состояние части пользовательского интерфейса. Они могут содержать другие компоненты, образуя иерархию.

```kotlin
class SomeComponent(
    componentContext: ComponentContext,
    // другие зависимости
) : ComponentContext by componentContext {
    // Логика компонента
}
```

### ComponentContext

ComponentContext предоставляет доступ к различным возможностям Decompose:

- **StateKeeper** - для сохранения и восстановления состояния
- **InstanceKeeper** - для сохранения экземпляров объектов (например, ViewModel)
- **BackHandler** - для обработки нажатия кнопки "Назад"
- **Lifecycle** - для отслеживания жизненного цикла компонента

### Navigation

Decompose предоставляет несколько типов навигации:

- **Stack** - стек экранов (как в обычной навигации Android)
- **Slot** - слот для одного экрана (как в модальных окнах)
- **Child** - простой дочерний компонент

## Сохранение состояния в Decompose

Decompose предоставляет два основных механизма для сохранения состояния:

### StateKeeper

StateKeeper используется для сохранения и восстановления состояния при уничтожении и пересоздании компонента (например, при повороте экрана).

```kotlin
// Регистрация функции сохранения состояния
stateKeeper.register("key", Serializer) { /* Возвращает Serializable объект */ }

// Восстановление состояния
val savedState = stateKeeper.consume("key", Serializer)
```

### InstanceKeeper

InstanceKeeper используется для сохранения экземпляров объектов (например, ViewModel) при уничтожении и пересоздании компонента.

```kotlin
// Получение или создание экземпляра
val viewModel = instanceKeeper.getOrCreate { ViewModel() }
```

## Пример использования ViewModel с Decompose

В нашем примере мы создали компонент ViewModelComponent, который демонстрирует использование ViewModel с Decompose:

1. **Создание ViewModel**:
   ```kotlin
   private val viewModel = instanceKeeper.getOrCreate { ViewModel() }
   ```

2. **Сохранение состояния**:
   ```kotlin
   stateKeeper.register { viewModel.save() }
   ```

3. **Восстановление состояния**:
   ```kotlin
   stateKeeper.consume<SavedState>()?.let { savedState ->
       viewModel.restore(savedState)
   }
   ```

4. **Обновление состояния**:
   ```kotlin
   _state.update { currentState ->
       currentState.copy(text = text)
   }
   ```

5. **Подписка на состояние в UI**:
   ```kotlin
   val state by component.state.subscribeAsState()
   ```

## Модальные окна с использованием childSlot

Decompose предоставляет механизм `childSlot` для управления модальными окнами. В отличие от простого флага видимости, `childSlot` позволяет создавать полноценные компоненты для модальных окон, которые могут иметь собственное состояние и логику.

1. **Создание SlotNavigation и childSlot**:
   ```kotlin
   private val commentSlotNavigation = SlotNavigation<CommentConfig>()
   
   val commentSlot: Value<ChildSlot<CommentConfig, CommentChild>> = childSlot(
       source = commentSlotNavigation,
       serializer = CommentConfig.serializer(),
       handleBackButton = true,
       childFactory = ::createCommentChild
   )
   ```

2. **Активация и закрытие модального окна**:
   ```kotlin
   // Показать модальное окно
   commentSlotNavigation.activate(CommentConfig)
   
   // Закрыть модальное окно
   commentSlotNavigation.dismiss()
   ```

3. **Создание компонента для модального окна**:
   ```kotlin
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
   ```

4. **Отображение модального окна в UI**:
   ```kotlin
   childState.child?.instance?.let { modal ->
       when (modal) {
           is ViewModelComponent.CommentChild.Comments -> CommentBottomSheet(modal.component)
       }
   }
   ```

Преимущества использования `childSlot` для модальных окон:
- Автоматическая обработка кнопки "Назад"
- Возможность создания полноценных компонентов с собственным состоянием
- Типобезопасность и предсказуемость поведения
- Интеграция с системой навигации Decompose

## Преимущества использования Decompose

1. **Предсказуемое управление состоянием** - состояние сохраняется и восстанавливается автоматически
2. **Типобезопасная навигация** - навигация основана на типах, что предотвращает ошибки во время выполнения
3. **Многоплатформенность** - работает на всех платформах Kotlin Multiplatform
4. **Интеграция с Jetpack Compose** - предоставляет удобные расширения для работы с Compose
5. **Поддержка анимаций** - встроенная поддержка анимаций при навигации
 
