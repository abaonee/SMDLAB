package com.example.practise9.tasks

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun Task3LifecycleScreen(onBack: () -> Unit) {
    var showComponent by remember { mutableStateOf(true) }
    var addCount by remember { mutableIntStateOf(0) }
    var removeCount by remember { mutableIntStateOf(0) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("3-topshiriq. Lifecycle logger", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Show Lifecycle Component:")
                Switch(
                    checked = showComponent,
                    onCheckedChange = { showComponent = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Add Observer count: $addCount | Remove Observer count: $removeCount")
            Spacer(modifier = Modifier.height(16.dp))

            if (showComponent) {
                LifecycleObserverComponent(
                    onAdd = { addCount++ },
                    onRemove = { removeCount++ }
                )
            } else {
                Text("Component is hidden (observer removed).", color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onBack) {
                Text("Orqaga / Back")
            }
        }
    }
}

@Composable
fun LifecycleObserverComponent(onAdd: () -> Unit, onRemove: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> Log.d("Task3Lifecycle", "Lifecycle ON_START observed")
                Lifecycle.Event.ON_STOP -> Log.d("Task3Lifecycle", "Lifecycle ON_STOP observed")
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        Log.d("Task3Lifecycle", "addObserver called")
        onAdd()

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            Log.d("Task3Lifecycle", "removeObserver called (onDispose)")
            onRemove()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            Text("Active LifecycleObserver Composable")
        }
    }
}
