package zone.jasimodern.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import zone.jasimodern.service.BillingServiceManager

class SettingsViewModel : ViewModel() {
    
    private val _billingStatus = MutableLiveData<BillingServiceManager.Status>()
    val billingStatus: LiveData<BillingServiceManager.Status> = _billingStatus
    
    private val _isXposedEnabled = MutableLiveData<Boolean>()
    val isXposedEnabled: LiveData<Boolean> = _isXposedEnabled
    
    private val _autoStartEnabled = MutableLiveData<Boolean>()
    val autoStartEnabled: LiveData<Boolean> = _autoStartEnabled
    
    fun updateBillingStatus(status: BillingServiceManager.Status) {
        _billingStatus.value = status
    }
    
    fun updateXposedStatus(enabled: Boolean) {
        _isXposedEnabled.value = enabled
    }
    
    fun updateAutoStart(enabled: Boolean) {
        _autoStartEnabled.value = enabled
    }
    
    fun toggleAutoStart(enabled: Boolean) {
        _autoStartEnabled.postValue(enabled)
        // 实际应用中这里会保存设置
    }
}
