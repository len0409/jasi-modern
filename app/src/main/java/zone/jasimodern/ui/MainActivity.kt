package zone.jasimodern.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import zone.jasimodern.R
import zone.jasimodern.databinding.ActivityMainBinding
import zone.jasimodern.service.BillingService
import zone.jasimodern.service.LicensingService
import zone.jasimodern.service.PatchManager
import zone.jasimodern.viewmodel.SettingsViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SettingsViewModel
    private val patchManager by lazy { PatchManager(this) }
    
    private var billingConnection: ServiceConnection? = null
    private var licensingConnection: ServiceConnection? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        
        setupViews()
        startServices()
        setupObservers()
    }
    
    private fun setupViews() {
        binding.btnRefresh.setOnClickListener { refreshServices() }
        binding.btnLoadPatch.setOnClickListener { loadPatchFromStorage() }
        binding.btnClearCache.setOnClickListener { clearCache() }
        binding.rvPatches.layoutManager = LinearLayoutManager(this)
    }
    
    private fun startServices() {
        val billingIntent = Intent(this, BillingService::class.java)
        val licensingIntent = Intent(this, LicensingService::class.java)
        
        billingConnection = createBillingConnection()
        licensingConnection = createLicensingConnection()
        
        bindService(billingIntent, billingConnection!!, Context.BIND_AUTO_CREATE)
        bindService(licensingIntent, licensingConnection!!, Context.BIND_AUTO_CREATE)
        
        startService(billingIntent)
        startService(licensingIntent)
    }
    
    private fun createBillingConnection(): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                binding.tvStatus.text = "计费服务: 已连接"
            }
            
            override fun onServiceDisconnected(name: ComponentName?) {
                binding.tvStatus.text = "计费服务: 已断开"
            }
        }
    }
    
    private fun createLicensingConnection(): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                binding.tvStatus.text = "许可证服务: 已连接"
            }
            
            override fun onServiceDisconnected(name: ComponentName?) {
                binding.tvStatus.text = "许可证服务: 已断开"
            }
        }
    }
    
    private fun setupObservers() {
        viewModel.autoStartEnabled.observe(this) { enabled ->
            // 更新UI
        }
    }
    
    private fun refreshServices() {
        binding.pbStatus.visibility = View.VISIBLE
        patchManager.reloadPatches()
    }
    
    private fun loadPatchFromStorage() {
        binding.pbStatus.visibility = View.VISIBLE
        viewModelScope.launch {
            patchManager.loadFromSdCard { success, message ->
                runOnUiThread {
                    binding.pbStatus.visibility = View.GONE
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun clearCache() {
        patchManager.clearCache()
        Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        billingConnection?.let { unbindService(it) }
        licensingConnection?.let { unbindService(it) }
    }
}
