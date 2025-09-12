# updateTransition

## Описание
updateTransition - это API для создания анимаций перехода между состояниями в Jetpack Compose. Он позволяет анимировать несколько значений одновременно при изменении состояния.

## Основные характеристики
- Группирует несколько анимаций, связанных с одним состоянием
- Обеспечивает синхронизацию анимаций при изменении состояния
- Позволяет определять различные спецификации анимации для каждого значения
- Поддерживает типизированные состояния (enum, sealed class и др.)

## Когда использовать
- Когда нужно анимировать несколько свойств при изменении одного состояния
- Для создания сложных переходов между различными состояниями UI
- Когда требуется синхронизация нескольких анимаций
- Для реализации анимированных компонентов с несколькими состояниями

## Пример использования
```kotlin
enum class BoxState { Small, Large }
var currentState by remember { mutableStateOf(BoxState.Small) }

val transition = updateTransition(
    targetState = currentState,
    label = "Box Transition"
)

val size by transition.animateDp(
    transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy) },
    label = "size"
) { state ->
    when (state) {
        BoxState.Small -> 64.dp
        BoxState.Large -> 128.dp
    }
}

val color by transition.animateColor(
    label = "color"
) { state ->
    when (state) {
        BoxState.Small -> MaterialTheme.colorScheme.primary
        BoxState.Large -> MaterialTheme.colorScheme.secondary
    }
}

Box(
    modifier = Modifier
        .size(size)
        .background(color)
)

Button(onClick = { 
    currentState = when (currentState) {
        BoxState.Small -> BoxState.Large
        BoxState.Large -> BoxState.Small
    }
}) {
    Text("Изменить состояние")
}
```

## Преимущества
- Синхронизация нескольких анимаций
- Типобезопасное определение состояний
- Декларативный подход к определению анимаций
- Возможность использования различных спецификаций для разных свойств

## Ограничения
- Более сложный в использовании, чем animate*asState
- Все анимации привязаны к одному состоянию
- Требует более структурированного подхода к организации кода 