# animateItem

## Описание
animateItem - это модификатор, который добавляет анимацию перемещения элементов в LazyList (LazyColumn, LazyRow) в Jetpack Compose. Он автоматически анимирует изменение позиции элемента при перестановке, добавлении или удалении элементов в списке.

## Основные характеристики
- Анимирует изменение позиции элементов в LazyList
- Применяется как модификатор к элементам внутри LazyColumn или LazyRow
- Автоматически реагирует на изменения в списке
- Поддерживает настройку спецификации анимации

## Когда использовать
- Для анимации перемещения элементов при изменении порядка в списке
- При реализации списков с возможностью сортировки
- Когда нужно плавно анимировать добавление или удаление элементов
- Для создания более отзывчивых и визуально приятных списков

## Пример использования
```kotlin
@Composable
fun AnimatedSortableList() {
    var items by remember { mutableStateOf(List(10) { "Элемент ${it + 1}" }) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { items = items.shuffled() }) {
                Text("Перемешать")
            }
            
            Button(onClick = { items = items.sorted() }) {
                Text("Сортировать")
            }
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items, key = { it }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .animateItem(
                            fadeInSpec = null, fadeOutSpec = null, placementSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
```

## Пример с добавлением и удалением элементов
```kotlin
@Composable
fun AnimatedListWithAddRemove() {
    var items by remember { mutableStateOf(List(5) { "Элемент ${it + 1}" }) }
    var nextItem by remember { mutableIntStateOf(6) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { 
                    items = items + "Элемент $nextItem"
                    nextItem++
                }
            ) {
                Text("Добавить")
            }
            
            Button(
                onClick = { 
                    if (items.isNotEmpty()) {
                        items = items.dropLast(1)
                    }
                },
                enabled = items.isNotEmpty()
            ) {
                Text("Удалить")
            }
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items, key = { it }) { item ->
                ListItem(
                    headlineContent = { Text(item) },
                    trailingContent = {
                        IconButton(onClick = { items = items - item }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null,
                            placementSpec = tween(durationMillis = 300),
                        )
                )
                Divider()
            }
        }
    }
}
```

## Преимущества
- Простой в использовании
- Автоматически анимирует изменения позиции элементов
- Работает с ключами элементов для правильного отслеживания
- Создает плавные и приятные визуальные эффекты при изменении списка

## Ограничения
- Работает только с LazyColumn и LazyRow
- Требует уникальных ключей для элементов списка
- Анимирует только изменение позиции, но не другие свойства
- Может вызывать проблемы с производительностью при анимации большого количества элементов одновременно 
