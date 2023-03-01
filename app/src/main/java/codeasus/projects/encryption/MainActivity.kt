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

    init{
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playECDH()
    }

    private fun playECDH() {
        val plainText = "Salam enigma"
        println("Original plaintext message: $plainText")

        // Initialize two key pairs
        val keyPairA = ECDHGround.generateECKeys()
        Log.d(TAG, "public: ${Base64.encodeToString(keyPairA?.public?.encoded, Base64.NO_WRAP)}")

//        val keyPairB = ECDHGround.generateECKeys()
//
//        // Create two AES secret keys to encrypt/decrypt the message
//        val secretKeyA = ECDHGround.generateSharedSecret(keyPairA!!.private, keyPairB!!.public)
//        val secretKeyB = ECDHGround.generateSharedSecret(keyPairB.private, keyPairA.public)
//        println(keyPairB.public)
//
//        // Encrypt the message using 'secretKeyA'
//        val cipherText = ECDHGround.encryptString(secretKeyA, plainText)
//        println("Encrypted cipher text: $cipherText")
//
//        // Decrypt the message using 'secretKeyB'
//        val decryptedPlainText = ECDHGround.decryptString(secretKeyB, cipherText)
//        println("Decrypted cipher text: $decryptedPlainText")
    }
}