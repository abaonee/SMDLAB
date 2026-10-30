package uz.ttpu.permissionslab.task3

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    var isGranted by remember { mutableStateOf(checkCameraPermission(context)) }
    var shouldShowRationale by remember {
        mutableStateOf(
            activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
            } ?: false
        )
    }
    var hasRequestedAtLeastOnce by remember { mutableStateOf(false) }

    fun updatePermissionState() {
        isGranted = checkCameraPermission(context)
        shouldShowRationale = activity?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
        } ?: false
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        updatePermissionState()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        hasRequestedAtLeastOnce = true
        updatePermissionState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "3-Topshiriq: Kamera Ruxsatnomasi",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Android 6.0 (API 23) dan past versiyalarda ruxsat o'rnatish vaqtida beriladi
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("API < 23: Kamera ruxsati o'rnatish vaqtida (install-time) berilgan.")
                }
            }
        } else when {
            // 1-holat: Ruxsat berilgan
            isGranted -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✓ Kamera ruxsati berilgan!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Rasmga olish va kamera funksiyalari ishga tayyor.",
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            // 2-holat: Rationale ko'rsatilishi kerak (1 marta rad etilgan)
            shouldShowRationale -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ℹ Nega kamera ruxsati kerak?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ilovada rasmga olish va profilga rasm joylash uchun kamera ruxsati zarur. Iltimos, ruxsat bering.",
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { launcher.launch(Manifest.permission.CAMERA) }
                        ) {
                            Text("Ruxsat So'rash (Qayta)")
                        }
                    }
                }
            }

            // 3-holat: Ruxsat yo'q (Hali so'ralmagan YOKI 2 marta rad etilgan)
            else -> {
                val isPermanentlyDenied = hasRequestedAtLeastOnce && !shouldShowRationale

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPermanentlyDenied) Color(0xFFFFEBEE) else Color(0xFFE3F2FD)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isPermanentlyDenied) "⚠ Ruxsat Rad Etilgan (Sozlamalardan yoqing)" else "Kamera ruxsati so'ralmagan",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPermanentlyDenied) Color(0xFFC62828) else Color(0xFF1565C0),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isPermanentlyDenied)
                                "Kamera ruxsati ikki marta rad etildi. Ruxsat berish uchun Sozlamalar menyusiga o'ting."
                            else
                                "Tugmani bosib kamera ruxsatini bering.",
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (isPermanentlyDenied) {
                            Button(
                                onClick = {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                    context.startActivity(intent)
                                }
                            ) {
                                Text("Sozlamalarni Ochish")
                            }
                        } else {
                            Button(
                                onClick = { launcher.launch(Manifest.permission.CAMERA) }
                            ) {
                                Text("Ruxsat So'rash")
                            }
                        }
                    }
                }
            }
        }
    }
}
