package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toBlockPaddedUTF8ByteArray
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toUTF8ByteArray
import codeasus.projects.encryption.crypto.RSACryptoUtil

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DBG@MainActivity"

        private const val strIOSMessage = "Salam"

        private const val base64IOSEncryptedMessage = "9szYgaPEgw/lPYStzJCUJw=="
        private const val base64IOSSecretKeyAES = "QU9ghA/W4UMbQXslGW26AkEsDR00Sdr3yKcHLJP0+Vc="
        private const val base64IOSInitializationVector = "gNRfVR+C8HzZHhA2Ian6Qw=="

        private const val data0 = "Can you deliver this message: 'əıöğöçş32423🍺🍺🥞🥞😒👌'"
        private const val data2 = "Fire"
        private const val data1 = "DreamChaserX0012"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        //performIOSSecretKeyCryptography()
    }

    private fun setup() {
        RSACryptoUtil.generateKeyPair()
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
                strDecryptedSecretKey == base64IOSSecretKeyAES
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