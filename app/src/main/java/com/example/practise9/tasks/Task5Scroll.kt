package com.example.practise9.tasks

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun Task5ScrollScreen(onBack: () -> Unit) {
    var mode by remember { mutableStateOf(1) } // 1: derivedStateOf, 2: Direct reading

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("5-topshiriq. Scroll & Analytics", style = MaterialTheme.typography.titleMedium)
                Button(onClick = onBack) { Text("Back") }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { mode = 1 }) { Text("derivedStateOf") }
                Button(onClick = { mode = 2 }) { Text("Direct Reading") }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(if (mode == 1) "Mode: derivedStateOf (Optimized)" else "Mode: Direct Reading (Unoptimized)")
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (mode == 1) {
                    OptimizedScrollContent()
                } else {
                    UnoptimizedScrollContent()
                }
            }
        }
    }
}

@Composable
fun OptimizedScrollContent() {
    val listState = rememberLazyListState()
    val showButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    var recomposeCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recomposeCount++
    }

    // snapshotFlow analytics integration
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 0 }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                Log.d("Task5Analytics", "Analytics event sent: Scrolled past first item (derivedStateOf)")
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text("Recomposition count: $recomposeCount")
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(100) { index ->
                    Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                        Text("Item #$index", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
        if (showButton) {
            Button(
                onClick = { /* Scroll back to top if needed */ },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Text("Top")
            }
        }
    }
}

@Composable
fun UnoptimizedScrollContent() {
    val listState = rememberLazyListState()
    // Direct reading without derivedStateOf
    val showButton = listState.firstVisibleItemIndex > 0

    var recomposeCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recomposeCount++
    }

    // snapshotFlow analytics integration
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 0 }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                Log.d("Task5Analytics", "Analytics event sent: Scrolled past first item (Direct)")
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text("Recomposition count: $recomposeCount (High!)")
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(100) { index ->
                    Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                        Text("Item #$index", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
        if (showButton) {
            Button(
                onClick = {},
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Text("Top")
            }
        }
    }
}
