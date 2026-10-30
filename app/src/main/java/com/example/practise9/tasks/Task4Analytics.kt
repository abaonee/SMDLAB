package com.example.practise9.tasks

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Simple outer Analytics object
object DummyAnalytics {
    var userType: String = "Guest"
}

@Composable
fun Task4AnalyticsScreen(onBack: () -> Unit) {
    var userType by remember { mutableStateOf("Guest") }
    var sideEffectRunCount by remember { mutableIntStateOf(0) }
    var regularCounter by remember { mutableIntStateOf(0) }

    // SideEffect: runs after every successful recomposition commit
    SideEffect {
        sideEffectRunCount++
        DummyAnalytics.userType = userType
        Log.d("Task4Analytics", "SideEffect executed! Run count: $sideEffectRunCount, Analytics.userType set to: $userType")
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("4-topshiriq. SideEffect va analytics", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Current userType state: $userType")
            Text("SideEffect execution count: $sideEffectRunCount")
            Text("Regular Recomposition Counter: $regularCounter")
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { userType = "FreeUser" }) {
                    Text("Set Free")
                }
                Button(onClick = { userType = "PremiumUser" }) {
                    Text("Set Premium")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { regularCounter++ }) {
                Text("Trigger Recomposition (Counter++)")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Izoh: Holat o'zgarmasa ham (masalan, regularCounter oshganda) " +
                    "recomposition bo'lgandan keyin SideEffect ishlaydi, chunki u har bir muvaffaqiyatli commit'dan so'ng ishga tushadi.")

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onBack) {
                Text("Orqaga / Back")
            }
        }
    }
}
