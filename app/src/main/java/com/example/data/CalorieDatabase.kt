package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        FoodEntity::class,
        MealLogEntity::class,
        WaterLogEntity::class,
        ActivityLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CalorieDatabase : RoomDatabase() {
    abstract fun calorieDao(): CalorieDao

    companion object {
        @Volatile
        private var INSTANCE: CalorieDatabase? = null

        fun getDatabase(context: Context): CalorieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalorieDatabase::class.java,
                    "calorie_metabolism_db"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.calorieDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: CalorieDao) {
            // Initial User Profile
            dao.saveUserProfile(
                UserEntity(
                    id = 1,
                    name = "المستخدم",
                    gender = "رجل",
                    age = 26,
                    heightCm = 175f,
                    weightKg = 75f,
                    activityLevel = "نشاط متوسط",
                    dietType = "حساب السعرات",
                    goal = "خسارة وزن",
                    targetWaterMl = 2800
                )
            )

            // Initial Rich Arabic Food Database (Raw vs Cooked Comparison)
            val initialFoods = listOf(
                FoodEntity(
                    nameAr = "صدر دجاج (Chicken Breast)",
                    category = "بروتينات",
                    rawCaloriesPer100g = 120.0,
                    cookedCaloriesPer100g = 165.0,
                    rawProteinPer100g = 22.5,
                    cookedProteinPer100g = 31.0,
                    rawCarbsPer100g = 0.0,
                    cookedCarbsPer100g = 0.0,
                    rawFatPer100g = 2.5,
                    cookedFatPer100g = 3.6,
                    cookingNotes = "يفقد الدجاج ~25% من وزنه ماء أثناء الطهي فتزداد كثافة السعرات بالجرام"
                ),
                FoodEntity(
                    nameAr = "أرز أبيض (White Rice)",
                    category = "نشويات",
                    rawCaloriesPer100g = 360.0,
                    cookedCaloriesPer100g = 130.0,
                    rawProteinPer100g = 7.0,
                    cookedProteinPer100g = 2.7,
                    rawCarbsPer100g = 80.0,
                    cookedCarbsPer100g = 28.0,
                    rawFatPer100g = 0.6,
                    cookedFatPer100g = 0.3,
                    cookingNotes = "يمتص الأرز الماء أثناء الطهي فيتضاعف وزنه 2.5 إلى 3 مرات وتقل سعرات الجرام"
                ),
                FoodEntity(
                    nameAr = "لحم بقري مفروم (Ground Beef)",
                    category = "بروتينات",
                    rawCaloriesPer100g = 250.0,
                    cookedCaloriesPer100g = 210.0,
                    rawProteinPer100g = 17.0,
                    cookedProteinPer100g = 24.0,
                    rawCarbsPer100g = 0.0,
                    cookedCarbsPer100g = 0.0,
                    rawFatPer100g = 20.0,
                    cookedFatPer100g = 12.0,
                    cookingNotes = "يفقد اللحم المفروم بعض الدهون والماء أثناء الشواء"
                ),
                FoodEntity(
                    nameAr = "بطاطس (Potatoes)",
                    category = "نشويات",
                    rawCaloriesPer100g = 77.0,
                    cookedCaloriesPer100g = 87.0,
                    rawProteinPer100g = 2.0,
                    cookedProteinPer100g = 1.9,
                    rawCarbsPer100g = 17.0,
                    cookedCarbsPer100g = 20.0,
                    rawFatPer100g = 0.1,
                    cookedFatPer100g = 0.1,
                    cookingNotes = "البطاطس المسلوقة تجف قليلاً أو تحتفظ بالسعرات بزيادة بسيطة في الكثافة"
                ),
                FoodEntity(
                    nameAr = "شوفان (Oats)",
                    category = "نشويات",
                    rawCaloriesPer100g = 389.0,
                    cookedCaloriesPer100g = 71.0,
                    rawProteinPer100g = 16.9,
                    cookedProteinPer100g = 2.5,
                    rawCarbsPer100g = 66.0,
                    cookedCarbsPer100g = 12.0,
                    rawFatPer100g = 6.9,
                    cookedFatPer100g = 1.4,
                    cookingNotes = "الشوفان عند طهيه بالحليب أو الماء يمتص السوائل ويكبر حجمه بشكل كبير"
                ),
                FoodEntity(
                    nameAr = "معكرونة (Pasta)",
                    category = "نشويات",
                    rawCaloriesPer100g = 371.0,
                    cookedCaloriesPer100g = 131.0,
                    rawProteinPer100g = 13.0,
                    cookedProteinPer100g = 5.0,
                    rawCarbsPer100g = 74.0,
                    cookedCarbsPer100g = 25.0,
                    rawFatPer100g = 1.5,
                    cookedFatPer100g = 1.1,
                    cookingNotes = "تمتص المعكرونة كمية كبيرة من الماء أثناء السلق فيقل تركيز السعرات للجرام"
                ),
                FoodEntity(
                    nameAr = "عدس أصفر (Yellow Lentils)",
                    category = "بقوليات",
                    rawCaloriesPer100g = 353.0,
                    cookedCaloriesPer100g = 116.0,
                    rawProteinPer100g = 25.0,
                    cookedProteinPer100g = 9.0,
                    rawCarbsPer100g = 60.0,
                    cookedCarbsPer100g = 20.0,
                    rawFatPer100g = 1.0,
                    cookedFatPer100g = 0.4,
                    cookingNotes = "غني بالبروتين والنقيع مع السلق يضاعف الوزن"
                ),
                FoodEntity(
                    nameAr = "سمك سلمون (Salmon)",
                    category = "بروتينات",
                    rawCaloriesPer100g = 208.0,
                    cookedCaloriesPer100g = 206.0,
                    rawProteinPer100g = 20.0,
                    cookedProteinPer100g = 22.0,
                    rawCarbsPer100g = 0.0,
                    cookedCarbsPer100g = 0.0,
                    rawFatPer100g = 13.0,
                    cookedFatPer100g = 12.0,
                    cookingNotes = "غني بأوميغا 3 والدهون الصحية المفيدة للكيتو واللوكار ب"
                ),
                FoodEntity(
                    nameAr = "بيض دجاج (Eggs)",
                    category = "بروتينات",
                    rawCaloriesPer100g = 143.0,
                    cookedCaloriesPer100g = 155.0,
                    rawProteinPer100g = 12.6,
                    cookedProteinPer100g = 13.0,
                    rawCarbsPer100g = 0.7,
                    cookedCarbsPer100g = 1.1,
                    rawFatPer100g = 9.5,
                    cookedFatPer100g = 11.0,
                    cookingNotes = "بيضة متوسطة الحجم تزن حوالي 50 جرام"
                ),
                FoodEntity(
                    nameAr = "زيت زيتون (Olive Oil)",
                    category = "دهون صحية",
                    rawCaloriesPer100g = 884.0,
                    cookedCaloriesPer100g = 884.0,
                    rawProteinPer100g = 0.0,
                    cookedProteinPer100g = 0.0,
                    rawCarbsPer100g = 0.0,
                    cookedCarbsPer100g = 0.0,
                    rawFatPer100g = 100.0,
                    cookedFatPer100g = 100.0,
                    cookingNotes = "الملعقة الكبيرة تزن ~14 جرام وتساوي ~120 سعر حراري"
                ),
                FoodEntity(
                    nameAr = "موز (Banana)",
                    category = "خضروات وفواكه",
                    rawCaloriesPer100g = 89.0,
                    cookedCaloriesPer100g = 89.0,
                    rawProteinPer100g = 1.1,
                    cookedProteinPer100g = 1.1,
                    rawCarbsPer100g = 22.8,
                    cookedCarbsPer100g = 22.8,
                    rawFatPer100g = 0.3,
                    cookedFatPer100g = 0.3,
                    cookingNotes = "مصدر سريع للطاقة والبوتاسيوم قبل التمرين"
                ),
                FoodEntity(
                    nameAr = "تفاح (Apple)",
                    category = "خضروات وفواكه",
                    rawCaloriesPer100g = 52.0,
                    cookedCaloriesPer100g = 52.0,
                    rawProteinPer100g = 0.3,
                    cookedProteinPer100g = 0.3,
                    rawCarbsPer100g = 13.8,
                    cookedCarbsPer100g = 13.8,
                    rawFatPer100g = 0.2,
                    cookedFatPer100g = 0.2,
                    cookingNotes = "تفاحة متوسطة تزن ~150-180 جرام"
                ),
                FoodEntity(
                    nameAr = "لوز نيء (Raw Almonds)",
                    category = "دهون صحية",
                    rawCaloriesPer100g = 579.0,
                    cookedCaloriesPer100g = 590.0,
                    rawProteinPer100g = 21.0,
                    cookedProteinPer100g = 21.0,
                    rawCarbsPer100g = 22.0,
                    cookedCarbsPer100g = 21.0,
                    rawFatPer100g = 50.0,
                    cookedFatPer100g = 52.0,
                    cookingNotes = "ممتاز للحمية المنخفضة الكاربوهيدرات والكيتو"
                )
            )

            dao.insertAllFoods(initialFoods)
        }
    }
}
