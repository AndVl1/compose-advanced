# PagerIndicatorLazyListSimple - Руководство по реализации

## Обзор
`PagerIndicatorLazyListSimple` демонстрирует создание индикатора страниц с ограниченным видимым окном и автоматической прокруткой. Использует `LazyRow` для производительности и `animateScrollToItem` для плавной прокрутки.

## Пошаговая реализация

### Шаг 1: Импорты и константы
```kotlin
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
// ... другие импорты

private val BasePageIndicationSize = 8.dp
private val SpaceSize = 10.dp
```

**Важные импорты:**
- `LazyRow` - для эффективного отображения списка точек
- `LazyListState` - для контроля прокрутки
- `LaunchedEffect` - для реакции на изменения страницы

### Шаг 2: Data класс состояния
```kotlin
@Immutable
data class PagerIndicatorState(
    val pagerState: PagerState,
    val pageCount: Int,
    val indicationCount: Int = 3, // Количество видимых точек
)
```

**Параметры:**
- `indicationCount` - сколько точек показывать одновременно

### Шаг 3: Функция компонента
```kotlin
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicatorLazyListSimple(
    state: PagerIndicatorState,
    modifier: Modifier = Modifier,
    itemSpace: Dp = SpaceSize,
)
```

### Шаг 4: Инициализация состояния
```kotlin
val currentPage = state.pagerState.currentPage
val listState = rememberLazyListState()
val totalWidth = BasePageIndicationSize * state.indicationCount +
        itemSpace * (state.indicationCount - 1)
val activeColor = MaterialTheme.colorScheme.primary
val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
```

**Ключевые моменты:**
- `rememberLazyListState()` - хранит состояние прокрутки
- `totalWidth` - фиксированная ширина видимого окна
- Цвета берутся из системной темы Material3

### Шаг 5: Автоматическая прокрутка
```kotlin
LaunchedEffect(key1 = currentPage) {
    updateState(
        currentPage = currentPage,
        listState = listState,
        indicationCount = state.indicationCount
    )
}
```

**Работа эффекта:**
- Запускается при изменении `currentPage`
- Проверяет границы видимого окна
- Вызывает `animateScrollToItem` при необходимости
- Обрабатывает ошибки с подробным логированием

### Шаг 6: UI структура
```kotlin
Row(
    modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth(),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
) {
    Spacer(modifier = Modifier.widthIn(itemSpace / 2))
    LazyRow(
        state = listState,
        userScrollEnabled = false, // Отключаем пользовательскую прокрутку
        modifier = Modifier
            .widthIn(max = totalWidth) // Ограничиваем ширину
            .align(Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(itemSpace),
    ) {
        indicationItems(
            itemsCount = state.pageCount,
            currentItem = currentPage,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
        )
    }
}
```

**Важные настройки:**
- `userScrollEnabled = false` - пользователь не может прокручивать вручную
- `widthIn(max = totalWidth)` - ограничивает видимую область
- `Arrangement.spacedBy(itemSpace)` - равномерные отступы между элементами

### Шаг 7: Функция обновления состояния
```kotlin
private suspend fun updateState(
    currentPage: Int,
    listState: LazyListState,
    indicationCount: Int,
) {
    if (currentPage >= 0) {
        val visibleItems = listState.layoutInfo.visibleItemsInfo.filter { it.offset >= 0 }
        val firstVisibleItemIndex = visibleItems.firstOrNull()?.index ?: 0
        val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0

        try {
            if (currentPage < firstVisibleItemIndex) {
                // Страница ушла влево за границу - прокручиваем к началу
                listState.animateScrollToItem(currentPage)
            } else if (currentPage > lastVisibleItemIndex) {
                // Страница ушла вправо за границу - прокручиваем окно
                listState.animateScrollToItem(currentPage - (indicationCount - 1))
            }
        } catch (ex: IllegalArgumentException) {
            // Обработка ошибок с логированием
            Log.e("PagerIndicator", "Failed to animate scroll", ex)
        }
    }
}
```

**Логика прокрутки:**
1. **Получаем видимые элементы** - только те, что не обрезаны (`offset >= 0`)
2. **Проверяем границы** - находится ли активная страница в видимой области
3. **Прокручиваем при необходимости**:
   - Влево: `animateScrollToItem(currentPage)` - показываем активную страницу слева
   - Вправо: `animateScrollToItem(currentPage - (indicationCount - 1))` - активная страница справа
4. **Обработка ошибок** - try/catch с подробным логированием для отладки edge cases

### Шаг 8: Отрисовка точек
```kotlin
private fun LazyListScope.indicationItems(
    itemsCount: Int,
    currentItem: Int,
    activeColor: Color,
    inactiveColor: Color,
) {
    items(itemsCount) { index ->
        val isSelected = (index == currentItem)
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(BasePageIndicationSize)
                .background(if (isSelected) activeColor else inactiveColor)
        )
    }
}
```

**Особенности:**
- `LazyListScope.items()` - создает элементы лениво
- Каждый элемент проверяет `isSelected` для выбора цвета
- Простое переключение цвета без анимации

## Математика позиционирования

### Сценарий прокрутки вправо:
```
Видимые индексы: [2, 3, 4] (indicationCount = 3)
Текущая страница: 5
Действие: animateScrollToItem(5 - (3 - 1)) = animateScrollToItem(3)
Результат: [3, 4, 5] - активная страница 5 теперь видна справа
```

### Сценарий прокрутки влево:
```
Видимые индексы: [3, 4, 5]
Текущая страница: 2
Действие: animateScrollToItem(2)
Результат: [2, 3, 4] - активная страница 2 видна слева
```

## Особенности реализации

### ✅ Плюсы:
- **Производительность**: `LazyRow` создает только видимые элементы
- **Overflow management**: автоматически управляет переполнением
- **Плавная прокрутка**: `animateScrollToItem` обеспечивает плавную анимацию
- **Граничная логика**: прокрутка только при выходе за границы
- **Масштабируемость**: работает с любым количеством страниц

### ⚠️ Минусы:
- **Дискретная анимация**: активная точка "прыгает" между позициями
- **Нет interpolation**: не отслеживает `currentPageOffsetFraction`
- **Сложность логики**: требует понимания `LazyListState`

## Когда использовать
- **Большое количество страниц** (10+)
- **Ограниченное пространство** экрана
- **Нужна производительность** при многих элементах  
- **Приемлема дискретная анимация** без плавного следования за свайпом

## Альтернативы
- `PagerAnimatedIndicator` - для плавной анимации без ограничений
- `PagerCustomLayoutIndicator` - для полного контроля Layout
