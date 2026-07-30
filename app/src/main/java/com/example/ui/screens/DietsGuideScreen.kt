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
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SlateDark

@Composable
fun DietsGuideScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F6)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Title
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = EmeraldSuccess,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "دليل الحميات والتغدية وإسرار الطبخ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = SlateDark
                    )
                    Text(
                        text = "نصائح وإرشادات شاملة لكل نظام غذائي وحساب السعرات الدقيق",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Cooking Science Card: Raw vs Cooked
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TipsAndUpdates,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "💡 السر العلمي: الفرق بين وزن الطعام قبل وبعد الطبخ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• اللحوم والدجاج والأسماك: تفقد السوائل والماء أثناء الطهي الشواء أو السلق، مما يقلل وزنها. لذلك فإن 100 جرام دجاج مطبوخ تحتوي سعرات وأحماض أمينية أعلى من 100 جرام دجاج نيء!",
                        fontSize = 12.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• النشويات (الأرز، المعكرونة، الشوفان، العدس): تمتص الماء أثناء الطهي فيزيد وزنها وحجمها لـ 2.5 إلى 3 أضعاف. لذلك فإن 100 جرام أرز مطبوخ يحتوي سعرات أقل بكثير من 100 جرام أرز نيء!",
                        fontSize = 12.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Keto Guide
        item {
            DietGuideCard(
                title = "1. حمية الكيتو دايت (Keto Diet 🥑)",
                color = Color(0xFF059669),
                badge = "70% دهون • 25% بروتين • 5% كارب",
                description = "حمية عالية الدهون ومنخفضة الكاربوهيدرات جداً تجبر الجسم على إفراز الكيتونات وحرق الدهون المخزنة بدلاً من الجلوكوز.",
                allowedFoods = "البيض، اللحوم والأسماك، زيت الزيتون والزبدة، الأفوكادو، الجبن، الخضار الورقية كالسبانخ.",
                forbiddenFoods = "الأرز، المعكرونة، الخبز، السكر، الفواكه السكرية، البطاطس والمشروبات الغازية.",
                tips = "احرص على تعويض الصوديوم والبوتاسيوم بشرب ماء مملح لمنع أعراض كيتو فلو."
            )
        }

        // Low Carb Guide
        item {
            DietGuideCard(
                title = "2. حمية اللو كارب (Low Carb 🥩)",
                color = IndigoPrimary,
                badge = "35% بروتين • 20% كارب • 45% دهون",
                description = "نظام متوازن وممتاز جداً لخسارة الدهون مع الحفاظ على الأداء الرياضي والكتلة العضلية بدون حرمان قاسي.",
                allowedFoods = "صدور الدجاج، اللحوم الحمراء، الأسماك، الشوفان، البطاطس المسلوقة، الخضار، المكسرات والبيض.",
                forbiddenFoods = "المقليات، السكريات المضافة، المشروبات المحلاة والمعجنات البيضاء.",
                tips = "تناول كربوهيدراتك مع وجبة الإفطار وقبل/بعد التمرين مباشرة لتزويد العضلات باللياقة."
            )
        }

        // Carnivore Guide
        item {
            DietGuideCard(
                title = "3. حمية الكارنيفور الحيوانية (Carnivore / كارنو 🍖)",
                color = Color(0xFFDC2626),
                badge = "50% بروتين حيواني • 50% دهون حيوانية • 0% كارب",
                description = "حمية استبعاد حادة تعتمد حصرياً على المنتجات الحيوانية وتساعد في شفاء الهضم والالتهابات وتخسيس الوزن السريع.",
                allowedFoods = "اللحوم الحمراء البقرية والضأن، كبدة وقوانص، الدواجن، الأسماك، البيض، السمن والزبدة المصفاة والملح.",
                forbiddenFoods = "جميع النباتات والخضروات والفواكه والمكسرات والزيوت النباتية والسكر.",
                tips = "اهتم بشرائح اللحم الغنية بالدهون وشرب كميات وفيرة من الماء والملح البحري."
            )
        }

        // Calorie Counting Flexible Diet Guide
        item {
            DietGuideCard(
                title = "4. نظام حساب السعرات والمرونة (IIFYM 🎯)",
                color = Color(0xFFD97706),
                badge = "مرونة شاملة بحسب الماكروز",
                description = "طريقة علمية قائمة على قانون الطاقة: إذا كان استهلاكك أقل من حرقك اليومي (TDEE) ستخسر وزناً بغض النظر عن نوع الطعام.",
                allowedFoods = "جميع أنواع الأطعمة الصحية وغير الصحية بشرط الموازنة وعدم تجاوز حد السعرات اليومي.",
                forbiddenFoods = "لا يوجد طعام ممنوع، ولكن الأطعمة الصحية تعطي شبع أعلى بفضل الألياف والبروتين.",
                tips = "استخدم حاسبة الطعام للتأكد من الموزون قبل أو بعد الطبخ بدقة."
            )
        }
    }
}

@Composable
fun DietGuideCard(
    title: String,
    color: Color,
    badge: String,
    description: String,
    allowedFoods: String,
    forbiddenFoods: String,
    tips: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = color)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = badge, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = description, fontSize = 12.sp, color = SlateDark, lineHeight = 18.sp)

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "✅ الأطعمة الموصى بها:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
            Text(text = allowedFoods, fontSize = 11.sp, color = Color(0xFF475569))

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "❌ الأطعمة الممنوعة/المحدودة:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB91C1C))
            Text(text = forbiddenFoods, fontSize = 11.sp, color = Color(0xFF475569))

            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Restaurant, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "نصيحة ذهبية: $tips", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = color)
            }
        }
    }
}
