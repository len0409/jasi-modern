package zone.jasimodern.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import zone.jasimodern.R

class BillingService : Service() {
    
    companion object {
        private const val TAG = "BillingService"
        private const val CHANNEL_ID = "billing_service_channel"
        
        // 响应码常量
        const val BILLING_RESPONSE_RESULT_OK = 0
        const val BILLING_RESPONSE_RESULT_USER_CANCELED = 1
        const val BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2
    }
    
    private val binder = BillingBinder()
    
    inner class BillingBinder : IInAppBillingService.Stub() {
        
        override fun isBillingSupported(apiVersion: Int, packageName: String, billingType: String?): Int {
            Log.d(TAG, "isBillingSupported called")
            return BILLING_RESPONSE_RESULT_OK
        }
        
        override fun getBuyIntent(
            apiVersion: Int,
            packageName: String,
            itemId: String,
            itemType: String,
            extraParams: android.os.Bundle?
        ): android.os.Bundle {
            Log.d(TAG, "getBuyIntent called for $itemId")
            return createBuyIntent(itemId, extraParams)
        }
        
        override fun consumePurchase(
            apiVersion: Int,
            packageName: String,
            purchaseToken: String
        ): Int {
            Log.d(TAG, "consumePurchase called")
            return BILLING_RESPONSE_RESULT_OK
        }
        
        // 其他方法实现...
        override fun getBuyIntentExtraParams(
            apiVersion: Int,
            packageName: String,
            itemId: String,
            itemType: String,
            extraParams: android.os.Bundle?,
            extraIntent: android.os.Bundle?
        ): android.os.Bundle = createBuyIntent(itemId, extraParams)
        
        override fun getBuyIntentToReplaceSkus(
            apiVersion: Int,
            oldSkus: List<String>?,
            packageName: String,
            newSku: String,
            itemType: String,
            extraParams: android.os.Bundle?
        ): android.os.Bundle = createBuyIntent(newSku, extraParams)
        
        override fun getPurchases(
            apiVersion: Int,
            packageName: String,
            itemType: String,
            continuationToken: String?
        ): android.os.Bundle {
            Log.d(TAG, "getPurchases called")
            val response = android.os.Bundle()
            response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
            return response
        }
        
        override fun getPurchaseHistory(
            apiVersion: Int,
            packageName: String,
            itemType: String,
            continuationToken: String?
        ): android.os.Bundle {
            Log.d(TAG, "getPurchaseHistory called")
            val response = android.os.Bundle()
            response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
            return response
        }
        
        override fun getSkuDetails(
            apiVersion: Int,
            packageName: String,
            itemType: String,
            skuList: List<String>?
        ): android.os.Bundle {
            Log.d(TAG, "getSkuDetails called")
            val response = android.os.Bundle()
            response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
            return response
        }
    }
    
    private fun createBuyIntent(itemId: String, extraParams: android.os.Bundle?): android.os.Bundle {
        val response = android.os.Bundle()
        response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
        
        val intent = Intent(this, zone.jasimodern.ui.MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        val pendingIntent = android.app.PendingIntent.getActivity(
            this,
            0,
            intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        
        response.putParcelable("BUY_INTENT", pendingIntent)
        return response
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildNotification())
        Log.d(TAG, "BillingService created")
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "计费服务",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
    
    private fun buildNotification(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Jasi Modern")
                .setContentText("计费服务运行中")
                .setSmallIcon(R.drawable.ic_notification)
                .build()
        } else {
            android.app.Notification.Builder(this)
                .setContentTitle("Jasi Modern")
                .setContentText("计费服务运行中")
                .setSmallIcon(R.drawable.ic_notification)
                .build()
        }
    }
    
    override fun onBind(intent: Intent?): IBinder? = binder
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "BillingService destroyed")
    }
}