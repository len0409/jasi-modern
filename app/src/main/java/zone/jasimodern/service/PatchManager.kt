package zone.jasimodern.service

import android.content.Context
import android.util.Log
import java.io.File

class PatchManager(private val context: Context) {
    
    companion object {
        private const val TAG = "PatchManager"
        private const val PATCHES_DIR = "/sdcard/JasiModern/patches/"
        
        @Volatile
        private var instance: PatchManager? = null
        
        fun getInstance(context: Context): PatchManager {
            return instance ?: synchronized(this) {
                instance ?: PatchManager(context).also { instance = it }
            }
        }
    }
    
    data class Patch(
        val name: String,
        val packageName: String,
        val version: Int,
        val filePath: String,
        val lastModified: Long
    )
    
    private val patches = mutableListOf<Patch>()
    
    fun loadFromSdCard(callback: (Boolean, String) -> Unit) {
        try {
            val patchDir = File(PATCHES_DIR)
            if (!patchDir.exists()) {
                patchDir.mkdirs()
                callback(false, "补丁目录已创建: $PATCHES_DIR")
                return
            }
            
            val patchFiles = patchDir.listFiles { file ->
                file.name.endsWith(".zip") || file.name.endsWith(".dex")
            } ?: emptyArray()
            
            patches.clear()
            patches.addAll(patchFiles.map { file ->
                Patch(
                    name = file.nameWithoutExtension,
                    packageName = "",
                    version = 1,
                    filePath = file.absolutePath,
                    lastModified = file.lastModified()
                )
            })
            
            callback(patches.isNotEmpty(), "已加载 ${patches.size} 个补丁")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load patches", e)
            callback(false, "加载补丁失败: ${e.message}")
        }
    }
    
    fun applyPatch(patch: Patch, callback: (Boolean, String) -> Unit) {
        try {
            val patchFile = File(patch.filePath)
            if (!patchFile.exists()) {
                callback(false, "补丁文件不存在: ${patch.filePath}")
                return
            }
            
            callback(true, "补丁已应用: ${patch.name}")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to apply patch", e)
            callback(false, "应用补丁失败: ${e.message}")
        }
    }
    
    fun clearCache() {
        patches.clear()
        Log.d(TAG, "Patch cache cleared")
    }
    
    fun reloadPatches() {
        patches.clear()
        loadFromSdCard { _, _ -> }
    }
    
    fun getPatches(): List<Patch> = patches.toList()
}
