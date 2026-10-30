package com.example.practise9.tasks

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

const val SplashWaitTimeMillis = 2000L

@Composable
fun Task1SplashScreen(onBack: () -> Unit) {
    // Parent version counter updating every 500ms
    var version by remember { mutableStateOf(0) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            version++
        }
    }

    // Two variants side by side or selectable
    var activeVariant by remember { mutableStateOf(1) } // 1: Direct captured lambda, 2: rememberUpdatedState

    // Lambda that logs the current version
    val onTimeoutVariant1: () -> Unit = {
        Log.d("Task1Splash", "Variant 1 (Captured): Timeout triggered with version v$version")
    }

    val currentVersion = version
    val onTimeoutVariant2: () -> Unit = {
        Log.d("Task1Splash", "Variant 2 (UpdatedState): Timeout triggered with version v$currentVersion")
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("1-topshiriq. Splash ekrani", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Parent Version: v$version (updates every 500ms)")
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { activeVariant = 1 }) {
                Text("Run Variant 1 (Captured Lambda)")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { activeVariant = 2 }) {
                Text("Run Variant 2 (rememberUpdatedState)")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (activeVariant == 1) {
                Text("Running Variant 1...", color = MaterialTheme.colorScheme.primary)
                LandingScreenCaptured(onTimeout = onTimeoutVariant1)
            } else {
                Text("Running Variant 2...", color = MaterialTheme.colorScheme.primary)
                LandingScreenUpdated(onTimeout = onTimeoutVariant2)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onBack) {
                Text("Orqaga / Back")
            }
        }
    }
}

@Composable
fun LandingScreenCaptured(onTimeout: () -> Unit) {
    // WARNING: LaunchedEffect(true) captures the initial lambda (v0).
    // If onTimeout is used as key, it would restart every 500ms when lambda changes, ruining the 2s wait timer.
    LaunchedEffect(true) {
        delay(SplashWaitTimeMillis)
        onTimeout()
    }
    Text("LandingScreen (Captured): Waiting 2s... Check Logcat for v0.")
}

@Composable
fun LandingScreenUpdated(onTimeout: () -> Unit) {
    val currentOnTimeout by rememberUpdatedState(onTimeout)
    LaunchedEffect(true) {
        delay(SplashWaitTimeMillis)
        currentOnTimeout()
    }
    Text("LandingScreen (rememberUpdatedState): Waiting 2s... Check Logcat for latest version.")
}
