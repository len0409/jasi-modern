package zone.jasimodern.xposed

import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

class XposedModule : IXposedHookLoadPackage {
    
    companion object {
        private const val TAG = "XposedModule"
        
        fun initialize(context: android.content.Context) {
            // Xposed模块自动初始化
        }
    }
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookPackageManager(lpparam.classLoader)
        hookDeviceInfo(lpparam.classLoader)
    }
    
    private fun hookPackageManager(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                "android.content.pm.PackageManager", classLoader,
                "getPackageInfo", String::class.java,
                Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val pkg = param.args[0] as? String ?: return
                        if (isSuspiciousPackage(pkg)) param.result = null
                    }
                }
            )
            
            XposedHelpers.findAndHookMethod(
                "android.content.pm.PackageManager", classLoader,
                "getApplicationInfo", String::class.java,
                Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val pkg = param.args[0] as? String ?: return
                        if (isSuspiciousPackage(pkg)) param.result = null
                    }
                }
            )
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Failed to hook PackageManager", e)
        }
    }
    
    private fun hookDeviceInfo(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                "android.telephony.TelephonyManager", classLoader,
                "getDeviceId",
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam): Any = "000000000000000"
                }
            )
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Failed to hook device info", e)
        }
    }
    
    private fun isSuspiciousPackage(packageName: String): Boolean {
        val suspicious = setOf(
            "com.noshifuou.bugsnag", "com.devadvance.rootcloak",
            "com.koushikdutta.superuser", "eu.chainfire.supersu",
            "com.topjohnwu.magisk"
        )
        return packageName in suspicious
    }
}
