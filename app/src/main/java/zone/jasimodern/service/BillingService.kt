package zone.jasimodern.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class BillingService : Service() {

    companion object {
        private const val TAG = "BillingService"
        private const val CHANNEL_ID = "billing_service_channel"
        const val BILLING_RESPONSE_RESULT_OK = 0
        const val BILLING_RESPONSE_RESULT_USER_CANCELED = 1
    }

    inner class BillingBinder : Binder() {
        fun getService(): BillingService = this@BillingService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildNotification())
        Log.d(TAG, "BillingService created")
    }

    override fun onBind(intent: Intent?): IBinder? = BillingBinder()

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
