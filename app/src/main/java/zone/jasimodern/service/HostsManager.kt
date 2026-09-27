package zone.jasimodern.service

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class HostsManager(private val context: Context) {
    
    companion object {
        private const val TAG = "HostsManager"
        private const val HOSTS_FILE = "/sdcard/JasiModern/hosts"
        
        @Volatile
        private var instance: HostsManager? = null
        
        fun getInstance(context: Context): HostsManager {
            return instance ?: synchronized(this) {
                instance ?: HostsManager(context).also { instance = it }
            }
        }
    }
    
    private var hostsRules: MutableList<String> = mutableListOf()
    
    suspend fun loadHostsFile(): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(HOSTS_FILE)
                if (!file.exists()) {
                    Result.failure(Exception("Hosts文件不存在: $HOSTS_FILE"))
                } else {
                    hostsRules = file.readLines()
                        .filter { it.isNotEmpty() && !it.startsWith("#") }
                        .toMutableList()
                    Result.success(hostsRules.size)
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载hosts失败", e)
                Result.failure(e)
            }
        }
    }
    
    fun getRules(): List<String> = hostsRules.toList()
    
    fun addRule(rule: String): Boolean {
        if (rule.isNotEmpty() && !rule.startsWith("#") && !hostsRules.contains(rule)) {
            hostsRules.add(rule)
            return true
        }
        return false
    }
    
    fun removeRule(rule: String): Boolean {
        return hostsRules.remove(rule)
    }
    
    fun clearRules() {
        hostsRules.clear()
    }
    
    suspend fun saveToFile(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(HOSTS_FILE)
                file.parentFile?.mkdirs()
                file.writeText(hostsRules.joinToString("\n") { "$it\n" })
                Result.success(true)
            } catch (e: Exception) {
                Log.e(TAG, "保存hosts失败", e)
                Result.failure(e)
            }
        }
    }
}
