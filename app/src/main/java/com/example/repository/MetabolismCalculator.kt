package com.example.repository

import com.example.data.UserEntity
import kotlin.math.roundToInt

data class MetabolicResults(
    val bmr: Double, // Basal Metabolic Rate (الحرق الأساسي)
    val tdee: Double, // Total Daily Energy Expenditure (الحرق اليومي الكلي)
    val targetCalories: Double, // Target Daily Intake based on Goal
    val bmi: Double, // Body Mass Index (مؤشر كتلة الجسم)
    val bmiCategory: String,
    val targetProteinGrams: Double,
    val targetCarbsGrams: Double,
    val targetFatGrams: Double,
    val dietAdviceSummary: String,
    val workoutAdvice: String
)

object MetabolismCalculator {

    fun calculate(user: UserEntity): MetabolicResults {
        val weight = user.weightKg.toDouble()
        val height = user.heightCm.toDouble()
        val age = user.age.toDouble()

        // 1. Mifflin-St Jeor BMR Formula
        val bmr = if (user.gender == "رجل") {
            (10.0 * weight) + (6.25 * height) - (5.0 * age) + 5.0
        } else {
            (10.0 * weight) + (6.25 * height) - (5.0 * age) - 161.0
        }

        // 2. TDEE Activity Multiplier
        val activityMultiplier = when (user.activityLevel) {
            "خامل" -> 1.2
            "خفيف" -> 1.375
            "نشاط متوسط" -> 1.55
            "عالي" -> 1.725
            "شديد" -> 1.9
            else -> 1.55
        }
        val tdee = bmr * activityMultiplier

        // 3. Goal Adjustment
        val targetCalories = when (user.goal) {
            "خسارة وزن" -> (tdee - 500.0).coerceAtLeast(1200.0)
            "بناء عضلات" -> tdee + 350.0
            else -> tdee // تثبيت الوزن
        }

        // 4. BMI Calculation
        val heightMeters = height / 100.0
        val bmi = if (heightMeters > 0) weight / (heightMeters * heightMeters) else 22.0

        val bmiCategory = when {
            bmi < 18.5 -> "تحت الوزن الطبيعي"
            bmi in 18.5..24.9 -> "وزن مثالي وصحي"
            bmi in 25.0..29.9 -> "زيادة طفيفة في الوزن"
            else -> "سمنة - يتطلب برنامج محدد"
        }

        // 5. Diet Macro Ratios
        val (proteinPct, carbsPct, fatPct) = when (user.dietType) {
            "كيتو" -> Triple(0.25, 0.05, 0.70)
            "لو كارب" -> Triple(0.35, 0.20, 0.45)
            "كارنيفور" -> Triple(0.50, 0.00, 0.50)
            else -> Triple(0.30, 0.45, 0.25) // حساب السعرات المرن
        }

        // Protein = 4 kcal/g, Carbs = 4 kcal/g, Fat = 9 kcal/g
        val targetProteinGrams = (targetCalories * proteinPct) / 4.0
        val targetCarbsGrams = (targetCalories * carbsPct) / 4.0
        val targetFatGrams = (targetCalories * fatPct) / 9.0

        // 6. Diet Advice Summary
        val dietAdvice = when (user.dietType) {
            "كيتو" -> "حمية الكيتو دايت تعتمد على قطع النشويات (أقل من 20-30 جرام) والاعتماد على الدهون الصحية كمصدر رئيسي للطاقة. ركز على البيض، زيت الزيتون، السلمون، والأفوكادو وشرب الكثير من الماء مع الملح."
            "لو كارب" -> "حمية منخفضة الكاربوهيدرات الممتازة لحرق الدهون! حصر الكاربوهيدرات في الشوفان والبطاطس المسلوقة والخضروات، وزيادة البروتين لحماية الكتلة العضلية."
            "كارنيفور" -> "حمية الكارنيفور (الحمية الحيوانية النقية): تعتمد حصرياً على اللحوم الحمراء، الدواجن، البيض، والأسماك مع الزبدة الحيوانية والملح الشديد، بدون أي نشويات أو نباتات."
            else -> "حمية حساب السعرات والمرونة: يمكنك تناول جميع أنواع الأطعمة بشرط عدم تجاوز حد السعرات اليومية ($targetCalories سعر) وتغطية الاحتياج اليومي من البروتين."
        }

        // 7. Dynamic Workout Advice tailored to Weight & Height & BMI
        val workoutAdvice = generateWorkoutAdvice(weight, height, bmi, user.age, user.goal, user.gender)

        return MetabolicResults(
            bmr = (bmr * 10).roundToInt() / 10.0,
            tdee = (tdee * 10).roundToInt() / 10.0,
            targetCalories = (targetCalories * 10).roundToInt() / 10.0,
            bmi = (bmi * 10).roundToInt() / 10.0,
            bmiCategory = bmiCategory,
            targetProteinGrams = (targetProteinGrams * 10).roundToInt() / 10.0,
            targetCarbsGrams = (targetCarbsGrams * 10).roundToInt() / 10.0,
            targetFatGrams = (targetFatGrams * 10).roundToInt() / 10.0,
            dietAdviceSummary = dietAdvice,
            workoutAdvice = workoutAdvice
        )
    }

    private fun generateWorkoutAdvice(
        weight: Double,
        height: Double,
        bmi: Double,
        age: Int,
        goal: String,
        gender: String
    ): String {
        val adviceList = mutableListOf<String>()

        if (bmi >= 30.0) {
            adviceList.add("• نظراً لأن الوزن ($weight كجم) بالنسبة للطول ($height سم) يعطي مؤشر سمنة، يُنصح بالبدء بتمارين خفيفة الضغط على المفاصل مثل المشي السريع لمدة 30-40 دقيقة، أو السباحة والدراجة الثابتة لحماية الركبتين.")
            adviceList.add("• أضف تمارين المقاومة الخفيفة باستخدام الأجهزة الرياضية 3 أيام أسبوعياً لبناء العضلات وزيادة معدل الحرق.")
        } else if (bmi >= 25.0) {
            adviceList.add("• بالنسبة لوزنك ($weight كجم) وطولك ($height سم)، أفضل جدول هو الجمع بين 40 دقيقة تمارين مقاومة وحديد، تليها 20 دقيقة كارديو متوسط الشدة (مثل المشي المائل على السير).")
        } else {
            if (goal == "بناء عضلات") {
                adviceList.add("• وزنك متناسق مع طولك! للتركيز على بناء الكتلة العضلية، ركز على رفع الأوزان التصاعدية (Progressive Overload) 4-5 أيام أسبوعياً مع تقسيم العضلات (دفع/سحب/أرجل).")
                adviceList.add("• قلل الكارديو الشديد واكتفِ بـ 15-20 دقيقة للإحماء وصحة القلب.")
            } else {
                adviceList.add("• جيدة جداً! حافظ على لياقتك بممارسة تمارين المقاومة 3-4 أيام أسبوعياً مع 30 دقيقة مشي يومي أو تمارين HIIT مرتين أسبوعياً.")
            }
        }

        if (age > 45) {
            adviceList.add("• اهتم جداً بالإحماء والتمدد (Dynamic Stretching) لمدة 10 دقائق قبل التمرين وخذ قسطاً كافياً من النوم للاستشفاء العضلي.")
        } else {
            adviceList.add("• احرص على التنشيط العضلي وشرب الماء بكثرة (أكثر من 2.5 لتر) قبل وأثناء التمرين لمنع التشنجات.")
        }

        return adviceList.joinToString("\n\n")
    }
}
