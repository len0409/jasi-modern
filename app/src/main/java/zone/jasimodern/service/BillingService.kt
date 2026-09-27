package zone.jasimodern.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class BillingService : Service() {

    companion object {
        private const val TAG = "BillingService"
        private const val CHANNEL_ID = "billing_service_channel"
        const val BILLING_RESPONSE_RESULT_OK = 0
        const val BILLING_RESPONSE_RESULT_USER_CANCELED = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildNotification())
        Log.d(TAG, "BillingService created")
    }

    override fun onBind(intent: Intent?): IBinder? = billingBinder

    private val billingBinder = object : Binder() {
        override fun isBillingSupported(apiVersion: Int, packageName: String, billingType: String?): Int {
            return BILLING_RESPONSE_RESULT_OK
        }

        override fun getBuyIntent(apiVersion: Int, packageName: String, itemId: String, itemType: String, extraParams: Bundle?): Bundle {
            return createBuyIntent(itemId)
        }

        override fun getBuyIntentExtraParams(apiVersion: Int, packageName: String, itemId: String, itemType: String, extraParams: Bundle?, extraIntent: Bundle?): Bundle {
            return createBuyIntent(itemId)
        }

        override fun getBuyIntentToReplaceSkus(apiVersion: Int, oldSkus: List<String>?, packageName: String, newSku: String, itemType: String, extraParams: Bundle?): Bundle {
            return createBuyIntent(newSku)
        }

        override fun getPurchases(apiVersion: Int, packageName: String, itemType: String, continuationToken: String?): Bundle {
            return Bundle().apply { putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK) }
        }

        override fun getPurchaseHistory(apiVersion: Int, packageName: String, itemType: String, continuationToken: String?): Bundle {
            return Bundle().apply { putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK) }
        }

        override fun getSkuDetails(apiVersion: Int, packageName: String, itemType: String, skuList: List<String>?): Bundle {
            return Bundle().apply { putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK) }
        }

        override fun consumePurchase(apiVersion: Int, packageName: String, purchaseToken: String): Int {
            return BILLING_RESPONSE_RESULT_OK
        }
    }

    private fun createBuyIntent(itemId: String): Bundle {
        val response = Bundle()
        response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
        return response
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "计费服务", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Jasi Modern")
                .setContentText("计费服务运行中")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .build()
        } else {
            Notification.Builder(this)
                .setContentTitle("Jasi Modern")
                .setContentText("计费服务运行中")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "BillingService destroyed")
    }
}
