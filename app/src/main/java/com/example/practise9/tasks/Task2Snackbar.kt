package com.example.practise9.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Task2SnackbarScreen(onBack: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var clickCount by remember { mutableIntStateOf(0) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("2-topshiriq. Snackbar tugmasi", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Snackbar tugmasini bosing:")
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                clickCount++
                // Qoida: "Foydalanuvchi biror narsa qilgani uchun (event) boshlanadigan ish"
                // uchun rememberCoroutineScope ishlatiladi.
                // Nega LaunchedEffect bu yerda ishlamaydi?
                // Chunki LaunchedEffect faqat Composition paytida avtomatik ishga tushadi, 
                // o'quvchi tugmani bosgandagi callback'da ishlata olmaydi.
                scope.launch {
                    snackbarHostState.showSnackbar("Salom #${clickCount}! Snackbar ko'rsatilmoqda.")
                }
            }) {
                Text("Show Snackbar (#$clickCount)")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Izoh: Snackbar ko'rinib turganda orqaga qaytsangiz, rememberCoroutineScope " +
                    "avtomatik cancel bo'lib, xotira oqishining oldini oladi.")

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onBack) {
                Text("Orqaga / Back")
            }
        }
    }
}
