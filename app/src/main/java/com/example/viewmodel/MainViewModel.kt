package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ActivityLogEntity
import com.example.data.CalorieDatabase
import com.example.data.FoodEntity
import com.example.data.MealLogEntity
import com.example.data.UserEntity
import com.example.data.WaterLogEntity
import com.example.repository.CalorieRepository
import com.example.repository.MetabolismCalculator
import com.example.repository.MetabolicResults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CalorieRepository
    val todayDateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    init {
        val dao = CalorieDatabase.getDatabase(application).calorieDao()
        repository = CalorieRepository(dao)
    }

    // 1. User Profile & Metabolic Calculations
    val userProfile: StateFlow<UserEntity> = repository.userProfile
        .map { it ?: UserEntity() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserEntity()
        )

    val metabolicResults: StateFlow<MetabolicResults> = userProfile
        .map { MetabolismCalculator.calculate(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MetabolismCalculator.calculate(UserEntity())
        )

    // 2. Food Database & Search Filters
    val allFoods: StateFlow<List<FoodEntity>> = repository.allFoods
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("الكل")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredFoods: StateFlow<List<FoodEntity>> = combine(allFoods, searchQuery, selectedCategory) { foods, query, category ->
        foods.filter { food ->
            val matchesQuery = query.isBlank() || food.nameAr.contains(query, ignoreCase = true)
            val matchesCategory = category == "الكل" || food.category == category
            matchesQuery && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 3. Today's Logs
    val todayMealLogs: StateFlow<List<MealLogEntity>> = repository.getMealLogsByDate(todayDateString)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayWaterLogs: StateFlow<List<WaterLogEntity>> = repository.getWaterLogsByDate(todayDateString)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayActivityLogs: StateFlow<List<ActivityLogEntity>> = repository.getActivityLogsByDate(todayDateString)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 4. Summary Totals
    val totalWaterMl: StateFlow<Int> = todayWaterLogs.map { logs ->
        logs.sumOf { it.amountMl }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val consumedCalories: StateFlow<Double> = todayMealLogs.map { logs ->
        logs.sumOf { it.calories }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val consumedProtein: StateFlow<Double> = todayMealLogs.map { logs ->
        logs.sumOf { it.proteinGrams }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val consumedCarbs: StateFlow<Double> = todayMealLogs.map { logs ->
        logs.sumOf { it.carbsGrams }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val consumedFat: StateFlow<Double> = todayMealLogs.map { logs ->
        logs.sumOf { it.fatGrams }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val burnedCalories: StateFlow<Double> = todayActivityLogs.map { logs ->
        logs.sumOf { it.caloriesBurned }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // 5. Interactive Calculator State
    private val _isCookedMode = MutableStateFlow(false) // false = قبل الطبخ, true = بعد الطبخ
    val isCookedMode: StateFlow<Boolean> = _isCookedMode.asStateFlow()

    private val _gramsInput = MutableStateFlow("100")
    val gramsInput: StateFlow<String> = _gramsInput.asStateFlow()

    private val _selectedFood = MutableStateFlow<FoodEntity?>(null)
    val selectedFood: StateFlow<FoodEntity?> = _selectedFood.asStateFlow()

    private val _selectedMealType = MutableStateFlow("غداء")
    val selectedMealType: StateFlow<String> = _selectedMealType.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setCookedMode(isCooked: Boolean) {
        _isCookedMode.value = isCooked
    }

    fun setGramsInput(grams: String) {
        _gramsInput.value = grams.filter { it.isDigit() || it == '.' }
    }

    fun selectFood(food: FoodEntity) {
        _selectedFood.value = food
    }

    fun setMealType(mealType: String) {
        _selectedMealType.value = mealType
    }

    fun setImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    // User Operations
    fun updateUserProfile(
        name: String,
        gender: String,
        age: Int,
        heightCm: Float,
        weightKg: Float,
        activityLevel: String,
        dietType: String,
        goal: String
    ) {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = current.copy(
                name = name,
                gender = gender,
                age = age,
                heightCm = heightCm,
                weightKg = weightKg,
                activityLevel = activityLevel,
                dietType = dietType,
                goal = goal
            )
            repository.saveUserProfile(updated)
        }
    }

    fun logCurrentMeal(onSuccess: () -> Unit = {}) {
        val food = _selectedFood.value ?: return
        val grams = _gramsInput.value.toDoubleOrNull() ?: 100.0
        val isCooked = _isCookedMode.value
        val factor = grams / 100.0

        val cal = if (isCooked) food.cookedCaloriesPer100g * factor else food.rawCaloriesPer100g * factor
        val prot = if (isCooked) food.cookedProteinPer100g * factor else food.rawProteinPer100g * factor
        val carb = if (isCooked) food.cookedCarbsPer100g * factor else food.rawCarbsPer100g * factor
        val fat = if (isCooked) food.cookedFatPer100g * factor else food.rawFatPer100g * factor

        val mealLog = MealLogEntity(
            dateString = todayDateString,
            mealType = _selectedMealType.value,
            foodName = food.nameAr,
            isCooked = isCooked,
            weightGrams = grams,
            calories = (cal * 10).roundToInt() / 10.0,
            proteinGrams = (prot * 10).roundToInt() / 10.0,
            carbsGrams = (carb * 10).roundToInt() / 10.0,
            fatGrams = (fat * 10).roundToInt() / 10.0,
            imageUri = _selectedImageUri.value?.toString() ?: ""
        )

        viewModelScope.launch {
            repository.insertMealLog(mealLog)
            _selectedImageUri.value = null
            onSuccess()
        }
    }

    fun deleteMealLog(id: Long) {
        viewModelScope.launch {
            repository.deleteMealLog(id)
        }
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            repository.addWater(todayDateString, amountMl)
        }
    }

    fun logActivity(activityName: String, durationMinutes: Int, caloriesBurnedPerMin: Double) {
        val totalBurned = durationMinutes * caloriesBurnedPerMin
        val activity = ActivityLogEntity(
            dateString = todayDateString,
            activityName = activityName,
            durationMinutes = durationMinutes,
            caloriesBurned = (totalBurned * 10).roundToInt() / 10.0
        )
        viewModelScope.launch {
            repository.insertActivityLog(activity)
        }
    }

    fun deleteActivityLog(id: Long) {
        viewModelScope.launch {
            repository.deleteActivityLog(id)
        }
    }

    fun addCustomFood(
        nameAr: String,
        category: String,
        rawCal: Double,
        cookedCal: Double,
        rawProtein: Double,
        cookedProtein: Double,
        rawCarbs: Double,
        cookedCarbs: Double,
        rawFat: Double,
        cookedFat: Double,
        notes: String
    ) {
        val newFood = FoodEntity(
            nameAr = nameAr,
            category = category,
            rawCaloriesPer100g = rawCal,
            cookedCaloriesPer100g = cookedCal,
            rawProteinPer100g = rawProtein,
            cookedProteinPer100g = cookedProtein,
            rawCarbsPer100g = rawCarbs,
            cookedCarbsPer100g = cookedCarbs,
            rawFatPer100g = rawFat,
            cookedFatPer100g = cookedFat,
            cookingNotes = notes
        )
        viewModelScope.launch {
            repository.insertCustomFood(newFood)
        }
    }
}
