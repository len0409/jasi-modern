package zone.jasimodern.utils

import java.security.MessageDigest
import kotlin.random.Random

object EncryptionUtils {

    private const val TAG = "EncryptionUtils"

    fun obscureString(text: String, key: String? = null): String {
        val actualKey = key ?: generateKeyFromStack()
        return text.map { c ->
            ((c.code + actualKey.hashCode()) % 127).toChar()
        }.toString()
    }

    fun deobfuscateString(text: String, key: String? = null): String {
        val actualKey = key ?: generateKeyFromStack()
        return text.map { c ->
            (((c.code - actualKey.hashCode()) % 127 + 127) % 127).toChar()
        }.toString()
    }

    private fun generateKeyFromStack(): String {
        val stack = Throwable().stackTrace
        return if (stack.size > 2) {
            stack[2].className + stack[2].methodName
        } else {
            "default_key"
        }
    }

    fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun generateToken(length: Int = 32): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }

    fun generateFakeDeviceId(): String {
        val timestamp = System.currentTimeMillis()
        val random = Random.Default.nextLong()
        return sha256("$timestamp$random")
            .substring(0, 32)
            .lowercase()
    }
}
