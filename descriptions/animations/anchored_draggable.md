# AnchoredDraggable

## Описание
AnchoredDraggable - это API для создания перетаскиваемых элементов с фиксированными позициями (якорями) в Jetpack Compose. Он позволяет определить несколько позиций, между которыми пользователь может перетаскивать элемент, с автоматической анимацией к ближайшему якорю при отпускании.

## Основные характеристики
- Создает перетаскиваемый элемент с фиксированными позициями
- Автоматически анимирует к ближайшему якорю при отпускании
- Поддерживает программное управление позицией
- Предоставляет информацию о текущем состоянии перетаскивания
- Работает как в горизонтальном, так и в вертикальном направлении

## Когда использовать
- Для создания свайпабельных элементов с фиксированными позициями
- При реализации жестов перетаскивания с привязкой к определенным точкам
- Для создания интерактивных элементов управления (слайдеры, переключатели)
- Когда нужно реализовать свайп с действиями (например, свайп элемента списка для удаления)

## Пример использования
```kotlin
val density = LocalDensity.current
val scope = rememberCoroutineScope()

val dragState = remember {
    AnchoredDraggableState(
        initialValue = DragAnchors.Center,
        positionalThreshold = { distance -> distance * 0.5f },
        velocityThreshold = { 100f },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    ).apply {
        updateAnchors(
            DraggableAnchors {
                DragAnchors.Start at -100f
                DragAnchors.Center at 0f
                DragAnchors.End at 100f
            }
        )
    }
}

Box(
    modifier = Modifier
        .offset {
            IntOffset(
                x = dragState.offset.roundToInt(),
                y = 0
            )
        }
        .size(60.dp)
        .background(MaterialTheme.colorScheme.primary)
        .anchoredDraggable(
            state = dragState,
            orientation = Orientation.Horizontal
        )
)

// Программное управление
Button(onClick = {
    scope.launch {
        dragState.animateTo(DragAnchors.Start)
    }
}) {
    Text("К началу")
}
```

## Преимущества
- Создает интуитивно понятные интерактивные элементы
- Автоматически анимирует к ближайшему якорю
- Поддерживает как жесты пользователя, так и программное управление
- Предоставляет гибкие возможности настройки

## Ограничения
- Более сложный в настройке, чем базовые жесты
- Требует явного определения якорей
- Может требовать дополнительной логики для обработки действий
- Необходимо внимательно управлять состоянием для предотвращения проблем 