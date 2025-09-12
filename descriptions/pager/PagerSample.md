# PagerSample - Демонстрация различных индикаторов

## Обзор
`pagerSample.kt` содержит основной экран демонстрации различных реализаций индикаторов для HorizontalPager. 

## Структура файла

### Основной компонент
```kotlin
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(
    pagerIndicatorComponent: PagerIndicatorComponent
) {
    val pagerState = rememberPagerState(pageCount = { 20 })
    
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Pager Indicator",
                showBackButton = true,
                onBackClick = { pagerIndicatorComponent.onBackClicked() }
            )
        }
    ) { paddingValues ->
        // Контент экрана
    }
}
```

### Конфигурация пейджера
```kotlin
val pagerState = rememberPagerState(pageCount = { 20 })
```
- **20 страниц** для демонстрации прокрутки
- `rememberPagerState` для сохранения состояния

### HorizontalPager реализация
```kotlin
HorizontalPager(
    state = pagerState,
    modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
) { page ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Page ${page + 1}",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}
```

**Особенности страниц:**
- Серый фон с закругленными углами
- Белый текст с номером страницы
- Высота 150dp для компактности
- Material3 typography.headlineMedium

## Демонстрируемые индикаторы

### 1. PagerAnimatedIndicator
```kotlin
PagerAnimatedIndicator(
    pagerState = pagerState,
    pageCount = pagerState.pageCount,
)
```
- Анимированная активная точка поверх статичных
- Box + Row архитектура
- Плавное следование за свайпом

### 2. PagerIndicatorLazyListSimple
```kotlin
PagerIndicatorLazyListSimple(
    state = PagerIndicatorState(
        pagerState = pagerState,
        pageCount = pagerState.pageCount,
        indicationCount = 3
    )
)
```
- LazyRow с ограниченным видимым окном
- Автоматическая прокрутка при boundary crossing
- Показ только 3 точек одновременно

### 3. PagerCustomLayoutIndicator
```kotlin
PagerCustomLayoutIndicator(
    pagerState = pagerState,
    pageCount = pagerState.pageCount,
)
```
- Custom Layout API для точного позиционирования
- Полный контроль над размещением элементов
- Простая реализация без overflow management

## Финальная структура

### Layout композиции
```kotlin
Scaffold(topBar = { AppToolbar }) { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // HorizontalPager
        HorizontalPager(state = pagerState) { /* pages */ }
        
        // Заголовки и индикаторы
        SectionTitle("Animated Indicator")
        PagerAnimatedIndicator(pagerState, pageCount)
        
        SectionTitle("Lazy List Simple (3 visible)")  
        PagerIndicatorLazyListSimple(state)
        
        SectionTitle("Custom Layout Indicator")
        PagerCustomLayoutIndicator(pagerState, pageCount)
    }
}
```

### Вспомогательные компоненты
```kotlin
@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
}
```

## Архитектурные решения

### ✅ Преимущества рефакторинга:
- **Стандартные зависимости** - только Material3 и Compose
- **Простота понимания** - убрана domain-specific логика VK
- **Фокус на демонстрации** - чистое showcasing индикаторов
- **Переносимость** - код работает в любом Compose проекте

### 🎯 Цели демонстрации:
- Показать различные подходы к реализации индикаторов
- Сравнить производительность и UX разных решений
- Продемонстрировать интеграцию с HorizontalPager
- Обучить выбору подходящего решения

## Интеграция с навигацией

### PagerIndicatorComponent
```kotlin
class PagerIndicatorComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit
) : ComponentContext by componentContext {
    
    fun onBackClicked() {
        onBack()
    }
}
```

### Decompose интеграция
```kotlin
// В RootComponent
is Configuration.PagerIndicator -> Child.PagerIndicator(
    PagerIndicatorComponent(
        componentContext = componentContext,
        onBack = { navigation.pop() }
    )
)
```

## Рекомендации по использованию

### Когда использовать каждый индикатор:

1. **PagerAnimatedIndicator**
   - Малое количество страниц (до 8)
   - Нужна плавная анимация
   - Простота важнее оптимизации

2. **PagerIndicatorLazyListSimple**  
   - Большое количество страниц (10+)
   - Ограниченное пространство
   - Нужна прокрутка индикатора

3. **PagerCustomLayoutIndicator**
   - Специфические требования к layout
   - Нужен полный контроль позиционирования
   - Custom анимации и эффекты
