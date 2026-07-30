package com.example.repository

import com.example.data.ActivityLogEntity
import com.example.data.CalorieDao
import com.example.data.FoodEntity
import com.example.data.MealLogEntity
import com.example.data.UserEntity
import com.example.data.WaterLogEntity
import kotlinx.coroutines.flow.Flow

class CalorieRepository(private val dao: CalorieDao) {

    val userProfile: Flow<UserEntity?> = dao.getUserProfile()
    val allFoods: Flow<List<FoodEntity>> = dao.getAllFoods()

    suspend fun getUserProfileDirect(): UserEntity? = dao.getUserProfileDirect()

    suspend fun saveUserProfile(user: UserEntity) {
        dao.saveUserProfile(user)
    }

    suspend fun insertCustomFood(food: FoodEntity) {
        dao.insertFood(food)
    }

    fun getMealLogsByDate(date: String): Flow<List<MealLogEntity>> {
        return dao.getMealLogsByDate(date)
    }

    suspend fun insertMealLog(meal: MealLogEntity) {
        dao.insertMealLog(meal)
    }

    suspend fun deleteMealLog(id: Long) {
        dao.deleteMealLog(id)
    }

    fun getWaterLogsByDate(date: String): Flow<List<WaterLogEntity>> {
        return dao.getWaterLogsByDate(date)
    }

    suspend fun addWater(date: String, amountMl: Int) {
        dao.insertWaterLog(WaterLogEntity(dateString = date, amountMl = amountMl))
    }

    fun getActivityLogsByDate(date: String): Flow<List<ActivityLogEntity>> {
        return dao.getActivityLogsByDate(date)
    }

    suspend fun insertActivityLog(activity: ActivityLogEntity) {
        dao.insertActivityLog(activity)
    }

    suspend fun deleteActivityLog(id: Long) {
        dao.deleteActivityLog(id)
    }
}
