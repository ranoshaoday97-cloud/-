package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.FoodEntity
import com.example.data.MealLogEntity
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SlateDark
import com.example.viewmodel.MainViewModel
import kotlin.math.roundToInt

val CATEGORIES = listOf("الكل", "بروتينات", "نشويات", "دهون صحية", "خضروات وفواكه", "بقوليات")
val MEAL_TYPES = listOf("إفطار", "غداء", "عشاء", "وجبة خفيفة")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCalculatorScreen(
    viewModel: MainViewModel
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredFoods by viewModel.filteredFoods.collectAsState()
    val selectedFood by viewModel.selectedFood.collectAsState()

    val isCookedMode by viewModel.isCookedMode.collectAsState()
    val gramsInput by viewModel.gramsInput.collectAsState()
    val selectedMealType by viewModel.selectedMealType.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    val todayMealLogs by viewModel.todayMealLogs.collectAsState()
    val consumedCal by viewModel.consumedCalories.collectAsState()

    var showCustomFoodDialog by remember { mutableStateOf(false) }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setImageUri(uri)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F6)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Food Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("ابحث عن اسم الطعام (دجاج، أرز، لحم، إلخ)...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("food_search_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        // 2. Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CATEGORIES) { cat ->
                    val isSelected = cat == selectedCategory
                    val bg = if (isSelected) EmeraldSuccess else Color.White
                    val textColor = if (isSelected) Color.White else SlateDark

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bg)
                            .border(1.dp, if (isSelected) EmeraldSuccess else Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .clickable { viewModel.setSelectedCategory(cat) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("category_chip_$cat")
                    ) {
                        Text(text = cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = textColor)
                    }
                }
            }
        }

        // 3. Food List Selection Carousel / Row
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "اختر صنف الطعام:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SlateDark)
                    TextButton(onClick = { showCustomFoodDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "+ صنف خاص", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (filteredFoods.isEmpty()) {
                    Text(text = "لم يتم العثور على طعام مطابق للبحث.", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 12.dp))
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(filteredFoods) { food ->
                            val isSelected = food.id == selectedFood?.id
                            val borderCol = if (isSelected) EmeraldSuccess else Color(0xFFE2E8F0)
                            val containerBg = if (isSelected) Color(0xFFECFDF5) else Color.White

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = containerBg),
                                modifier = Modifier
                                    .width(180.dp)
                                    .border(2.dp, borderCol, RoundedCornerShape(16.dp))
                                    .clickable { viewModel.selectFood(food) }
                                    .testTag("food_card_${food.id}")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = food.nameAr,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = SlateDark,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = food.category,
                                        fontSize = 11.sp,
                                        color = IndigoPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "قبل: ${food.rawCaloriesPer100g.roundToInt()}كالو", fontSize = 10.sp, color = Color(0xFFD97706))
                                        Text(text = "بعد: ${food.cookedCaloriesPer100g.roundToInt()}كالو", fontSize = 10.sp, color = Color(0xFF059669))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Interactive Calculation Card for Selected Food
        selectedFood?.let { food ->
            item {
                val grams = gramsInput.toDoubleOrNull() ?: 100.0
                val factor = grams / 100.0
                val cal = if (isCookedMode) food.cookedCaloriesPer100g * factor else food.rawCaloriesPer100g * factor
                val prot = if (isCookedMode) food.cookedProteinPer100g * factor else food.rawProteinPer100g * factor
                val carb = if (isCookedMode) food.cookedCarbsPer100g * factor else food.rawCarbsPer100g * factor
                val fat = if (isCookedMode) food.cookedFatPer100g * factor else food.rawFatPer100g * factor

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, IndigoPrimary, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "حساب سعرات: ${food.nameAr}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = SlateDark
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // TOGGLE BUTTON: Raw (قبل الطبخ) vs Cooked (بعد الطبخ)
                        Text(text = "حالة الطعام أثناء الوزن:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SlateDark)
                        Spacer(modifier = Modifier.height(6.dp))

                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            SegmentedButton(
                                selected = !isCookedMode,
                                onClick = { viewModel.setCookedMode(false) },
                                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                            ) {
                                Text("قبل الطبخ (نيء 🥩)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            SegmentedButton(
                                selected = isCookedMode,
                                onClick = { viewModel.setCookedMode(true) },
                                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                            ) {
                                Text("بعد الطبخ (مطبوخ 🍳)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (food.cookingNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "💡 ملاحظة: ${food.cookingNotes}",
                                fontSize = 11.sp,
                                color = Color(0xFFD97706)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Grams Weight Input & Quick Buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = gramsInput,
                                onValueChange = { viewModel.setGramsInput(it) },
                                label = { Text("الوزن بالجرام") },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("grams_input_field")
                            )

                            listOf("100", "150", "200").forEach { g ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .clickable { viewModel.setGramsInput(g) }
                                        .padding(horizontal = 10.dp, vertical = 12.dp)
                                ) {
                                    Text(text = "${g}g", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Real-time Calculated Macros Box
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            MacroDisplay("السعرات", "${cal.roundToInt()} كالو", Color(0xFFEF4444))
                            MacroDisplay("البروتين", "${prot.roundToInt()} جم", Color(0xFF3B82F6))
                            MacroDisplay("الكارب", "${carb.roundToInt()} جم", Color(0xFFF59E0B))
                            MacroDisplay("الدهون", "${fat.roundToInt()} جم", Color(0xFFEC4899))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Image Picker & Meal Type Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Image Attachment Button
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE2E8F0))
                                    .clickable { imagePickerLauncher.launch("image/*") }
                                    .testTag("attach_food_photo_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedImageUri != null) {
                                    AsyncImage(
                                        model = selectedImageUri,
                                        contentDescription = "Food Image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AddAPhoto,
                                        contentDescription = "Attach Photo",
                                        tint = SlateDark
                                    )
                                }
                            }

                            // Meal Type Selector
                            var mealMenuExpanded by remember { mutableStateOf(false) }
                            ExposedDropdownMenuBox(
                                expanded = mealMenuExpanded,
                                onExpandedChange = { mealMenuExpanded = !mealMenuExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedMealType,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("نوع الوجبة") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mealMenuExpanded) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = mealMenuExpanded,
                                    onDismissRequest = { mealMenuExpanded = false }
                                ) {
                                    MEAL_TYPES.forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type) },
                                            onClick = {
                                                viewModel.setMealType(type)
                                                mealMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Submit Button -> Log Meal
                        Button(
                            onClick = { viewModel.logCurrentMeal() },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("save_meal_to_log_button")
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "إضافة الوجبة لمفكرة اليوم", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // 5. Today's Logged Meals List
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "وجبات اليوم المسجلة (${todayMealLogs.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SlateDark
                    )
                    Text(
                        text = "المجموع: ${consumedCal.roundToInt()} كالو",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = EmeraldSuccess
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (todayMealLogs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "لم تقم بتسجيل أي وجبة اليوم حتى الآن.",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    todayMealLogs.forEach { log ->
                        MealLogItemCard(mealLog = log, onDelete = { viewModel.deleteMealLog(log.id) })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // Custom Food Dialog
    if (showCustomFoodDialog) {
        CustomFoodDialog(
            onDismiss = { showCustomFoodDialog = false },
            onSave = { name, cat, rCal, cCal, rP, cP, rC, cC, rF, cF, notes ->
                viewModel.addCustomFood(name, cat, rCal, cCal, rP, cP, rC, cC, rF, cF, notes)
                showCustomFoodDialog = false
            }
        )
    }
}

@Composable
private fun MacroDisplay(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun MealLogItemCard(
    mealLog: MealLogEntity,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (mealLog.imageUri.isNotBlank()) {
                    AsyncImage(
                        model = mealLog.imageUri,
                        contentDescription = "Meal Photo",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = mealLog.foodName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SlateDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (mealLog.isCooked) Color(0xFFD1FAE5) else Color(0xFFFEF3C7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (mealLog.isCooked) "مطبوخ" else "نيء",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (mealLog.isCooked) Color(0xFF047857) else Color(0xFFB45309)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${mealLog.mealType} • ${mealLog.weightGrams.roundToInt()} جرام",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "ب: ${mealLog.proteinGrams.roundToInt()}g | ك: ${mealLog.carbsGrams.roundToInt()}g | د: ${mealLog.fatGrams.roundToInt()}g",
                        fontSize = 10.sp,
                        color = IndigoPrimary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${mealLog.calories.roundToInt()} كالو",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = EmeraldSuccess
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomFoodDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Double, Double, Double, Double, Double, Double, Double, Double, String) -> Unit
) {
    var nameAr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("بروتينات") }
    var rawCal by remember { mutableStateOf("150") }
    var cookedCal by remember { mutableStateOf("180") }
    var rawProtein by remember { mutableStateOf("20") }
    var cookedProtein by remember { mutableStateOf("25") }
    var rawCarbs by remember { mutableStateOf("0") }
    var cookedCarbs by remember { mutableStateOf("0") }
    var rawFat by remember { mutableStateOf("5") }
    var cookedFat by remember { mutableStateOf("6") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة طعام جديد بالجدول", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(350.dp)
            ) {
                item { OutlinedTextField(value = nameAr, onValueChange = { nameAr = it }, label = { Text("اسم الطعام بالعرية") }) }
                item { OutlinedTextField(value = rawCal, onValueChange = { rawCal = it }, label = { Text("سعرات 100g (قبل الطبخ/نيء)") }) }
                item { OutlinedTextField(value = cookedCal, onValueChange = { cookedCal = it }, label = { Text("سعرات 100g (بعد الطبخ/مطبوخ)") }) }
                item { OutlinedTextField(value = rawProtein, onValueChange = { rawProtein = it }, label = { Text("البروتين (قبل الطبخ)") }) }
                item { OutlinedTextField(value = cookedProtein, onValueChange = { cookedProtein = it }, label = { Text("البروتين (بعد الطبخ)") }) }
                item { OutlinedTextField(value = rawCarbs, onValueChange = { rawCarbs = it }, label = { Text("الكاربوهيدرات (قبل الطبخ)") }) }
                item { OutlinedTextField(value = cookedCarbs, onValueChange = { cookedCarbs = it }, label = { Text("الكاربوهيدرات (بعد الطبخ)") }) }
                item { OutlinedTextField(value = rawFat, onValueChange = { rawFat = it }, label = { Text("الدهون (قبل الطبخ)") }) }
                item { OutlinedTextField(value = cookedFat, onValueChange = { cookedFat = it }, label = { Text("الدهون (بعد الطبخ)") }) }
                item { OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("ملاحظات الطبخ") }) }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    nameAr, category,
                    rawCal.toDoubleOrNull() ?: 100.0, cookedCal.toDoubleOrNull() ?: 100.0,
                    rawProtein.toDoubleOrNull() ?: 0.0, cookedProtein.toDoubleOrNull() ?: 0.0,
                    rawCarbs.toDoubleOrNull() ?: 0.0, cookedCarbs.toDoubleOrNull() ?: 0.0,
                    rawFat.toDoubleOrNull() ?: 0.0, cookedFat.toDoubleOrNull() ?: 0.0,
                    notes
                )
            }) {
                Text("حفظ الصنف")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}
