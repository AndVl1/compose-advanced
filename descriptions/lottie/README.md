# Lottie Animations - Руководство по реализации

## Обзор
`LottieAnimationsScreen` демонстрирует интеграцию Lottie анимаций в Jetpack Compose с различными параметрами управления воспроизведением. Показывает примеры использования `lottie-compose` библиотеки с разными скоростями, количеством повторов и интерактивными элементами управления.

## Структура реализации

### Компоненты
- **LottieAnimationsScreen** - основной экран с LazyColumn списком анимаций
- **LottieAnimationCard** - карточка с одной анимацией и элементами управления
- **LottieAnimationItem** - data class для параметров анимации

## Пошаговая реализация

### Шаг 1: Добавление зависимости

```kotlin
// app/build.gradle.kts
implementation("com.airbnb.android:lottie-compose:6.1.0")
```

### Шаг 2: Data класс для анимаций

```kotlin
data class LottieAnimationItem(
    val title: String,
    val description: String,
    val url: String,
    val isPlaying: Boolean = true,
    val speed: Float = 1f,
    val iterations: Int = LottieConstants.IterateForever,
    val backgroundColor: Color = Color.Transparent
)
```

**Параметры:**
- `title` - название анимации
- `description` - описание для пользователя
- `url` - ссылка на JSON файл анимации
- `isPlaying` - начальное состояние воспроизведения
- `speed` - скорость анимации (0.5f = медленно, 2f = быстро)
- `iterations` - количество повторов
- `backgroundColor` - фон для контраста

### Шаг 3: Основной экран

```kotlin
@Composable
fun LottieAnimationsScreen(component: LottieAnimationsComponent) {
    val animationItems = remember {
        listOf(
            LottieAnimationItem(
                title = "Loading Animation",
                description = "Стандартная анимация загрузки с обычной скоростью",
                url = "https://raw.githubusercontent.com/airbnb/lottie-android/refs/heads/master/sample/src/main/assets/Lottie%20Logo%201.json",
                speed = 1f
            ),
            // ... другие анимации
        )
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Lottie Animations",
                showBackButton = true,
                onBackClick = { component.onBackClicked() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(animationItems) { item ->
                LottieAnimationCard(item)
            }
        }
    }
}
```

### Шаг 4: Карточка с анимацией

```kotlin
@Composable
fun LottieAnimationCard(item: LottieAnimationItem) {
    var isPlaying by remember { mutableStateOf(item.isPlaying) }
    
    Card(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Контейнер анимации
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(item.backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Url(item.url)
                )
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    isPlaying = isPlaying,
                    speed = item.speed,
                    iterations = item.iterations,
                    restartOnPlay = false
                )
                
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(120.dp)
                )
            }
            
            // Информационная секция с элементами управления
            // ...
        }
    }
}
```

## Ключевые функции Lottie Compose

### 1. Загрузка композиции
```kotlin
val composition by rememberLottieComposition(
    LottieCompositionSpec.Url(item.url)  // Загрузка из URL
)
```

**Альтернативы:**
- `LottieCompositionSpec.Asset("animation.json")` - из assets
- `LottieCompositionSpec.RawRes(R.raw.animation)` - из raw ресурсов

### 2. Управление анимацией
```kotlin
val progress by animateLottieCompositionAsState(
    composition = composition,
    isPlaying = isPlaying,
    speed = item.speed,           // Скорость воспроизведения
    iterations = item.iterations, // Количество повторов
    restartOnPlay = false        // Не перезапускать при возобновлении
)
```

### 3. Отображение анимации
```kotlin
LottieAnimation(
    composition = composition,
    progress = { progress },  // Лямбда для динамического прогресса
    modifier = Modifier.size(120.dp)
)
```

## Примеры анимаций в коде

### 1. Стандартная загрузка
```kotlin
LottieAnimationItem(
    title = "Loading Animation",
    url = "https://raw.githubusercontent.com/airbnb/lottie-android/refs/heads/master/sample/src/main/assets/Lottie%20Logo%201.json",
    speed = 1f
)
```

### 2. Ускоренная анимация
```kotlin
LottieAnimationItem(
    title = "Fast Loading", 
    url = "https://raw.githubusercontent.com/airbnb/lottie-android/refs/heads/master/sample/src/main/assets/Lottie%20Logo%202.json",
    speed = 2f  // В 2 раза быстрее
)
```

### 3. Замедленная анимация
```kotlin
LottieAnimationItem(
    title = "Slow Motion",
    url = "https://raw.githubusercontent.com/airbnb/lottie-android/refs/heads/master/sample/src/main/assets/Lottie%20Logo%201.json",
    speed = 0.5f  // В 2 раза медленнее
)
```

### 4. Ограниченные повторы
```kotlin
LottieAnimationItem(
    title = "Calendar",
    url = "https://raw.githubusercontent.com/thesvbd/Lottie-examples/refs/heads/master/assets/animations/calendar.json",
    iterations = 5  // Только 5 повторов
)
```

## Интерактивные элементы управления

### Play/Pause кнопка
```kotlin
Button(
    onClick = { isPlaying = !isPlaying },
    modifier = Modifier.height(32.dp)
) {
    Text(
        text = if (isPlaying) "Pause" else "Play",
        style = MaterialTheme.typography.labelMedium
    )
}
```

### Отображение параметров
```kotlin
Column {
    Text(
        text = "Speed: ${item.speed}x",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary
    )
    Text(
        text = if (item.iterations == LottieConstants.IterateForever) 
            "∞ loops" else "${item.iterations} loop(s)",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary
    )
}
```

## Архитектура навигации

### Component
```kotlin
class LottieAnimationsComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit
) : ComponentContext by componentContext {
    
    fun onBackClicked() {
        onBack()
    }
}
```

### Интеграция в RootComponent
```kotlin
is Configuration.LottieAnimations -> Child.LottieAnimations(
    LottieAnimationsComponent(
        componentContext = componentContext,
        onBack = { navigation.pop() }
    )
)
```

## Особенности реализации

### ✅ Преимущества:
- **Библиотека от Airbnb** - стабильная и широко используемая
- **Интерактивность** - Play/Pause управление для каждой анимации
- **Различные параметры** - демонстрация speed, iterations, фонов
- **Онлайн загрузка** - поддержка URL для Lottie файлов
- **Material 3 дизайн** - современный внешний вид карточек

### ⚠️ Ограничения:
- **Зависимость от сети** - анимации загружаются онлайн
- **Размер карточек** - фиксированная высота 280dp
- **Обработка ошибок** - нет fallback для недоступных URL

## Когда использовать
- **Демонстрация возможностей** Lottie в Compose приложениях
- **Обучение** интеграции анимаций с различными параметрами
- **Прототипирование** UI с rich анимациями
- **Тестирование** производительности Lottie анимаций

## Дополнительные возможности
- Кэширование композиций для оффлайн использования
- Добавление слайдеров для динамического изменения speed
- Интеграция с локальными assets файлами
- Поддержка темной/светлой темы для анимаций