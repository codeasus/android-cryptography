package codeasus.projects.encryption.crypto

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.*
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.cert.X509Certificate
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSACryptoUtil {
    private const val KEYSTORE_ALIAS_RSA = "EnigmaApp_RSAKeyPairAlias"
    private const val PROVIDER_ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding"
    private const val ALGORITHM_TYPE = "RSA"

    private const val FILE_RSA_PUBLIC_KEY = "PublicKey.pem"

    private const val STRING_ERROR_KEYPAIR = "Encryption/Decryption KeyPair has not been generated"
    private const val STRING_ERROR_DELETE_CERTIFICATE = "Public Key Certificate could not be deleted"

    private const val TAG = "DBG@RSACryptoUtil"

    fun generateKeyPair() {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            PROVIDER_ANDROID_KEY_STORE
        )
        val keyGenParameterSpec = KeyGenParameterSpec
            .Builder(
                KEYSTORE_ALIAS_RSA,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setKeySize(2048)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .setCertificateSerialNumber(BigInteger.ONE)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            .build()

        keyPairGenerator.initialize(keyGenParameterSpec)
        keyPairGenerator.generateKeyPair()
    }

    private fun getKeyPair(): KeyPair {
        val keyStore = getKeyStoreInstance()
        if (keyStore.containsAlias(KEYSTORE_ALIAS_RSA)) {
            val entry = keyStore.getEntry(KEYSTORE_ALIAS_RSA, null) as? KeyStore.PrivateKeyEntry
            return KeyPair(entry?.certificate?.publicKey, entry?.privateKey)
        }
        throw RuntimeException(STRING_ERROR_KEYPAIR)
    }

    fun generatePubKeyCertificate(context: Context) {
        val keyStore = getKeyStoreInstance()
        val fos = context.openFileOutput(FILE_RSA_PUBLIC_KEY, Context.MODE_APPEND)
        if (keyStore.containsAlias(KEYSTORE_ALIAS_RSA)) {
            val pk: PublicKey = getPublicKey()
            try {
                fos.write(pk.encoded)
            } catch (e: IOException) {
                e.message?.let { Log.d(TAG, it) }
            } finally {
                fos.close()
            }
        }
        throw RuntimeException(STRING_ERROR_KEYPAIR)
    }

    fun deletePubKeyCertificate(context: Context) {
        if (!context.deleteFile(FILE_RSA_PUBLIC_KEY)) {
            throw RuntimeException(STRING_ERROR_DELETE_CERTIFICATE)
        }
    }

    fun getPubKeyCertificateAsX509(): X509Certificate {
        val keyStore = getKeyStoreInstance()
        if (keyStore.containsAlias(KEYSTORE_ALIAS_RSA)) {
            return keyStore.getCertificate(KEYSTORE_ALIAS_RSA) as X509Certificate
        }
        throw RuntimeException(STRING_ERROR_KEYPAIR)
    }

    private fun getPublicKey(): PublicKey {
        return getKeyPair().public
    }

    private fun getPrivateKey(): PrivateKey {
        return getKeyPair().private
    }

    fun encrypt(data: ByteArray): String {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey())
        val encryptedMessage = cipher.doFinal(data)
        return Base64.encodeToString(encryptedMessage, Base64.NO_WRAP)
    }

    fun encrypt(data: ByteArray, publicKey: String): String {
        val pk = base64ToPublicKey(publicKey)
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, pk)
        val encryptedMessage = cipher.doFinal(data)
        return Base64.encodeToString(encryptedMessage, Base64.NO_WRAP)
    }

    fun decrypt(cipherData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey())
        return cipher.doFinal(cipherData)
    }

    private fun getKeyStoreInstance(): KeyStore {
        return KeyStore.getInstance(PROVIDER_ANDROID_KEY_STORE).apply {
            load(null)
        }
    }

    private fun base64ToPublicKey(base64Input: String): PublicKey {
        return KeyFactory
            .getInstance(ALGORITHM_TYPE)
            .generatePublic(X509EncodedKeySpec(Base64.decode(base64Input, Base64.NO_WRAP)))
    }

    fun keyToBase64(key: Key): String {
        return Base64.encodeToString(key.encoded, Base64.NO_WRAP)
    }
}