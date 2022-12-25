package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import codeasus.projects.encryption.crypto.AESCryptoUtil
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toBlockPaddedUTF8ByteArray
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toUTF8ByteArray
import codeasus.projects.encryption.crypto.IOSSpecificAESCryptoUtil
import codeasus.projects.encryption.crypto.RSACryptoUtil
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DBG@MainActivity"

        private const val data = "Message: 'əıöğw@#><\",352:?%!)(*?öçş32423🍺🍺🥞🥞😒👌'"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()

    }

    private fun setup() {
        RSACryptoUtil.generateKeyPair()
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