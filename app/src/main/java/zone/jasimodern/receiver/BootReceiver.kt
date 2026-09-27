package zone.jasimodern.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import zone.jasimodern.service.BillingService
import zone.jasimodern.service.LicensingService

class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Boot completed, starting services")
            
            // 启动计费服务
            context.startService(Intent(context, BillingService::class.java))
            
            // 启动许可证服务
            context.startService(Intent(context, LicensingService::class.java))
        }
    }
}