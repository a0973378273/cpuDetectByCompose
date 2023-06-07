package com.bean.cpucompose

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
//        val register = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//            val granted =
//                checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) ==
//                        PackageManager.PERMISSION_GRANTED
//            Log.d(localClassName, "onCreate: $granted")
//        }
//        register.launch(android.Manifest.permission.PACKAGE_USAGE_STATS)
//        val granted =
//            checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) ==
//                    PackageManager.PERMISSION_GRANTED
//        Log.d(localClassName, "onCreate: $granted")
//        if (!granted) {
//            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//            startActivity(intent)
//        }
    }
}

