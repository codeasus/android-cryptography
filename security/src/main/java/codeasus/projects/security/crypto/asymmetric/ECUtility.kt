package codeasus.projects.security.crypto.asymmetric

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator

object ECUtility {
    private const val KEY_SIZE_EC = 2048
    private const val PROVIDER = "AndroidKeyStore"
    fun generateECKeyPair(keyAlias: String) {
        val kPG = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, PROVIDER)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val kPGS = KeyGenParameterSpec
                .Builder(keyAlias, KeyProperties.PURPOSE_AGREE_KEY)
                .setKeySize(KEY_SIZE_EC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
            kPG.initialize(kPGS)
            kPG.generateKeyPair()
        } else {

        }
    }
}