package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActivityLogEntity
import com.example.ui.theme.SlateDark
import kotlin.math.roundToInt

data class PresetExercise(
    val name: String,
    val caloriesPerMin: Double,
    val icon: ImageVector
)

val PRESET_EXERCISES = listOf(
    PresetExercise("مشي سريع", 5.0, Icons.Default.DirectionsWalk),
    PresetExercise("جري", 11.0, Icons.Default.DirectionsRun),
    PresetExercise("رفع أثقال / حديد", 7.0, Icons.Default.FitnessCenter),
    PresetExercise("سباحة", 9.0, Icons.Default.Pool),
    PresetExercise("ركوب دراجة", 8.0, Icons.Default.TwoWheeler)
)

@Composable
fun ExerciseLogWidget(
    activities: List<ActivityLogEntity>,
    onLogActivity: (String, Int, Double) -> Unit,
    onDeleteActivity: (Long) -> Unit
) {
    var selectedExercise by remember { mutableStateOf(PRESET_EXERCISES[0]) }
    var durationText by remember { mutableStateOf("30") }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = "Exercise Tracking",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تسجيل النشاط البدني والتمارين",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SlateDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Exercise Preset Selector Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(PRESET_EXERCISES) { ex ->
                    val isSelected = ex == selectedExercise
                    val bg = if (isSelected) Color(0xFFD1FAE5) else Color(0xFFF1F5F9)
                    val tint = if (isSelected) Color(0xFF047857) else Color(0xFF64748B)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bg)
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFF10B981) else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("exercise_chip_${ex.name}")
                    ) {
                        Icon(imageVector = ex.icon, contentDescription = ex.name, tint = tint, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = ex.name,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = tint
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it.filter { c -> c.isDigit() } },
                    label = { Text("المدة (بالدقائق)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        val duration = durationText.toIntOrNull() ?: 30
                        onLogActivity(selectedExercise.name, duration, selectedExercise.caloriesPerMin)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    modifier = Modifier
                        .height(54.dp)
                        .testTag("submit_exercise_button")
                ) {
                    Text("تسجيل التمرين", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            if (activities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "تمارين اليوم المسجلة:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateDark
                )
                Spacer(modifier = Modifier.height(6.dp))

                activities.forEach { act ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = act.activityName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SlateDark)
                            Text(text = "${act.durationMinutes} دقيقة • حرق ${act.caloriesBurned.roundToInt()} كالو", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                        IconButton(onClick = { onDeleteActivity(act.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
