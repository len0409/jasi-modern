package zone.jasimodern.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import zone.jasimodern.databinding.FragmentSettingsBinding
import zone.jasimodern.service.BillingServiceManager
import zone.jasimodern.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: SettingsViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        
        setupSwitches()
        observeViewModel()
    }
    
    private fun setupSwitches() {
        binding.switchAutoStart.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateAutoStart(isChecked)
            Toast.makeText(context, "自动启动: ${if (isChecked) "已开启" else "已关闭"}", 
                Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun observeViewModel() {
        viewModel.autoStartEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.switchAutoStart.isChecked = enabled
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.updateBillingStatus(BillingServiceManager.Status.ACTIVE)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}