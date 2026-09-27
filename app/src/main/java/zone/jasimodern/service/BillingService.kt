package zone.jasimodern.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.vending.billing.IInAppBillingService
import zone.jasimodern.data.PurchaseDatabase
import zone.jasimodern.ui.MainActivity

class BillingService : Service() {
    
    companion object {
        private const val TAG = "BillingService"
        
        // 响应码常量
        const val BILLING_RESPONSE_RESULT_OK = 0
        const val BILLING_RESPONSE_RESULT_USER_CANCELED = 1
        const val BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2
        const val BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3
        const val BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4
        const val BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5
        const val BILLING_RESPONSE_RESULT_ERROR = 6
        const val BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7
        const val BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8
    }
    
    private val binder = BillingBinder()
    
    inner class BillingBinder : IInAppBillingService.Stub() {
        
        override fun isBillingSupported(apiVersion: Int, packageName: String, billingType: String?): Int {
            Log.d(TAG, "isBillingSupported called")
            return BILLING_RESPONSE_RESULT_OK
        }
        
        override fun getBuyIntent(
            apiVersion: Int,
            packageName: String,
            itemId: String,
            itemType: String,
            extraParams: Bundle?
        ): Bundle {
            Log.d(TAG, "getBuyIntent called for $itemId")
            return createBuyIntent(itemId, extraParams)
        }
        
        override fun getBuyIntentExtraParams(
            apiVersion: Int,
            packageName: String,
            itemId: String,
            itemType: String,
            extraParams: Bundle?,
            extraIntent: Bundle?
        ): Bundle {
            Log.d(TAG, "getBuyIntentExtraParams called")
            return createBuyIntent(itemId, extraParams)
        }
        
        override fun getBuyIntentToReplaceSkus(
            apiVersion: Int,
            oldSkus: List<String>?,
            packageName: String,
            newSku: String,
            itemType: String,
            extraParams: Bundle?
        ): Bundle {
            Log.d(TAG, "getBuyIntentToReplaceSkus called")
            return createBuyIntent(newSku, extraParams)
        }
        
        override fun getPurchases(
            apiVersion: Int,
            packageName: String,
            itemType: String,
            continuationToken: String?
        ): Bundle {
            Log.d(TAG, "getPurchases called")
            return createPurchasesBundle(packageName)
        }
        
        override fun getPurchaseHistory(
            apiVersion: Int,
            packageName: String,
            itemType: String,
            continuationToken: String?
        ): Bundle {
            Log.d(TAG, "getPurchaseHistory called")
            return createPurchaseHistoryBundle(packageName)
        }
        
        override fun getSkuDetails(
            apiVersion: Int,
            packageName: String,
            itemType: String,
            skuList: List<String>?
        ): Bundle {
            Log.d(TAG, "getSkuDetails called")
            return createSkuDetailsBundle(skuList)
        }
        
        override fun consumePurchase(
            apiVersion: Int,
            packageName: String,
            purchaseToken: String
        ): Int {
            Log.d(TAG, "consumePurchase called for $purchaseToken")
            return BILLING_RESPONSE_RESULT_OK
        }
    }
    
    private fun createBuyIntent(itemId: String, extraParams: Bundle?): Bundle {
        val response = Bundle()
        response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
        
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        val pendingIntent = android.app.PendingIntent.getActivity(
            this,
            0,
            intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        
        response.putParcelable("BUY_INTENT", pendingIntent)
        return response
    }
    
    private fun createPurchasesBundle(packageName: String): Bundle {
        val response = Bundle()
        response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
        
        val database = PurchaseDatabase(this)
        val purchases = database.getPurchases(packageName)
        
        val ids = ArrayList<String>()
        val tokens = ArrayList<String>()
        val signatures = ArrayList<String>()
        
        for (purchase in purchases) {
            ids.add(purchase.itemId)
            tokens.add(purchase.purchaseToken)
            signatures.add(purchase.signature)
        }
        
        response.putStringArrayList("INAPP_PURCHASE_ITEM_LIST", ids)
        response.putStringArrayList("INAPP_PURCHASE_DATA_LIST", tokens.map { it }.toArrayList())
        response.putStringArrayList("INAPP_DATA_SIGNATURE_LIST", signatures)
        
        return response
    }
    
    private fun createPurchaseHistoryBundle(packageName: String): Bundle {
        val response = Bundle()
        response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
        
        val database = PurchaseDatabase(this)
        val history = database.getPurchaseHistory(packageName)
        
        val ids = ArrayList<String>()
        val data = ArrayList<String>()
        val signatures = ArrayList<String>()
        
        for (purchase in history) {
            ids.add(purchase.itemId)
            data.add(purchase.purchaseToken)
            signatures.add(purchase.signature)
        }
        
        response.putStringArrayList("INAPP_PURCHASE_HISTORY_LIST", ids)
        response.putStringArrayList("INAPP_PURCHASE_HISTORY_DATA_LIST", data)
        response.putStringArrayList("INAPP_PURCHASE_HISTORY_SIGNATURE_LIST", signatures)
        
        return response
    }
    
    private fun createSkuDetailsBundle(skuList: List<String>?): Bundle {
        val response = Bundle()
        response.putInt("RESPONSE_CODE", BILLING_RESPONSE_RESULT_OK)
        
        val details = ArrayList<String>()
        val items = skuList ?: emptyList()
        
        for (sku in items) {
            val bundle = Bundle()
            bundle.putString("ITEM_ID", sku)
            bundle.putString("TYPE", "inapp")
            bundle.putString("PRICE", "免费")
            bundle.putString("TITLE", "测试商品: $sku")
            bundle.putString("DESCRIPTION", "这是一个测试商品描述")
            details.add(bundleToString(bundle))
        }
        
        response.putStringArrayList("DETAILS_LIST", details)
        return response
    }
    
    private fun bundleToString(bundle: Bundle): String {
        return bundle.keySet().joinToString(", ") { key ->
            "$key=${bundle.get(key)}"
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "BillingService created")
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "BillingService bound")
        return binder
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "BillingService started")
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "BillingService destroyed")
    }
}