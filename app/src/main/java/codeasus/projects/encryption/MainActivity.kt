package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import codeasus.projects.encryption.crypto.ecdh.ECDHGround

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DBG@MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playECDH()
    }

    private fun playECDH() {
        val plainText = "This is a random text"
        val pkIOS = "BAlWWu46il/ly6Axd/qclmhEVhGth93QN5+h3JBJEKEmhKd1LfqkpCqX1cT1cQDs9nPq9Lq0/FtZitkjr7Rqd94="
//        Log.e(TAG, "NEW_PUBLIC_KEY: ${Base64.encodeToString(ECDHGround.generateKeyPair()?.encoded, Base64.NO_WRAP)}")

        Log.e(TAG, "PUBLIC_KEY_IOS: ${Base64.encodeToString(ECDHGround.iosB64EncodedStrPKToPK(pkIOS).encoded, Base64.NO_WRAP)}")
        Log.d(TAG, "--------------------------------------------------------------")
        // Initialize two key pairs
        val keyPairA = ECDHGround.generateECKeys()
        val keyPairB = ECDHGround.generateECKeys()

        Log.d(TAG, "PUBLIC_A: ${Base64.encodeToString(keyPairA?.public?.encoded, Base64.NO_WRAP)}")
        Log.d(TAG, "PRIVATE_A: ${Base64.encodeToString(keyPairA?.private?.encoded, Base64.NO_WRAP)}")
        Log.d(TAG, "--------------------------------------------------------------")
        Log.d(TAG, "PUBLIC_B: ${Base64.encodeToString(keyPairB?.public?.encoded, Base64.NO_WRAP)}")
        Log.d(TAG, "PRIVATE_B: ${Base64.encodeToString(keyPairB?.private?.encoded, Base64.NO_WRAP)}")

        // Create two AES secret keys to encrypt/decrypt the message
        val secretKeyA = ECDHGround.generateSharedSecret(keyPairA!!.private, keyPairB!!.public)
        val secretKeyB = ECDHGround.generateSharedSecret(keyPairB.private, keyPairA.public)

        Log.d(TAG, "SECRET_KEY_A: ${Base64.encodeToString(secretKeyA?.encoded, Base64.NO_WRAP)}")
        Log.d(TAG, "SECRET_KEY_B: ${Base64.encodeToString(secretKeyB?.encoded, Base64.NO_WRAP)}")

//        // Encrypt the message using 'secretKeyA'
//        val cipherText = ECDHGround.encryptString(secretKeyA, plainText)
//        println("Encrypted cipher text: $cipherText")
//
//        // Decrypt the message using 'secretKeyB'
//        val decryptedPlainText = ECDHGround.decryptString(secretKeyB, cipherText)
//        println("Decrypted cipher text: $decryptedPlainText")
    }
}