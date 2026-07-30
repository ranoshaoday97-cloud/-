package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "المستخدم",
    val gender: String = "رجل", // "رجل" or "امرأة"
    val age: Int = 26,
    val heightCm: Float = 175f,
    val weightKg: Float = 75f,
    val activityLevel: String = "نشاط متوسط", // "خامل", "خفيف", "نشاط متوسط", "عالي", "شديد"
    val dietType: String = "حساب السعرات", // "حساب السعرات", "لو كارب", "كيتو", "كارنيفور"
    val goal: String = "خسارة وزن", // "خسارة وزن", "تثبيت الوزن", "بناء عضلات"
    val targetWaterMl: Int = 2800
)
