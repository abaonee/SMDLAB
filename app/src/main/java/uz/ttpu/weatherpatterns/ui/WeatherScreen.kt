package uz.ttpu.weatherpatterns.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeatherScreen(
    status: String,
    onCitySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(status, style = MaterialTheme.typography.headlineSmall)
        listOf("Tashkent", "Samarkand", "Bukhara", "Atlantis").forEach { city ->
            Button(onClick = { onCitySelected(city) }) {
                Text(city)
            }
        }
    }
}
