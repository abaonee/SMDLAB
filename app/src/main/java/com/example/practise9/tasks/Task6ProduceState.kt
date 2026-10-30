package com.example.practise9.tasks

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

sealed interface ImageState {
    object Loading : ImageState
    data class Success(val url: String) : ImageState
    data class Error(val message: String) : ImageState
}

// Fake Repository
suspend fun fakeLoadNetworkImage(url: String): String {
    delay(500)
    if (url.endsWith(".png")) {
        return url
    } else {
        throw IllegalArgumentException("Invalid image format! Must end with .png")
    }
}

@Composable
fun loadNetworkImage(url: String): State<ImageState> {
    return produceState<ImageState>(initialValue = ImageState.Loading, key1 = url) {
        try {
            val result = fakeLoadNetworkImage(url)
            value = ImageState.Success(result)
        } catch (e: Exception) {
            value = ImageState.Error(e.localizedMessage ?: "Unknown error")
        } finally {
            Log.d("Task6ProduceState", "Producer cancelled or finished for url: $url")
        }
    }
}

@Composable
fun Task6ProduceStateScreen(onBack: () -> Unit) {
    var url by remember { mutableStateOf("https://example.com/image.png") }
    val imageState by loadNetworkImage(url)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("6-topshiriq. Bonus: produceState", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Current URL: $url")
            Spacer(modifier = Modifier.height(16.dp))

            when (val state = imageState) {
                is ImageState.Loading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Loading image...")
                }
                is ImageState.Success -> {
                    Text("Success! Loaded: ${state.url}", color = MaterialTheme.colorScheme.primary)
                }
                is ImageState.Error -> {
                    Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { url = "https://example.com/photo.png" }) {
                    Text("Load Valid (.png)")
                }
                Button(onClick = { url = "https://example.com/doc.txt" }) {
                    Text("Load Invalid (.txt)")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onBack) {
                Text("Orqaga / Back")
            }
        }
    }
}
