# Shared Element Transitions - Руководство по реализации

## Обзор
`SimpleSharedElementScreen` демонстрирует реализацию Shared Element Transitions в Jetpack Compose без использования Decompose навигации. Показывает плавные переходы между экраном галереи изображений и детальным просмотром, используя чистый подход из официальной документации Android.

## Архитектура решения

### Основные компоненты
- **SimpleSharedElementScreen** - корневой компонент с состоянием навигации
- **SimpleImageListScreen** - экран галереи с сеткой изображений
- **SimpleImageDetailScreen** - экран детального просмотра
- **SharedElementScreen** - sealed class для состояний навигации

## Пошаговая реализация

### Шаг 1: Состояние навигации

```kotlin
sealed class SharedElementScreen {
    object List : SharedElementScreen()
    data class Detail(val imageId: String, val title: String) : SharedElementScreen()
}
```

**Преимущества sealed class:**
- Типобезопасная навигация
- Compile-time проверки
- Передача данных между экранами

### Шаг 2: Корневой компонент с SharedTransitionLayout

```kotlin
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SimpleSharedElementScreen(component: SharedElementListComponent) {
    var currentScreen by remember { 
        mutableStateOf<SharedElementScreen>(SharedElementScreen.List) 
    }
    
    SharedTransitionLayout {
        AnimatedContent(
            targetState = currentScreen,
            label = "SharedElementTransition"
        ) { screen ->
            when (screen) {
                is SharedElementScreen.List -> {
                    SimpleImageListScreen(
                        onImageClick = { imageId, title ->
                            currentScreen = SharedElementScreen.Detail(imageId, title)
                        },
                        onBackClick = { component.onBackClicked() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )
                }
                is SharedElementScreen.Detail -> {
                    SimpleImageDetailScreen(
                        imageId = screen.imageId,
                        title = screen.title,
                        onBackClick = { currentScreen = SharedElementScreen.List },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )
                }
            }
        }
    }
}
```

**Ключевые элементы:**
- `SharedTransitionLayout` - корневой контейнер для shared элементов
- `AnimatedContent` - обеспечивает AnimatedVisibilityScope
- Передача scope'ов в дочерние компоненты

### Шаг 3: Экран галереи изображений

```kotlin
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SimpleImageListScreen(
    onImageClick: (String, String) -> Unit,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: androidx.compose.animation.AnimatedVisibilityScope
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = "Shared Element Gallery",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(ImageData.sampleImages) { image ->
                with(sharedTransitionScope) {
                    SimpleImageCard(
                        image = image,
                        animatedVisibilityScope = animatedVisibilityScope,
                        onClick = { onImageClick(image.id, image.title) }
                    )
                }
            }
        }
    }
}
```

### Шаг 4: Карточка изображения с shared elements

```kotlin
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SimpleImageCard(
    image: ImageItem,
    animatedVisibilityScope: androidx.compose.animation.AnimatedVisibilityScope,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = image.imageUrl,
                contentDescription = image.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image-${image.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = image.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(key = "title-${image.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = image.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
```

### Шаг 5: Экран детального просмотра

```kotlin
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SimpleImageDetailScreen(
    imageId: String,
    title: String,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: androidx.compose.animation.AnimatedVisibilityScope
) {
    val image = ImageData.sampleImages.find { it.id == imageId }
    
    if (image == null) {
        // Error handling
        return
    }
    
    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                AppToolbar(
                    title = title,
                    showBackButton = true,
                    onBackClick = onBackClick
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Hero изображение с shared element
                AsyncImage(
                    model = image.imageUrl,
                    contentDescription = image.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 10f)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image-${image.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    contentScale = ContentScale.Crop
                )
                
                // Контент с shared заголовком
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = image.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = "title-${image.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                    
                    // Дополнительный контент
                    // ...
                }
            }
        }
    }
}
```

## Данные для демонстрации

### ImageItem data class
```kotlin
data class ImageItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val description: String
)
```

### Источники изображений
```kotlin
object ImageData {
    val sampleImages = listOf(
        ImageItem(
            id = "1",
            title = "Beautiful Landscape",
            imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=600&fit=crop",
            description = "A stunning landscape with mountains and lakes..."
        ),
        // ... другие изображения из Unsplash
    )
}
```

## Ключевые концепции Shared Elements

### 1. SharedContentState
```kotlin
rememberSharedContentState(key = "image-${image.id}")
```
- Уникальные ключи для связывания элементов
- Консистентность между экранами

### 2. sharedElement modifier
```kotlin
.sharedElement(
    state = rememberSharedContentState(key = "unique-key"),
    animatedVisibilityScope = animatedVisibilityScope
)
```
- Применяется к элементам, которые должны анимироваться
- Требует AnimatedVisibilityScope из AnimatedContent

### 3. Scope management
- `SharedTransitionScope` - координирует shared elements
- `AnimatedVisibilityScope` - из AnimatedContent для transition'ов
- Передача scope'ов через параметры композитных функций

## Анимации переходов

### Настройка AnimatedContent
```kotlin
AnimatedContent(
    targetState = currentScreen,
    transitionSpec = {
        fadeIn(animationSpec = tween(300)) + slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(300)
        ) togetherWith fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(300)
        )
    },
    label = "SharedElementTransition"
)
```

**Закомментирован в коде** для использования дефолтных анимаций.

## Интеграция с навигацией

### Упрощенная архитектура vs Decompose
```kotlin
// Простое состояние вместо Decompose навигации
var currentScreen by remember { 
    mutableStateOf<SharedElementScreen>(SharedElementScreen.List) 
}

// Навигация через изменение состояния
onImageClick = { imageId, title ->
    currentScreen = SharedElementScreen.Detail(imageId, title)
}
```

### Преимущества подхода:
- Полное соответствие документации Android
- Нет зависимости от Decompose для shared elements
- Простая отладка и понимание
- Использование стандартных Compose инструментов

## Особенности реализации

### ✅ Преимущества:
- **Официальный подход** - точно по документации Android
- **Плавные переходы** - корректная работа AnimatedVisibilityScope
- **Простая архитектура** - без сложных navigation framework'ов
- **Типобезопасность** - sealed class для состояний экрана
- **Обратная совместимость** - кнопка назад работает корректно

### ⚠️ Ограничения:
- **Локальная навигация** - только между двумя экранами
- **Нет deep links** - simplified navigation
- **Состояние в памяти** - теряется при пересоздании Activity

## Альтернативные подходы

### С Navigation Compose
```kotlin
// Использование с NavController
composable(
    route = "detail/{imageId}",
    arguments = listOf(navArgument("imageId") { type = NavType.StringType })
) { backStackEntry ->
    // SharedTransitionScope из NavGraphBuilder
}
```

### С Decompose (будущие версии)
- Планируется нативная поддержка в Decompose 4.0
- Будет использовать специальные API для SharedTransitionScope

## Отладка и тестирование

### Проверка ключей
- Убедитесь что ключи `rememberSharedContentState` одинаковые на обеих экранах
- Используйте уникальные ID для разных элементов

### Layout Inspector
- Используйте Layout Inspector для проверки shared element tree
- Проверяйте правильность передачи scope'ов

### Производительность
- Shared elements не влияют на производительность прокрутки
- Анимации оптимизированы на композитном уровне

## Когда использовать
- **Простые transition'ы** между экранами
- **Обучение** основам Shared Elements в Compose
- **Прототипирование** без сложной навигации
- **Демонстрация** возможностей системы