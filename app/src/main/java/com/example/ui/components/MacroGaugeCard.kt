package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repository.MetabolicResults
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SlateDark
import kotlin.math.roundToInt

@Composable
fun MacroGaugeCard(
    metabolicResults: MetabolicResults,
    consumedCalories: Double,
    burnedCalories: Double,
    consumedProtein: Double,
    consumedCarbs: Double,
    consumedFat: Double
) {
    val targetCalories = metabolicResults.targetCalories
    val remainingCalories = (targetCalories - consumedCalories + burnedCalories).coerceAtLeast(0.0)
    val calProgress = if (targetCalories > 0) (consumedCalories / targetCalories).toFloat().coerceIn(0f, 1f) else 0f

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Calorie Target",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ملخص السعرات والماكروز اليومي",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SlateDark
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "المتبقي: ${remainingCalories.roundToInt()} كالو",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetricItem(
                    title = "المستهدف",
                    value = "${targetCalories.roundToInt()}",
                    unit = "كالو",
                    color = IndigoPrimary
                )
                Text("=", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                MetricItem(
                    title = "المأكول",
                    value = "${consumedCalories.roundToInt()}",
                    unit = "كالو",
                    color = EmeraldSuccess
                )
                Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                MetricItem(
                    title = "المحروق",
                    value = "${burnedCalories.roundToInt()}",
                    unit = "كالو",
                    color = Color(0xFFEF4444)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Calorie Progress Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "نسبة الإنجاز اليومية",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(calProgress * 100).roundToInt()}%",
                        fontSize = 12.sp,
                        color = EmeraldSuccess,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { calProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape),
                    color = EmeraldSuccess,
                    trackColor = Color(0xFFE2E8F0)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BMR & TDEE Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "BMR",
                        tint = Color(0xFF8B5CF6),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "الحرق الأساسي (BMR): ${metabolicResults.bmr.roundToInt()}",
                        fontSize = 11.sp,
                        color = SlateDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "TDEE",
                        tint = Color(0xFF06B6D4),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "الحرق اليومي (TDEE): ${metabolicResults.tdee.roundToInt()}",
                        fontSize = 11.sp,
                        color = SlateDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Macro Progress Bars (Protein, Carbs, Fat)
            Text(
                text = "المغذيات الكبرى (الماكروز):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SlateDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            MacroRow(
                label = "البروتين",
                current = consumedProtein,
                target = metabolicResults.targetProteinGrams,
                color = Color(0xFF3B82F6)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MacroRow(
                label = "الكاربوهيدرات",
                current = consumedCarbs,
                target = metabolicResults.targetCarbsGrams,
                color = Color(0xFFF59E0B)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MacroRow(
                label = "الدهون",
                current = consumedFat,
                target = metabolicResults.targetFatGrams,
                color = Color(0xFFEC4899)
            )
        }
    }
}

@Composable
private fun MetricItem(
    title: String,
    value: String,
    unit: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 11.sp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = unit, fontSize = 10.sp, color = color, modifier = Modifier.padding(bottom = 2.dp))
        }
    }
}

@Composable
private fun MacroRow(
    label: String,
    current: Double,
    target: Double,
    color: Color
) {
    val progress = if (target > 0) (current / target).toFloat().coerceIn(0f, 1f) else 0f
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 11.sp, color = SlateDark, fontWeight = FontWeight.Medium)
            Text(
                text = "${current.roundToInt()} / ${target.roundToInt()} جم",
                fontSize = 11.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = color,
            trackColor = Color(0xFFF1F5F9)
        )
    }
}
