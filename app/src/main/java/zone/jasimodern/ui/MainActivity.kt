package zone.jasimodern.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import zone.jasimodern.databinding.ActivityMainBinding
import zone.jasimodern.service.BillingServiceManager
import zone.jasimodern.service.PatchManager

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val billingManager by lazy { BillingServiceManager.instance }
    private val patchManager by lazy { PatchManager.instance(this) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupObservers()
        setupListeners()
    }
    
    private fun setupObservers() {
        billingManager.status.observe(this) { status ->
            binding.tvStatus.text = when (status) {
                BillingServiceManager.Status.ACTIVE -> "计费服务: 活跃"
                BillingServiceManager.Status.INACTIVE -> "计费服务: 未激活"
                else -> "计费服务: 未知"
            }
            binding.pbStatus.visibility = View.GONE
        }
    }
    
    private fun setupListeners() {
        binding.btnRefresh.setOnClickListener { refreshServices() }
        binding.btnLoadPatch.setOnClickListener { loadPatchFromStorage() }
        binding.btnClearCache.setOnClickListener { 
            patchManager.clearCache()
            Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun refreshServices() {
        binding.pbStatus.visibility = View.VISIBLE
        billingManager.refresh()
    }
    
    private fun loadPatchFromStorage() {
        patchManager.loadFromSdCard { success, message ->
            runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
        }
    }
}
