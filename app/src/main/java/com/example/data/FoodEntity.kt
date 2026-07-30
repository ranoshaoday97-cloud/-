package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameAr: String,
    val category: String, // "بروتينات", "نشويات", "دهون صحية", "خضروات وفواكه", "بقوليات"
    val rawCaloriesPer100g: Double,
    val cookedCaloriesPer100g: Double,
    val rawProteinPer100g: Double,
    val cookedProteinPer100g: Double,
    val rawCarbsPer100g: Double,
    val cookedCarbsPer100g: Double,
    val rawFatPer100g: Double,
    val cookedFatPer100g: Double,
    val iconName: String = "ic_food_generic",
    val defaultServingGrams: Double = 100.0,
    val cookingNotes: String = ""
)
