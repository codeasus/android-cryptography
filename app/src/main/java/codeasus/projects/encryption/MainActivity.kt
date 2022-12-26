package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toBlockPaddedUTF8ByteArray
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toUTF8ByteArray
import codeasus.projects.encryption.crypto.RSACryptoUtil

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DBG@MainActivity"
        private const val data = "Message: 'əıöğw@#><\",352:?%!)(*?öçş32423🍺🍺🥞🥞😒👌'"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        RSACryptoUtil.getPublicKey()
    }

    private fun setup() {
        RSACryptoUtil.generateKeyPair()
    }
}