package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import codeasus.projects.encryption.crypto.AESCryptoUtil
import codeasus.projects.encryption.crypto.AndroidKeyStoreUtil
import codeasus.projects.encryption.crypto.RSACryptoUtil
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.cert.X509Certificate

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DBG@MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RSACryptoUtil.generateKeyPair()


        val data = "This is some secret bs"
        Log.d(TAG, "Data: $data")
//        val secretKey = AESCryptoUtil.generateSecretKey()
//        AndroidKeyStoreUtil.generateSecretKey()
//        val ivx = byteArrayOf(12, 34, 45, 23, 124, 34, 90, 99, 67, 44, 11, 23);
//        val iv = AESCryptoUtil.generateInitializationVector()
//        val encryptedData = AndroidKeyStoreUtil.encrypt(data, ivx);
//        val encryptedData = "FbYO4etTxdZYqYqkJXVDMHW/YrEd6xbIghiC+Xj9+/9d0KVIQOnsJbtUYZY5rYcf+ClA5yZvNoigwBsk1XbcJO3+"
//        Log.d(TAG, "Encrypted  Data: $encryptedData")
//        val decryptedData = String(AndroidKeyStoreUtil.decrypt(Base64.decode(encryptedData, Base64.DEFAULT), ivx), StandardCharsets.UTF_8)
//        Log.d(TAG, "Decrypted Data: $decryptedData")
//
//        val secretKeyEncoded = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT);
//        Log.d(TAG, "Secret Key: $secretKeyEncoded")
//        val encryptedData = Base64.encodeToString(AESCryptoUtil.encrypt(data, secretKey, iv), Base64.DEFAULT);
//        Log.d(TAG, "Encrypted  Data: $encryptedData")
//        RSACryptoUtil.generateKeyPair()
//        val encryptedSecretKey = RSACryptoUtil.encryptBase64EncodedData(secretKeyEncoded)
//        Log.d(TAG, "Encrypted Secret Key: $encryptedSecretKey")
//        val decryptedSecretKey = AESCryptoUtil.secretKeyFromByteArray(RSACryptoUtil.decrypt(Base64.decode(encryptedSecretKey, Base64.DEFAULT)))
//        val decryptedSecretKeyEncoded = Base64.encodeToString(decryptedSecretKey.encoded, Base64.DEFAULT)
//        Log.d(TAG, "Decrypted Secret Key: $decryptedSecretKeyEncoded")
//        val decryptedData = AESCryptoUtil.decrypt(Base64.decode(encryptedData, Base64.DEFAULT), secretKey, iv);
//        Log.d(TAG, "Decrypted Data: $decryptedData")
    }
}