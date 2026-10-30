package uz.ttpu.permissionslab.task5

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

enum class LocationPermissionStatus {
    FINE,      // Aniq (ACCESS_FINE_LOCATION)
    COARSE,    // Taxminiy (ACCESS_COARSE_LOCATION)
    NONE       // Yo'q (None)
}

@Composable
fun LocationScreen() {
    val context = LocalContext.current
    var status by remember { mutableStateOf(checkLocationPermission(context)) }

    fun updateStatus() {
        status = checkLocationPermission(context)
    }

    // ON_RESUME: Har safar ekran aktivlashganda ("Only this time" bir martalik ruxsat bekor bo'lganini aniqlash uchun)
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        updateStatus()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultPermissions: Map<String, Boolean> ->
        val fineGranted = resultPermissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = resultPermissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        status = when {
            fineGranted -> LocationPermissionStatus.FINE
            coarseGranted -> LocationPermissionStatus.COARSE
            else -> LocationPermissionStatus.NONE
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "5-Topshiriq: Bir Martalik Joylashuv",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "RequestMultiplePermissions kontrakti orqali Aniq va Taxminiy joylashuv so'raladi. ON_RESUME har safar bir martalik ruxsatni qayta tekshiradi.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        val (cardBg, statusTitle, statusDesc, textColor) = when (status) {
            LocationPermissionStatus.FINE -> Quadruple(
                Color(0xFFE8F5E9),
                "Aniq Joylashuv (Fine)",
                "ACCESS_FINE_LOCATION va ACCESS_COARSE_LOCATION berilgan.",
                Color(0xFF2E7D32)
            )
            LocationPermissionStatus.COARSE -> Quadruple(
                Color(0xFFFFF3E0),
                "Taxminiy Joylashuv (Coarse)",
                "Faqat ACCESS_COARSE_LOCATION berilgan.",
                Color(0xFFE65100)
            )
            LocationPermissionStatus.NONE -> Quadruple(
                Color(0xFFFFEBEE),
                "Joylashuv Ruxsati Yo'q",
                "Joylashuv ruxsatnomalari berilmagan yoki bekor qilingan.",
                Color(0xFFC62828)
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = statusTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = statusDesc,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        launcher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                ) {
                    Text("Joylashuv Ruxsatini So'rash")
                }
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

private fun checkLocationPermission(context: Context): LocationPermissionStatus {
    val fineGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return when {
        fineGranted -> LocationPermissionStatus.FINE
        coarseGranted -> LocationPermissionStatus.COARSE
        else -> LocationPermissionStatus.NONE
    }
}
