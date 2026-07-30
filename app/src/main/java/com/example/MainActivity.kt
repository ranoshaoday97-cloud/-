package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.components.AppHeader
import com.example.ui.components.AppNavBar
import com.example.ui.components.NavTab
import com.example.ui.screens.DietsGuideScreen
import com.example.ui.screens.FoodCalculatorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyTheme
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    var selectedTab by remember { mutableStateOf(NavTab.HOME) }
                    val snackbarHostState = remember { SnackbarHostState() }
                    val coroutineScope = rememberCoroutineScope()
                    val userProfile by viewModel.userProfile.collectAsState()

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            AppHeader(
                                userName = userProfile.name,
                                dietType = userProfile.dietType,
                                onNotificationClick = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("حاسبة السعرات والحرق تعمل تلقائياً مع قاعدة بيانات الأطعمة")
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            AppNavBar(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it }
                            )
                        },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (selectedTab) {
                                NavTab.HOME -> HomeScreen(
                                    viewModel = viewModel,
                                    onNavigateToFoodCalc = { selectedTab = NavTab.FOOD_CALC },
                                    onNavigateToDietsGuide = { selectedTab = NavTab.DIETS_GUIDE }
                                )
                                NavTab.FOOD_CALC -> FoodCalculatorScreen(
                                    viewModel = viewModel
                                )
                                NavTab.DIETS_GUIDE -> DietsGuideScreen()
                                NavTab.PROFILE -> ProfileScreen(
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
