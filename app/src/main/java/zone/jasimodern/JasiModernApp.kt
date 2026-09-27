package zone.jasimodern

import android.app.Application
import android.content.Intent
import android.util.Log
import zone.jasimodern.service.BillingService
import zone.jasimodern.service.LicensingService
import zone.jasimodern.xposed.XposedModule

class JasiModernApp : Application() {
    
    companion object {
        private const val TAG = "JasiModernApp"
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        Log.d(TAG, "Jasi Modern App started")
        
        // 初始化服务
        startCoreServices()
        
        // 检查Xposed
        if (isXposedInstalled()) {
            XposedModule.initialize(this)
            Log.d(TAG, "Xposed module initialized")
        }
    }
    
    private fun startCoreServices() {
        startService(Intent(this, BillingService::class.java))
        startService(Intent(this, LicensingService::class.java))
    }
    
    private fun isXposedInstalled(): Boolean {
        return try {
            Class.forName("de.robv.android.xposed.XposedHelpers")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
    
    companion object {
        lateinit var instance: JasiModernApp
            private set
    }
}