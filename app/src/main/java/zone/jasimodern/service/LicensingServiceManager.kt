package zone.jasimodern.service

import android.content.Context
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LicensingServiceManager(private val context: Context) {
    
    enum class Status { ACTIVE, INACTIVE, UNKNOWN }
    
    companion object {
        private const val TAG = "LicensingServiceManager"
    }
    
    private val _status = MutableStateFlow(Status.UNKNOWN)
    val status: StateFlow<Status> = _status
    
    private var licensingConnection: ServiceConnection? = null
    
    fun initialize() {
        bindToLicensingService()
    }
    
    private fun bindToLicensingService() {
        val intent = android.content.Intent("com.android.vending.licensing.ILicensingService")
        intent.setPackage(context.packageName)
        
        licensingConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                _status.value = Status.ACTIVE
                Log.d(TAG, "Licensing service connected")
            }
            
            override fun onServiceDisconnected(name: ComponentName?) {
                _status.value = Status.INACTIVE
                Log.d(TAG, "Licensing service disconnected")
            }
        }
        
        context.bindService(intent, licensingConnection!!, Context.BIND_AUTO_CREATE)
    }
    
    fun unbind() {
        licensingConnection?.let { conn ->
            try {
                context.unbindService(conn)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to unbind service", e)
            }
        }
        licensingConnection = null
    }
}