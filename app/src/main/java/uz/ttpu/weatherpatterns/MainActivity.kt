package uz.ttpu.weatherpatterns

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.ttpu.weatherpatterns.mvc.view.MvcActivity
import uz.ttpu.weatherpatterns.mvp.view.MvpActivity
import uz.ttpu.weatherpatterns.mvi.view.MviActivity
import uz.ttpu.weatherpatterns.mvvm.view.MvvmActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Launcher { target -> startActivity(Intent(this, target)) }
            }
        }
    }
}

@Composable
private fun Launcher(onOpen: (Class<*>) -> Unit) {
    Column(
        Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Weather: four patterns",
            style = MaterialTheme.typography.headlineSmall
        )
        Button(onClick = { onOpen(MvcActivity::class.java) }) { Text("MVC") }
        Button(onClick = { onOpen(MvpActivity::class.java) }) { Text("MVP") }
        Button(onClick = { onOpen(MvvmActivity::class.java) }) { Text("MVVM") }
        Button(onClick = { onOpen(MviActivity::class.java) }) { Text("MVI") }
    }
}
