package zone.jasimodern.service

import android.content.Context
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BillingServiceManager(private val context: Context) {
    
    enum class Status { ACTIVE, INACTIVE, UNKNOWN }
    
    companion object {
        private const val TAG = "BillingServiceManager"
    }
    
    private val _status = MutableStateFlow(Status.UNKNOWN)
    val status: StateFlow<Status> = _status
    
    private var billingConnection: ServiceConnection? = null
    
    fun initialize() {
        bindToBillingService()
    }
    
    private fun bindToBillingService() {
        val intent = android.content.Intent("com.android.vending.billing.IInAppBillingService.BIND")
        intent.setPackage(context.packageName)
        
        billingConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                _status.value = Status.ACTIVE
                Log.d(TAG, "Billing service connected")
            }
            
            override fun onServiceDisconnected(name: ComponentName?) {
                _status.value = Status.INACTIVE
                Log.d(TAG, "Billing service disconnected")
            }
        }
        
        context.bindService(intent, billingConnection!!, Context.BIND_AUTO_CREATE)
    }
    
    fun refresh() {
        billingConnection?.let { conn ->
            context.unbindService(conn)
        }
        billingConnection = null
        bindToBillingService()
    }
    
    fun unbind() {
        billingConnection?.let { conn ->
            try {
                context.unbindService(conn)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to unbind service", e)
            }
        }
        billingConnection = null
    }
}