package money.transfer.transfermoney.encryption

import android.util.Base64
import android.util.Log
import money.transfer.transfermoney.BuildConfig
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


class DataEncryption {

    fun encryptData(data: String, publicKey: String): String {
        val keySpec = X509EncodedKeySpec(Base64.decode(publicKey.trim { it <= ' ' }.toByteArray(), Base64.DEFAULT))
        val key = KeyFactory.getInstance("RSA").generatePublic(keySpec)

        val cipher = Cipher.getInstance(BuildConfig.cipherStandard)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(data.toByteArray(charset("UTF-8")))
        val base64Data = String(Base64.encode(encryptedBytes, Base64.DEFAULT))
        return base64Data.replace("(\\r|\\n)".toRegex(), "")
    }

    fun decryptData(encryptedBase64: String, privateKey: String): String {
        val keySpec = PKCS8EncodedKeySpec(Base64.decode(privateKey.trim { it <= ' ' }.toByteArray(), Base64.DEFAULT))
        val key = KeyFactory.getInstance("RSA").generatePrivate(keySpec)

        val cipher = Cipher.getInstance(BuildConfig.cipherStandard)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        val result = String(decryptedBytes)
        return result
    }

}