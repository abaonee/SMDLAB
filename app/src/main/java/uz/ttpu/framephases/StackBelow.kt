package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp

@Composable
fun StackBelow(
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp,
    content: @Composable () -> Unit,
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.mapIndexed { index, measurable ->
            Log.d("StackBelow", "measure child $index")
            measurable.measure(loose)
        }
        val spacingPx = spacing.roundToPx()
        val width = placeables.maxOfOrNull { it.width } ?: 0
        val childrenHeight = placeables.sumOf { it.height }
        val gapsHeight = spacingPx * (placeables.size - 1).coerceAtLeast(0)
        val height = childrenHeight + gapsHeight
        layout(constraints.constrainWidth(width), constraints.constrainHeight(height)) {
            Log.d("StackBelow", "place children")
            var y = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(0, y)
                y += placeable.height + spacingPx
            }
        }
    }
}

@Composable
fun StackBelowDemo() {
    StackBelow(Modifier.fillMaxWidth(), spacing = 8.dp) {
        Box(
            Modifier.fillMaxWidth().height(120.dp)
                .background(Color(0xFF6650A4))
        )
        Text("I'm below the image")
    }
}
