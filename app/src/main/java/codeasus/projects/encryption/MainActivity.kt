package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toBlockPaddedUTF8ByteArray
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toUTF8ByteArray
import codeasus.projects.encryption.crypto.RSACryptoUtil

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DBG@MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val base64IOSMessage = "Salam"
        val base64IOSEncryptedMessage = "9szYgaPEgw/lPYStzJCUJw=="
        val base64IOSSecretKeyAES = "QU9ghA/W4UMbQXslGW26AkEsDR00Sdr3yKcHLJP0+Vc="
        val base64IOSInitializationVector = "gNRfVR+C8HzZHhA2Ian6Qw=="

        RSACryptoUtil.generateKeyPair()

        val d0 = "Hello, can you deliver this number to my friend: 32423"
        val d2 = "Fire"
        val d1 = "DreamChaserX0012"
        Log.d(TAG, "Data: $d1")
        Log.d(TAG, "Array Data: ${d1.toUTF8ByteArray().contentToString()}")
        Log.d(TAG, "Block Padded Array Data: ${d1.toBlockPaddedUTF8ByteArray().contentToString()}")

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