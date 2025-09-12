# PagerAnimatedIndicator - Руководство по реализации

## Обзор
`PagerAnimatedIndicator` демонстрирует создание анимированного индикатора страниц с плавным движением активной точки. Использует архитектуру `Box` + `Row` где статичные неактивные точки располагаются в `Row`, а активная анимированная точка движется поверх них с помощью `offset`.

## Пошаговая реализация

### Шаг 1: Импорты и константы
```kotlin
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
// ... другие импорты

private val BasePageIndicationSize = 8.dp
private val SpaceSize = 10.dp
```

**Что важно:**
- `animateFloatAsState` - для плавной анимации позиции
- `tween` - для настройки анимации
- Константы для размеров точек и отступов

### Шаг 2: Функция компонента
```kotlin
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerAnimatedIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    itemSpace: Dp = SpaceSize,
)
```

**Параметры:**
- `pagerState` - состояние пейджера для отслеживания текущей страницы
- `pageCount` - общее количество страниц
- `itemSpace` - расстояние между точками

### Шаг 3: Настройка анимации
```kotlin
val density = LocalDensity.current
val activeColor = MaterialTheme.colorScheme.primary
val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

// Вычисление анимированного смещения
// val offset = (pagerState.currentPage + pagerState.currentPageOffsetFraction) * (BasePageIndicationSize + itemSpace).toPx()
// будет работать аналогично, если оставить duration анимации в спеке == 0
val animatedOffset by animateFloatAsState(
    targetValue = with(density) {
        (pagerState.currentPage + pagerState.currentPageOffsetFraction) * 
        (BasePageIndicationSize + itemSpace).toPx()
    },
    animationSpec = tween(durationMillis = 0),
    label = "indicatorOffset"
)
```

**Ключевые моменты:**
- `LocalDensity.current` - для конвертации Dp в Px
- `currentPage + currentPageOffsetFraction` - точное положение с учетом свайпа
- `tween(durationMillis = 0)` - мгновенная анимация для отслеживания свайпа
- Умножение на `(размер_точки + отступ)` для правильного позиционирования

### Шаг 4: Создание UI структуры
```kotlin
Box(
    modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth()
) {
    // Статичные точки
    Row(
        modifier = Modifier.align(Alignment.Center),
        horizontalArrangement = Arrangement.spacedBy(itemSpace),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(BasePageIndicationSize)
                    .background(inactiveColor)
            )
        }
    }

    // Анимированная активная точка
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .offset(
                x = with(density) { 
                    animatedOffset.toDp() - 
                    (pageCount - 1) * (BasePageIndicationSize + itemSpace) / 2 
                }
            )
            .clip(CircleShape)
            .size(BasePageIndicationSize)
            .background(activeColor)
    )
}
```

**Архитектура:**
1. **Box** - контейнер для наложения элементов
2. **Row** - размещение статичных неактивных точек
3. **Box с offset** - активная точка, которая движется поверх статичных

### Шаг 5: Расчет позиционирования
```kotlin
// Смещение для центрирования
val centeringOffset = (pageCount - 1) * (BasePageIndicationSize + itemSpace) / 2
val finalOffset = animatedOffset.toDp() - centeringOffset
```

**Объяснение математики:**
- `animatedOffset` - абсолютная позиция от 0 до (pageCount-1) * dotWidth
- `centeringOffset` - смещение для центрирования всего ряда точек
- Результат: активная точка движется относительно центрированного ряда

### Шаг 6: Вспомогательная функция
```kotlin
private operator fun Int.times(other: Dp): Dp = other * this
```
**Назначение:** Упрощение умножения `Int * Dp`

## Особенности реализации

### ✅ Плюсы:
- **Простая архитектура**: понятная Box + Row структура
- **Плавная анимация**: активная точка следует за пальцем при свайпе
- **Показывает все точки**: нет ограничений по количеству страниц
- **Material Design**: использует системные цвета темы

### ⚠️ Минусы:
- **Нет overflow management**: при большом количестве страниц может выходить за границы экрана
- **Фиксированная ширина**: занимает место всех точек сразу
- **Производительность**: создает все точки одновременно

## Когда использовать
- **Малое количество страниц** (до 7-8)
- **Нужна простая реализация** без сложной логики прокрутки
- **Важна плавность анимации** при свайпе
- **Не критичен overflow** за границы экрана

## Альтернативы
- `PagerIndicatorLazyListSimple` - для большого количества страниц с прокруткой
- `PagerCustomLayoutIndicator` - для полного контроля над размещением элементов
