package zone.jasimodern

import android.app.Application
import zone.jasimodern.service.BillingServiceManager
import zone.jasimodern.service.LicensingServiceManager
import zone.jasimodern.xposed.XposedModule

class JasiModernApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // 初始化服务管理器
        BillingServiceManager.initialize(this)
        LicensingServiceManager.initialize(this)
        
        // 检查是否需要加载Xposed模块
        if (isXposedInstalled()) {
            XposedModule.initialize(this)
        }
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