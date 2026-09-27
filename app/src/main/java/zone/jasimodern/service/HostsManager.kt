package zone.jasimodern.service

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileWriter

class HostsManager(private val context: Context) {
    
    companion object {
        private const val TAG = "HostsManager"
        private const val HOSTS_FILE = "hosts"
        private const val BACKUP_DIR = "/sdcard/JasiModern/hosts_backup/"
    }
    
    private var hostsContent = ""
    private val hostsRules = mutableListOf<HostsRule>()
    
    data class HostsRule(
        val ip: String,
        val domain: String,
        val category: String
    )
    
    fun loadFromAssets() {
        try {
            val inputStream = context.assets.open(HOSTS_FILE)
            hostsContent = inputStream.bufferedReader().use { it.readText() }
            
            parseHostsContent()
            Log.d(TAG, "Loaded hosts file with ${hostsRules.size} rules")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load hosts file", e)
        }
    }
    
    fun parseHostsContent() {
        hostsRules.clear()
        
        val lines = hostsContent.split("\n")
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue
            
            val parts = trimmed.split("\\s+".toRegex())
            if (parts.size >= 2) {
                val ip = parts[0]
                val domain = parts[1]
                
                val category = when {
                    domain.contains("google") || domain.contains("ads") -> "广告"
                    domain.contains("facebook") -> "社交媒体"
                    domain.contains("twitter") -> "社交媒体"
                    domain.contains("tracking") || domain.contains("analytics") -> "追踪器"
                    else -> "其他"
                }
                
                hostsRules.add(HostsRule(ip, domain, category))
            }
        }
    }
    
    fun backupHosts() {
        try {
            val backupDir = File(BACKUP_DIR)
            if (!backupDir.exists()) {
                backupDir.mkdirs()
            }
            
            val timestamp = System.currentTimeMillis()
            val backupFile = File(backupDir, "hosts_backup_$timestamp.txt")
            
            val writer = FileWriter(backupFile)
            writer.write(hostsContent)
            writer.close()
            
            Log.d(TAG, "Hosts file backed up to ${backupFile.absolutePath}")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to backup hosts", e)
        }
    }
    
    fun getRulesByCategory(category: String): List<HostsRule> {
        return hostsRules.filter { it.category == category }
    }
    
    fun getTotalRules(): Int = hostsRules.size
    
    fun getRuleCountByCategory(): Map<String, Int> {
        return hostsRules.groupBy { it.category }
            .mapValues { it.value.size }
    }
}
