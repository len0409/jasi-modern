package zone.jasimodern.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Base64
import android.util.Log

class LicensingService : Service() {

    companion object {
        private const val TAG = "LicensingService"
    }

    override fun onBind(intent: Intent?): IBinder? = licensingBinder

    private val licensingBinder = object : Binder() {
        override fun checkLicense(userId: Long, packageName: String, listener: LicenseListener?) {
            Log.d(TAG, "checkLicense called for user $userId")
            val result = generateLicenseResponse(packageName)
            listener?.verifyLicense(true, result.licenseText, result.signature)
        }

        override fun getLicenseVersion(): Int = 2
    }

    private data class LicenseResult(val licenseText: String, val signature: String)

    private fun generateLicenseResponse(packageName: String): LicenseResult {
        val timestamp = System.currentTimeMillis()
        val expiryTime = timestamp + (5L * 365 * 24 * 60 * 60 * 1000)
        val licenseData = "LICENSED$timestamp$packageName$expiryTime"
        val signature = Base64.encodeToString(licenseData.toByteArray(), Base64.DEFAULT)
        return LicenseResult(licenseData, signature)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "LicensingService created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "LicensingService destroyed")
    }
}
