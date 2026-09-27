# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep Xposed API
-keep class de.robv.android.xposed.** { *; }
-keep interface de.robv.android.xposed.** { *; }

# Keep Billing Service
-keep class com.android.vending.billing.** { *; }
-keep interface com.android.vending.billing.** { *; }

# Keep Licensing Service
-keep class com.android.vending.licensing.** { *; }
-keep interface com.android.vending.licensing.** { *; }

# Keep main classes
-keep class zone.jasimodern.** { *; }
