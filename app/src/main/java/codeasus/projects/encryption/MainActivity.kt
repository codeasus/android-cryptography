package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toBlockPaddedUTF8ByteArray
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toUTF8ByteArray
import codeasus.projects.encryption.crypto.IOSSpecificAESCryptoUtil
import codeasus.projects.encryption.crypto.RSACryptoUtil
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DBG@MainActivity"

        private const val strIOSMessage = "salam"

        private const val base64IOSEncryptedMessage = "9szYgaPEgw/lPYStzJCUJw=="
        private const val base64IOSSecretKeyAES = "QU9ghA/W4UMbQXslGW26AkEsDR00Sdr3yKcHLJP0+Vc="
        private const val base64IOSInitializationVector = "gNRfVR+C8HzZHhA2Ian6Qw=="

        private const val data0 = "Deliver this message: 'əıöğöçş32423🍺🍺🥞🥞😒👌'"
        private const val data1 = "DreamChaserX0012"
        private const val data2 = "Fire"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        //performIOSSecretKeyCryptography()
        //performRSAUTF8MessageCryptography()
        performCryptographyWithIOSKeyAndIV(strIOSMessage)
    }

    private fun setup() {
        RSACryptoUtil.generateKeyPair()
    }

    private fun performCryptographyWithIOSKeyAndIV(messageUTF8: String) {
        IOSSpecificAESCryptoUtil.init(base64IOSSecretKeyAES, base64IOSInitializationVector)
        Log.d(TAG, "Message to be encrypted with IOS Key: $messageUTF8")
        val byteArrayMessage = messageUTF8.toUTF8ByteArray()
        val base64BasedByteArrayMessage = Base64.encode(byteArrayMessage, Base64.NO_WRAP)
        val encryptedMessage = IOSSpecificAESCryptoUtil.encrypt(base64BasedByteArrayMessage)
        Log.d(TAG, "IOS Key encrypted message: $encryptedMessage")
        val byteArrayDecryptedMessage = Base64.decode(encryptedMessage, Base64.NO_WRAP)
        val decryptedByteArrayMessage = IOSSpecificAESCryptoUtil.decrypt(byteArrayDecryptedMessage)
        val decryptedDecodedMessage = Base64.decode(decryptedByteArrayMessage, Base64.NO_WRAP)
        val strDecryptedMessage = String(decryptedDecodedMessage, StandardCharsets.UTF_8)
        Log.d(
            TAG,
            "IOS Key decrypted message: $strDecryptedMessage, areEqual: ${
                messageUTF8 == strDecryptedMessage
            }"
        )
    }

    private fun performIOSSecretKeyCryptography() {
        Log.d(TAG, "Secret key to be encrypted: $base64IOSSecretKeyAES")
        val byteArraySecretKey = Base64.decode(base64IOSSecretKeyAES, Base64.NO_WRAP)
        val encryptedSecretKey = RSACryptoUtil.encrypt(byteArraySecretKey)
        Log.d(TAG, "Encrypted secret key: $encryptedSecretKey")
        val byteArrayEncryptedSecretKey = Base64.decode(encryptedSecretKey, Base64.NO_WRAP)
        val decryptedSecretKey = RSACryptoUtil.decrypt(byteArrayEncryptedSecretKey)
        val strDecryptedSecretKey = Base64.encodeToString(decryptedSecretKey, Base64.NO_WRAP)
        Log.d(
            TAG,
            "Decrypted secret key: $strDecryptedSecretKey, areEqual: ${
                base64IOSSecretKeyAES == strDecryptedSecretKey
            }"
        )
    }

    private fun performRSAUTF8MessageCryptography() {
        Log.d(TAG, "Message to be encrypted: $data0")
        val byteArrayMessage = data0.toUTF8ByteArray()
        val base64BasedByteArrayMessage = Base64.encode(byteArrayMessage, Base64.NO_WRAP)
        val encryptedMessage = RSACryptoUtil.encrypt(base64BasedByteArrayMessage)
        Log.d(TAG, "Encrypted message: $encryptedMessage")
        val byteArrayEncryptedMessage = Base64.decode(encryptedMessage, Base64.NO_WRAP)
        val decryptedMessage = RSACryptoUtil.decrypt(byteArrayEncryptedMessage)
        val base64StrDecryptedMessage = Base64.decode(decryptedMessage, Base64.NO_WRAP)
        val strDecryptedMessage = String(base64StrDecryptedMessage, StandardCharsets.UTF_8)
        Log.d(
            TAG, "Decrypted Message: $strDecryptedMessage, areEqual: ${
                data0 == strDecryptedMessage
            }"
        )
    }

    private fun performCustomPadding(data: String) {
        Log.d(TAG, "Data: $data")
        Log.d(TAG, "Array Data: ${data.toUTF8ByteArray().contentToString()}")
        Log.d(
            TAG,
            "Block Padded Array Data: ${
                data.toBlockPaddedUTF8ByteArray().contentToString()
            }"
        )
    }
}