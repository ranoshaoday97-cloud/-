package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String,
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String,
    val activityName: String, // "مشي سريع", "جري", "رفع أثقال / حديد", "سباحة", "ركوب دراجة", "تمارين HIIT"
    val durationMinutes: Int,
    val caloriesBurned: Double,
    val timestamp: Long = System.currentTimeMillis()
)
