package uz.ttpu.framephases

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent() {
    var selectedDemo by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedDemo ?: "Practice 7 — FramePhases") },
                navigationIcon = {
                    if (selectedDemo != null) {
                        TextButton(onClick = { selectedDemo = null }) {
                            Text("← Back")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedDemo) {
                "4. LectureTree" -> LectureTree()
                "5. PaddingDemo" -> PaddingDemo()
                "6. OffsetDemo" -> OffsetDemo()
                "7. ColorDemo" -> ColorDemo()
                "9. ParallaxSlow" -> ParallaxSlow()
                "9. ParallaxFast" -> ParallaxFast()
                "10. ScrollToTopNaive" -> ScrollToTopNaive()
                "10. ScrollToTopDerived" -> ScrollToTopDerived()
                "11. SkippingDirect" -> SkippingDirectParent()
                "11. SkippingLambda" -> SkippingLambdaParent()
                "13. CyclicDemo" -> CyclicDemo()
                "13. CyclicFixed" -> CyclicFixed()
                "14. StackBelow" -> StackBelowDemo()
                else -> DemoList(onSelect = { selectedDemo = it })
            }
        }
    }
}

@Composable
private fun DemoList(onSelect: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val demos = listOf(
            "4. LectureTree",
            "5. PaddingDemo",
            "6. OffsetDemo",
            "7. ColorDemo",
            "9. ParallaxSlow",
            "9. ParallaxFast",
            "10. ScrollToTopNaive",
            "10. ScrollToTopDerived",
            "11. SkippingDirect",
            "11. SkippingLambda",
            "13. CyclicDemo",
            "13. CyclicFixed",
            "14. StackBelow"
        )
        demos.forEach { demo ->
            Button(
                onClick = { onSelect(demo) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(demo)
            }
        }
    }
}
