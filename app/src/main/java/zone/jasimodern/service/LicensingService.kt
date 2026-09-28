package zone.jasimodern.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class LicensingService : Service() {

    companion object {
        private const val TAG = "LicensingService"
        private const val LICENSED = "LICENSED"
    }

    inner class LicensingBinder : Binder() {
        fun getService(): LicensingService = this@LicensingService
    }

    override fun onBind(intent: Intent?): IBinder? = LicensingBinder()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "LicensingService created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "LicensingService destroyed")
    }
}
