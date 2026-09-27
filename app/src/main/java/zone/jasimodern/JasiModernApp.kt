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
        lateinit var instance: JasiModernApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        Log.d(TAG, "Jasi Modern App started")

        startCoreServices()
    }

    private fun startCoreServices() {
        startService(Intent(this, BillingService::class.java))
        startService(Intent(this, LicensingService::class.java))
    }
}
