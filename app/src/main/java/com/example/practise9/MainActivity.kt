package com.example.practise9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practise9.tasks.*
import com.example.practise9.ui.theme.Practise9Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practise9Theme {
                SideEffectsLabApp()
            }
        }
    }
}

@Composable
fun SideEffectsLabApp() {
    var currentScreen by remember { mutableStateOf("home") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (currentScreen) {
            "home" -> HomeScreen(onSelectScreen = { currentScreen = it })
            "task1" -> Task1SplashScreen(onBack = { currentScreen = "home" })
            "task2" -> Task2SnackbarScreen(onBack = { currentScreen = "home" })
            "task3" -> Task3LifecycleScreen(onBack = { currentScreen = "home" })
            "task4" -> Task4AnalyticsScreen(onBack = { currentScreen = "home" })
            "task5" -> Task5ScrollScreen(onBack = { currentScreen = "home" })
            "task6" -> Task6ProduceStateScreen(onBack = { currentScreen = "home" })
        }
    }
}

@Composable
fun HomeScreen(onSelectScreen: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("SideEffectsLab — 13-Ma'ruza", style = MaterialTheme.typography.headlineSmall)
        Text("Jetpack Compose Side-effects", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("A qism: Nazariy savollar PART_A_ANSWERS.md faylida yozilgan.", 
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                TaskButton("1-topshiriq. Splash ekrani (LaunchedEffect & rememberUpdatedState)") {
                    onSelectScreen("task1")
                }
            }
            item {
                TaskButton("2-topshiriq. Snackbar tugmasi (rememberCoroutineScope)") {
                    onSelectScreen("task2")
                }
            }
            item {
                TaskButton("3-topshiriq. Lifecycle logger (DisposableEffect)") {
                    onSelectScreen("task3")
                }
            }
            item {
                TaskButton("4-topshiriq. SideEffect va analytics") {
                    onSelectScreen("task4")
                }
            }
            item {
                TaskButton("5-topshiriq. Scroll: derivedStateOf & snapshotFlow") {
                    onSelectScreen("task5")
                }
            }
            item {
                TaskButton("6-topshiriq. Bonus: produceState") {
                    onSelectScreen("task6")
                }
            }
        }
    }
}

@Composable
fun TaskButton(title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title, modifier = Modifier.padding(8.dp))
    }
}
