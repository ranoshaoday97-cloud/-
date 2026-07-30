package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_logs")
data class MealLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String, // yyyy-MM-dd
    val mealType: String, // "إفطار", "غداء", "عشاء", "وجبة خفيفة"
    val foodName: String,
    val isCooked: Boolean, // true = بعد الطبخ, false = قبل الطبخ
    val weightGrams: Double,
    val calories: Double,
    val proteinGrams: Double,
    val carbsGrams: Double,
    val fatGrams: Double,
    val imageUri: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
