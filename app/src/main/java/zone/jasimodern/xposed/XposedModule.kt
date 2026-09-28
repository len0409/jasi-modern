package zone.jasimodern.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

class XposedModule : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: IXposedHookLoadPackage.LoadPackageParam) {
        hookPackageManager(lpparam.classLoader)
        hookDeviceInfo(lpparam.classLoader)
    }

    private fun hookPackageManager(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                "android.content.pm.PackageManager", classLoader,
                "getPackageInfo", String::class.java, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        // Hook logic here
                    }
                }
            )
        } catch (e: Exception) {
            android.util.Log.e("XposedModule", "Failed to hook PackageManager", e)
        }
    }

    private fun hookDeviceInfo(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                "android.telephony.TelephonyManager", classLoader,
                "getDeviceId",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.result = "000000000000000"
                    }
                }
            )
        } catch (e: Exception) {
            android.util.Log.e("XposedModule", "Failed to hook device info", e)
        }
    }
}
