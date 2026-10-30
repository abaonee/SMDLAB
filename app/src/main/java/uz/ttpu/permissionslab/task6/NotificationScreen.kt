package uz.ttpu.permissionslab.task6

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

@Composable
fun NotificationScreen() {
    val context = LocalContext.current
    var isGranted by remember { mutableStateOf(checkNotificationPermission(context)) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        isGranted = checkNotificationPermission(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isGranted = granted
        if (granted) {
            sendTestNotification(context)
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
            text = "6-Topshiriq (Bonus): Bildirishnoma Ruxsati",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Android 13 (API 33+) da POST_NOTIFICATIONS runtime ruxsatnomasi. API 33 dan past versiyalarda avtomatik ruxsat berilgan bo'ladi.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isGranted) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isGranted) "✓ Bildirishnoma Ruxsati Berilgan" else "⚠ Bildirishnoma Ruxsati Yo'q",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isGranted) Color(0xFF2E7D32) else Color(0xFFC62828),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        "Joriy qurilma API ${Build.VERSION.SDK_INT} (Android 13+): Runtime dialog talab qilinadi."
                    else
                        "Joriy qurilma API ${Build.VERSION.SDK_INT} (< API 33): O'rnatish vaqtida ruxsat berilgan.",
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (!isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Button(
                        onClick = {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    ) {
                        Text("Bildirishnoma Ruxsatini So'rash")
                    }
                } else {
                    Button(
                        onClick = {
                            sendTestNotification(context)
                        }
                    ) {
                        Text("Test Bildirishnomasini Yuborish")
                    }
                }
            }
        }
    }
}

private fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // API < 33 versiyalarda bildirishnoma ruxsati avtomatik berilgan
    }
}

private fun sendTestNotification(context: Context) {
    val channelId = "permissions_lab_channel"
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Permissions Lab Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "PermissionsLab test bildirishnomalari kanali"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Permissions Lab")
        .setContentText("Muvaffaqiyatli! Bildirishnoma ruxsati ishlamoqda.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(1001, notification)
}
