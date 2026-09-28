package zone.jasimodern.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log

class XposedModule : IXposedHookLoadPackage {

    companion object {
        private const val TAG = "XposedModule"
    }

    override fun handleLoadPackage(param: Any) {
        val classLoader = (param as? Any)?.javaClass?.classLoader
        hookPackageManager(classLoader)
    }

    private fun hookPackageManager(classLoader: ClassLoader?) {
        if (classLoader == null) return
        try {
            XposedHelpers.findAndHookMethod(
                PackageManager::class.java.name, classLoader,
                "getPackageInfo", String::class.java, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val pkgName = param.args[0] as? String ?: return
                        if (isSuspiciousPackage(pkgName)) {
                            // Could modify result here if needed
                        }
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to hook PackageManager", e)
        }
    }

    private fun isSuspiciousPackage(packageName: String): Boolean {
        val suspicious = setOf(
            "com.noshifuou.bugsnag",
            "com.devadvance.rootcloak",
            "com.koushikdutta.superuser",
            "eu.chainfire.supersu",
            "com.topjohnwu.magisk"
        )
        return packageName in suspicious
    }
}
