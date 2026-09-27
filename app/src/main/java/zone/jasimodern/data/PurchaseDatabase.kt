package zone.jasimodern.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PurchaseDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    
    companion object {
        private const val DATABASE_NAME = "purchases.db"
        private const val DATABASE_VERSION = 1
        
        private const val TABLE_PURCHASES = "purchases"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PACKAGE = "packageName"
        private const val COLUMN_ITEM_ID = "itemId"
        private const val COLUMN_PURCHASE_TOKEN = "purchaseToken"
        private const val COLUMN_SIGNATURE = "signature"
        private const val COLUMN_PURCHASE_TIME = "purchaseTime"
    }
    
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_PURCHASES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PACKAGE TEXT NOT NULL,
                $COLUMN_ITEM_ID TEXT NOT NULL,
                $COLUMN_PURCHASE_TOKEN TEXT NOT NULL,
                $COLUMN_SIGNATURE TEXT NOT NULL,
                $COLUMN_PURCHASE_TIME INTEGER NOT NULL
            )
        """)
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PURCHASES")
        onCreate(db)
    }
    
    fun getPurchases(packageName: String): List<Purchase> {
        val purchases = mutableListOf<Purchase>()
        val db = readableDatabase
        
        val cursor = db.query(
            TABLE_PURCHASES,
            null,
            "$COLUMN_PACKAGE = ?",
            arrayOf(packageName),
            null, null, null
        )
        
        with(cursor) {
            while (moveToNext()) {
                purchases.add(Purchase(
                    itemId = getString(getColumnIndexOrThrow(COLUMN_ITEM_ID)),
                    purchaseToken = getString(getColumnIndexOrThrow(COLUMN_PURCHASE_TOKEN)),
                    signature = getString(getColumnIndexOrThrow(COLUMN_SIGNATURE))
                ))
            }
        }
        cursor.close()
        
        return purchases
    }
    
    fun getPurchaseHistory(packageName: String): List<Purchase> {
        // 历史购买记录（模拟）
        return getPurchases(packageName)
    }
    
    fun addPurchase(packageName: String, itemId: String, token: String, signature: String) {
        val db = writableDatabase
        db.insert(
            TABLE_PURCHASES,
            null,
            android.content.ContentValues().apply {
                put(COLUMN_PACKAGE, packageName)
                put(COLUMN_ITEM_ID, itemId)
                put(COLUMN_PURCHASE_TOKEN, token)
                put(COLUMN_SIGNATURE, signature)
                put(COLUMN_PURCHASE_TIME, System.currentTimeMillis())
            }
        )
    }
}

data class Purchase(
    val itemId: String,
    val purchaseToken: String,
    val signature: String
)
