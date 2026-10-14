package uz.ttpu.framephases

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*

@Composable
fun SkippingDirectParent() {
    var count by remember { mutableIntStateOf(0) }
    SideEffect { Log.d("Skip", "Parent: composition") }
    Column {
        Button(onClick = { count++ }) { Text("Add") }
        DirectChild(count)
        Sibling()
    }
}

@Composable
private fun DirectChild(count: Int) {
    SideEffect { Log.d("Skip", "Child: composition") }
    Text("Count: $count")
}

@Composable
fun SkippingLambdaParent() {
    var count by remember { mutableIntStateOf(0) }
    SideEffect { Log.d("Skip", "Parent: composition") }
    Column {
        Button(onClick = { count++ }) { Text("Add") }
        LambdaChild(count = { count })
        Sibling()
    }
}

@Composable
private fun LambdaChild(count: () -> Int) {
    SideEffect { Log.d("Skip", "Child: composition") }
    Text("Count: ${count()}")
}

@Composable
private fun Sibling() {
    SideEffect { Log.d("Skip", "Sibling: composition") }
    Text("I never change")
}
