package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ExerciseLogWidget
import com.example.ui.components.MacroGaugeCard
import com.example.ui.components.WaterTrackerWidget
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.SlateDark
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToFoodCalc: () -> Unit,
    onNavigateToDietsGuide: () -> Unit
) {
    val metabolicResults by viewModel.metabolicResults.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val consumedCal by viewModel.consumedCalories.collectAsState()
    val burnedCal by viewModel.burnedCalories.collectAsState()
    val consumedP by viewModel.consumedProtein.collectAsState()
    val consumedC by viewModel.consumedCarbs.collectAsState()
    val consumedF by viewModel.consumedFat.collectAsState()

    val currentWaterMl by viewModel.totalWaterMl.collectAsState()
    val activityLogs by viewModel.todayActivityLogs.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F6)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Daily Macro & Calorie Gauge Card
        item {
            MacroGaugeCard(
                metabolicResults = metabolicResults,
                consumedCalories = consumedCal,
                burnedCalories = burnedCal,
                consumedProtein = consumedP,
                consumedCarbs = consumedC,
                consumedFat = consumedF
            )
        }

        // 2. Quick Action Button -> Add Food Meal
        item {
            Button(
                onClick = onNavigateToFoodCalc,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("home_add_meal_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Meal", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "حاسبة وإضافة وجبة جديدة (قبل / بعد الطبخ)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // 3. Water Tracker Widget
        item {
            WaterTrackerWidget(
                currentWaterMl = currentWaterMl,
                targetWaterMl = userProfile.targetWaterMl,
                onAddWater = { amount -> viewModel.addWater(amount) }
            )
        }

        // 4. Physical Activity Logger Widget
        item {
            ExerciseLogWidget(
                activities = activityLogs,
                onLogActivity = { name, min, calPerMin ->
                    viewModel.logActivity(name, min, calPerMin)
                },
                onDeleteActivity = { id -> viewModel.deleteActivityLog(id) }
            )
        }

        // 5. Diet Advice Quick Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Diet Info",
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "توجيهات حمية (${userProfile.dietType})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF1E40AF)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = metabolicResults.dietAdviceSummary,
                        fontSize = 12.sp,
                        color = SlateDark,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
