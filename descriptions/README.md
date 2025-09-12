# Compose Advanced - Документация проекта

## Обзор проекта
Учебное приложение для демонстрации продвинутых техник и возможностей Jetpack Compose. Проект охватывает анимации, кастомные компоненты, интеграцию с Decompose, Lottie анимации и Shared Element Transitions.

## Структура проекта

### 📱 Основные разделы

#### 1. [Анимации](animations/)
Различные подходы к анимациям в Compose:
- **Animatable** - низкоуровневые анимации
- **animateAsState** - простые property анимации  
- **updateTransition** - комплексные состояния
- **AnimatedVisibility** - анимации появления/исчезновения
- **AnimatedContent** - переходы между контентом
- **Crossfade** - плавные переключения
- **animateContentSize** - автоматические размеры
- **AnchoredDraggable** - интерактивные жесты
- **animateItemPlacement** - анимации в списках

#### 2. [Кастомные компоненты](custom/)
Создание собственных компонентов:
- **Custom Modifiers** - собственные модификаторы
- **Canvas Drawing** - рисование на Canvas
- **Custom Layouts** - собственные layout'ы
- **Complex Components** - сложные составные компоненты

#### 3. [Pager Indicators](pager/)
Различные реализации индикаторов для HorizontalPager:
- **[PagerAnimatedIndicator](pager/PagerAnimatedIndicator.md)** - анимированная активная точка
- **[PagerLazySimple](pager/PagerLazySimple.md)** - LazyRow с прокруткой  
- **[PagerCustomLayoutIndicator](pager/PagerCustomLayoutIndicator.md)** - Custom Layout подход
- **[PagerSample](pager/PagerSample.md)** - демонстрационный экран

#### 4. [Lottie Animations](lottie/)
Интеграция Lottie анимаций:
- **[Руководство по реализации](lottie/README.md)** - полное описание интеграции
- Различные параметры (speed, iterations, фоны)
- Интерактивные элементы управления
- Онлайн загрузка анимаций

#### 5. [Shared Element Transitions](shared_elements/)
Плавные переходы между экранами:
- **[Полное руководство](shared_elements/README.md)** - реализация без Decompose
- SharedTransitionLayout + AnimatedContent
- Корректная работа scope'ов
- Типобезопасная навигация

#### 6. [Decompose Integration](decompose/)
Интеграция с Decompose framework:
- **[Руководство по настройке](decompose/README.md)** - архитектура навигации
- ComponentContext и lifecycle
- Типобезопасная навигация
- BackHandler интеграция

## Технологический стек

### Core технологии
- **Jetpack Compose** - UI фреймворк
- **Kotlin** - язык программирования
- **Material 3** - дизайн система

### Библиотеки и зависимости
```kotlin
// Decompose - навигация и архитектура
implementation("com.arkivanov.decompose:decompose:3.2.0-alpha05")
implementation("com.arkivanov.decompose:extensions-compose:3.2.0-alpha05")

// Lottie - анимации
implementation("com.airbnb.android:lottie-compose:6.1.0")

// Изображения
implementation("io.coil-kt:coil-compose:2.5.0")

// Сериализация
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
```

### Архитектура проекта
```
app/src/main/java/ru/vk/edu/composeadvenced/
├── navigation/          # Конфигурации навигации
├── root/               # Корневые компоненты
├── screens/            # Screen компоненты
├── ui/                 # UI компоненты
│   ├── animation/      # Анимационные экраны
│   ├── custom/         # Кастомные компоненты
│   ├── pager/          # Pager индикаторы
│   ├── components/     # Переиспользуемые компоненты
│   └── theme/          # Тема приложения
└── data/              # Модели данных
```

## Ключевые особенности реализации

### 🎨 Анимации
- **Производительность** - оптимизированные анимации без лагов
- **Интерактивность** - поддержка жестов и пользовательского ввода
- **Гибкость** - различные подходы для разных задач

### 🏗️ Архитектура
- **Decompose** - реактивная навигация с lifecycle
- **Unidirectional data flow** - предсказуемые состояния
- **Component-based** - переиспользуемые части UI

### 🎯 UX/UI принципы  
- **Material 3** - современный дизайн язык
- **Responsive** - адаптация к разным размерам экрана
- **Accessibility** - поддержка доступности

## Практические применения

### Для обучения
- **Пошаговые руководства** в каждом разделе
- **Комментированный код** с объяснениями
- **Примеры best practices** для Compose

### Для разработки
- **Готовые компоненты** для копирования в проекты
- **Паттерны архитектуры** для масштабируемых приложений
- **Performance tips** и оптимизации

### Для экспериментов
- **Playground** для тестирования новых идей
- **Модульная структура** для легкого добавления новых фич
- **Comprehensive examples** разных подходов

## Запуск и сборка

### Требования
- Android Studio Hedgehog | 2023.1.1+
- Kotlin 1.9.20+
- Compose Compiler 1.5.8+
- minSdk 24, targetSdk 34

### Сборка проекта
```bash
# Клонирование репозитория
git clone <repository-url>

# Сборка debug версии
./gradlew assembleDebug

# Запуск тестов
./gradlew test

# Проверка lint'ом
./gradlew lint
```

### Конфигурация
- **build.gradle.kts** - основные зависимости и настройки
- **AndroidManifest.xml** - INTERNET permission для Lottie и изображений
- **Theme configuration** - Material 3 цвета и типографика

## Навигационная структура

```
Main Screen
├── Анимации → AnimationsScreen
│   ├── Animatable
│   ├── animateAsState  
│   ├── updateTransition
│   ├── AnimatedVisibility
│   ├── AnimatedContent
│   ├── Crossfade
│   ├── animateContentSize
│   ├── AnchoredDraggable
│   └── animateItemPlacement
├── Кастомные компоненты → CustomComponentsScreen
│   ├── Custom Modifiers
│   ├── Canvas Drawing
│   ├── Custom Layouts
│   └── Complex Components
├── ViewModel с Decompose → ViewModelScreen
├── Pager Indicator → PagerIndicatorScreen
├── Shared Element Transition → SimpleSharedElementScreen
└── Lottie Animations → LottieAnimationsScreen
```

## Вклад в проект

### Добавление новых примеров
1. Создайте новую конфигурацию в `Configuration.kt`
2. Добавьте Component в `screens/`
3. Создайте UI в `ui/`
4. Обновите `RootComponent` и `RootContent`
5. Добавьте документацию в `descriptions/`

### Структура документации
- **README.md** - обзор и основные концепции
- **Пошаговые гайды** - детальная реализация
- **Code snippets** - ключевые части кода
- **Best practices** - рекомендации по использованию

## Полезные ресурсы

### Официальная документация
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Compose Animation](https://developer.android.com/jetpack/compose/animation)
- [Material 3](https://m3.material.io/)

### Библиотеки
- [Decompose](https://arkivanov.github.io/Decompose/)
- [Lottie Compose](https://github.com/airbnb/lottie-android)
- [Coil](https://coil-kt.github.io/coil/)

### Сообщество
- [Compose Slack](https://kotlinlang.slack.com/messages/compose/)
- [Android Developers](https://developer.android.com/courses/android-development-with-kotlin)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/android-jetpack-compose)