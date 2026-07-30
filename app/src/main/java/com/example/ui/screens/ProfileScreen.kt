package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SlateDark
import com.example.viewmodel.MainViewModel

val ACTIVITY_LEVELS = listOf("خامل", "خفيف", "نشاط متوسط", "عالي", "شديد")
val DIET_TYPES = listOf("حساب السعرات", "لو كارب", "كيتو", "كارنيفور")
val GOALS = listOf("خسارة وزن", "تثبيت الوزن", "بناء عضلات")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val metabolicResults by viewModel.metabolicResults.collectAsState()

    var name by remember(userProfile) { mutableStateOf(userProfile.name) }
    var gender by remember(userProfile) { mutableStateOf(userProfile.gender) }
    var ageText by remember(userProfile) { mutableStateOf(userProfile.age.toString()) }
    var heightText by remember(userProfile) { mutableStateOf(userProfile.heightCm.toInt().toString()) }
    var weightText by remember(userProfile) { mutableStateOf(userProfile.weightKg.toInt().toString()) }

    var activityLevel by remember(userProfile) { mutableStateOf(userProfile.activityLevel) }
    var dietType by remember(userProfile) { mutableStateOf(userProfile.dietType) }
    var goal by remember(userProfile) { mutableStateOf(userProfile.goal) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F6)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = IndigoPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "الملف الشخصي وحاسبة معدل الحرق",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = SlateDark
                    )
                    Text(
                        text = "تحديث بيانات الوزن والطول والعمر لحساب BMR و TDEE ونصفائح التمارين",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Live Calculated Results Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, EmeraldSuccess, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = EmeraldSuccess)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "نتائج الحرق ومؤشر الجسم", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SlateDark)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFD1FAE5))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = metabolicResults.bmiCategory,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ResultMetric("BMR (الحرق الأساسي)", "${metabolicResults.bmr.toInt()} كالو", IndigoPrimary)
                        ResultMetric("TDEE (الحرق اليومي)", "${metabolicResults.tdee.toInt()} كالو", Color(0xFF0284C7))
                        ResultMetric("مؤشر كتلة الجسم BMI", "${metabolicResults.bmi}", Color(0xFFD97706))
                    }
                }
            }
        }

        // Personalized Workout Tips Card (نصائح التمارين الرياضية حسب الوزن والطول)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "💪 نصائح التمارين المخصصة لوزنك وطولك",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF15803D)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = metabolicResults.workoutAdvice,
                        fontSize = 12.sp,
                        color = SlateDark,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Profile Form Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "تعديل القياسات والجسم:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SlateDark)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("الاسم") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Gender Selector
                    Text(text = "الجنس:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SlateDark)
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = gender == "رجل",
                            onClick = { gender = "رجل" },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                        ) {
                            Text("رجل 👨")
                        }
                        SegmentedButton(
                            selected = gender == "امرأة",
                            onClick = { gender = "امرأة" },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                        ) {
                            Text("امرأة 👩")
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = ageText,
                            onValueChange = { ageText = it.filter { c -> c.isDigit() } },
                            label = { Text("العمر (سنة)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = heightText,
                            onValueChange = { heightText = it.filter { c -> c.isDigit() } },
                            label = { Text("الطول (سم)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = weightText,
                            onValueChange = { weightText = it.filter { c -> c.isDigit() } },
                            label = { Text("الوزن (كجم)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    // Activity Level Dropdown
                    var actMenuExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = actMenuExpanded,
                        onExpandedChange = { actMenuExpanded = !actMenuExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = activityLevel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("مستوى النشاط البدني اليومي") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = actMenuExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = actMenuExpanded,
                            onDismissRequest = { actMenuExpanded = false }
                        ) {
                            ACTIVITY_LEVELS.forEach { level ->
                                DropdownMenuItem(
                                    text = { Text(level) },
                                    onClick = {
                                        activityLevel = level
                                        actMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Diet Type Dropdown
                    var dietMenuExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = dietMenuExpanded,
                        onExpandedChange = { dietMenuExpanded = !dietMenuExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = dietType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("نوع الحمية الغذائية") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dietMenuExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = dietMenuExpanded,
                            onDismissRequest = { dietMenuExpanded = false }
                        ) {
                            DIET_TYPES.forEach { d ->
                                DropdownMenuItem(
                                    text = { Text(d) },
                                    onClick = {
                                        dietType = d
                                        dietMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Goal Dropdown
                    var goalMenuExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = goalMenuExpanded,
                        onExpandedChange = { goalMenuExpanded = !goalMenuExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = goal,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("الهدف الأساسي") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = goalMenuExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = goalMenuExpanded,
                            onDismissRequest = { goalMenuExpanded = false }
                        ) {
                            GOALS.forEach { g ->
                                DropdownMenuItem(
                                    text = { Text(g) },
                                    onClick = {
                                        goal = g
                                        goalMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Update Button
                    Button(
                        onClick = {
                            viewModel.updateUserProfile(
                                name = name,
                                gender = gender,
                                age = ageText.toIntOrNull() ?: 26,
                                heightCm = heightText.toFloatOrNull() ?: 175f,
                                weightKg = weightText.toFloatOrNull() ?: 75f,
                                activityLevel = activityLevel,
                                dietType = dietType,
                                goal = goal
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("save_profile_button")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "حفظ وتحديث الحسابات تلقائياً", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultMetric(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 11.sp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
