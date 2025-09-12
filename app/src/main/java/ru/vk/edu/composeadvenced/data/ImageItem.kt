package ru.vk.edu.composeadvenced.data

data class ImageItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val description: String
)

// Фиктивные данные для демонстрации
object ImageData {
    val sampleImages = listOf(
        ImageItem(
            id = "1",
            title = "Beautiful Landscape",
            imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=600&fit=crop",
            description = "A stunning landscape with mountains and lakes that stretches far into the horizon."
        ),
        ImageItem(
            id = "2", 
            title = "City Architecture",
            imageUrl = "https://images.unsplash.com/photo-1480714378408-67cf0d13bc1f?w=400&h=600&fit=crop",
            description = "Modern architectural design showcasing the beauty of urban development."
        ),
        ImageItem(
            id = "3",
            title = "Nature Wildlife",
            imageUrl = "https://images.unsplash.com/photo-1547036967-23d11aacaee0?w=400&h=600&fit=crop", 
            description = "Amazing wildlife photography capturing animals in their natural habitat."
        ),
        ImageItem(
            id = "4",
            title = "Ocean Waves",
            imageUrl = "https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=400&h=600&fit=crop",
            description = "Powerful ocean waves crashing against the rocky shore during sunset."
        ),
        ImageItem(
            id = "5",
            title = "Forest Path",
            imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=400&h=600&fit=crop",
            description = "A peaceful forest path winding through ancient trees and dappled sunlight."
        ),
        ImageItem(
            id = "6",
            title = "Mountain Peak",
            imageUrl = "https://images.unsplash.com/photo-1454391304352-2bf4678b1a7a?w=400&h=600&fit=crop",
            description = "Snow-capped mountain peak rising majestically above the clouds."
        ),
        ImageItem(
            id = "7",
            title = "Desert Sunset",
            imageUrl = "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9?w=400&h=600&fit=crop",
            description = "Golden desert dunes illuminated by the warm glow of the setting sun."
        ),
        ImageItem(
            id = "8",
            title = "River Valley",
            imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=600&fit=crop",
            description = "A serene river valley with lush green vegetation and crystal clear water."
        ),
        ImageItem(
            id = "9",
            title = "Urban Skyline",
            imageUrl = "https://images.unsplash.com/photo-1449824913935-59a10b8d2000?w=400&h=600&fit=crop",
            description = "Breathtaking city skyline illuminated against the evening sky."
        ),
        ImageItem(
            id = "10",
            title = "Tropical Beach",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400&h=600&fit=crop",
            description = "Crystal clear tropical waters and pristine white sand beaches."
        ),
        ImageItem(
            id = "11",
            title = "Autumn Colors",
            imageUrl = "https://images.unsplash.com/photo-1415604934674-561df9abf539?w=400&h=600&fit=crop",
            description = "Vibrant autumn foliage creating a spectacular natural display."
        ),
        ImageItem(
            id = "12",
            title = "Starry Night",
            imageUrl = "https://images.unsplash.com/photo-1419242902214-272b3f66ee7a?w=400&h=600&fit=crop",
            description = "Magnificent starry night sky over a tranquil landscape."
        )
    )
}