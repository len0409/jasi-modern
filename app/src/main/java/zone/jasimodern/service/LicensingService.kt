package zone.jasimodern.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class LicensingService : Service() {
    
    companion object {
        private const val TAG = "LicensingService"
        private const val LICENSED = "LICENSED"
    }
    
    private val binder = LicensingBinder()
    
    inner class LicensingBinder : ILicensingService.Stub() {
        
        override fun checkLicense(
            userId: Long,
            packageName: String,
            listener: ILicensingServiceResponseListener?
        ) {
            Log.d(TAG, "checkLicense called for user $userId")
            
            if (listener != null) {
                val result = generateLicenseResponse(packageName)
                listener.verifyLicense(
                    LicensingResponseCode.LICENSED.rawValue,
                    result.licenseText,
                    result.signature
                )
            }
        }
        
        override fun getLicenseVersion(): Int = 2
    }
    
    private data class LicenseResult(
        val licenseText: String,
        val signature: String
    )
    
    private fun generateLicenseResponse(packageName: String): LicenseResult {
        val timestamp = System.currentTimeMillis()
        val expiryTime = timestamp + (5L * 365 * 24 * 60 * 60 * 1000) // 5年有效期
        
        val licenseData = "$LICENSED$timestamp$packageName$expiryTime"
        val signature = android.util.Base64.encodeToString(
            licenseData.toByteArray(),
            android.util.Base64.DEFAULT
        )
        
        return LicenseResult(licenseData, signature)
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "LicensingService created")
    }
    
    override fun onBind(intent: Intent?): IBinder? = binder
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "LicensingService destroyed")
    }
}