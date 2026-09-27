package zone.jasimodern.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Base64
import android.util.Log
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher

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
        
        override fun getLicenseVersion(): Int {
            return 2
        }
    }
    
    private data class LicenseResult(
        val licenseText: String,
        val signature: String
    )
    
    private fun generateLicenseResponse(packageName: String): LicenseResult {
        val timestamp = System.currentTimeMillis()
        val expiryTime = timestamp + (5L * 365 * 24 * 60 * 60 * 1000) // 5年有效期
        
        val licenseData = buildString {
            append(LICENSED)
            append(timestamp)
            append(packageName)
            append(expiryTime)
        }
        
        // 在实际项目中，这里应该使用真实的密钥签名
        // 当前使用模拟实现
        val signature = mockSign(licenseData)
        
        return LicenseResult(licenseData, signature)
    }
    
    private fun mockSign(data: String): String {
        // 模拟签名 - 实际项目应使用真实密钥
        return Base64.encodeToString(data.toByteArray(), Base64.DEFAULT)
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "LicensingService created")
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "LicensingService bound")
        return binder
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "LicensingService destroyed")
    }
}