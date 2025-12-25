package io.timely.sdk

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

object Timely {
    private const val TAG = "TimelySDK"
    private const val PERMISSION_REQUEST_CODE = 1001

    private var baseUrl: String? = null
    private var appId: String? = null

    /**
     * Initialize Timely SDK
     * @param activity Current activity
     * @param baseUrl Your backend URL (e.g. https://your-app.onrender.com)
     * @param appId Unique ID for this application
     */
    fun initialize(activity: Activity, baseUrl: String, appId: String) {
        this.baseUrl = baseUrl
        this.appId = appId
        
        Log.d(TAG, "Initializing Timely SDK for App: $appId")
        
        // Request notification permission for Android 13+
        requestPermission(activity)
        
        // Fetch current token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d(TAG, "FCM Token: $token")
            reportTokenToBackend(token)
        }
    }

    private fun requestPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Requesting POST_NOTIFICATIONS permission")
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    fun reportTokenToBackend(token: String) {
        val urlString = baseUrl ?: return
        val currentAppId = appId ?: "unknown"
        
        Log.d(TAG, "Reporting token to $urlString for app $currentAppId")

        Thread {
            try {
                val url = java.net.URL("$urlString/register-token")
                val conn = url.openConnection() as java.net.HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.doOutput = true

                // Include appId in the payload
                val jsonInputString = "{\"token\": \"$token\", \"appId\": \"$currentAppId\"}"
                
                conn.outputStream.use { os ->
                    val input = jsonInputString.toByteArray(charset("utf-8"))
                    os.write(input, 0, input.size)
                }

                val responseCode = conn.responseCode
                Log.d(TAG, "Token report response code: $responseCode")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to report token", e)
            }
        }.start()
    }
}
