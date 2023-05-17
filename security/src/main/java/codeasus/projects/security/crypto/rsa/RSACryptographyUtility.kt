package codeasus.projects.security.crypto.rsa

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.security.*
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSACryptographyUtility {
    private const val KEYSTORE_ALIAS_RSA = "EnigmaApp_RSACryptoAlias"
    private const val PROVIDER = "AndroidKeyStore"
    private const val ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding"
    private const val ALGORITHM_TYPE = "RSA"

    private const val FILE_RSA_PUBLIC_KEY = "PublicKey.pem"

    private const val STRING_ERROR_KEYPAIR = "Encryption/Decryption KeyPair has not been generated"
    private const val STRING_ERROR_DELETE_CERTIFICATE = "Public Key Certificate could not be deleted"

    private val TAG = "DBG@CRYPTO@${RSACryptographyUtility::class.java.name}"

    // b64, B64 -> base64, Base64
    // data -> ByteArray

    fun generateKeyPair() {
        val kPG = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, PROVIDER)
        val kGPS = KeyGenParameterSpec
            .Builder(
                KEYSTORE_ALIAS_RSA,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setKeySize(2048)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .build()

        kPG.initialize(kGPS)
        kPG.generateKeyPair()
    }

    private fun getKeyStore(): KeyStore {
        return KeyStore.getInstance(PROVIDER).apply {
            load(null)
        }
    }

    fun isKeyGenerated(): Boolean {
        return getKeyStore().containsAlias(KEYSTORE_ALIAS_RSA)
    }

    @Throws(RuntimeException::class)
    fun generatePubKeyCertificate(ctx: Context) {
        val ks = getKeyStore()
        if (ks.containsAlias(KEYSTORE_ALIAS_RSA)) {
            val pk: PublicKey = getPublicKey()
            var fOS: FileOutputStream? = null
            try {
                fOS = ctx.openFileOutput(FILE_RSA_PUBLIC_KEY, Context.MODE_APPEND)
                fOS.write(pk.encoded)
            } catch (e: IOException) {
                e.message?.let { Log.e(TAG, it) }
            } finally {
                fOS?.close()
            }
        }
        throw RuntimeException(STRING_ERROR_KEYPAIR)
    }

    @Throws(RuntimeException::class, FileNotFoundException::class)
    fun deletePubKeyCertificate(ctx: Context) {
        if (!ctx.deleteFile(FILE_RSA_PUBLIC_KEY)) {
            throw RuntimeException(STRING_ERROR_DELETE_CERTIFICATE)
        }
    }

    @Throws(RuntimeException::class)
    fun getPubKeyCertificateAsX509(): X509Certificate {
        getKeyStore().run {
            if (this.containsAlias(KEYSTORE_ALIAS_RSA)) {
                return this.getCertificate(KEYSTORE_ALIAS_RSA) as X509Certificate
            }
            throw RuntimeException(STRING_ERROR_KEYPAIR)
        }
    }

    private fun getPublicKey(): PublicKey {
        return getKeyPair().public
    }

    private fun getPrivateKey(): PrivateKey {
        return getKeyPair().private
    }

    fun encrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey())
        return cipher.doFinal(data)
    }

    fun encrypt(data: ByteArray, pk: PublicKey): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, pk)
        return cipher.doFinal(data)
    }

    fun decrypt(cipherData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING)
        val priKey = getPrivateKey()
        cipher.init(Cipher.DECRYPT_MODE, priKey)
        return cipher.doFinal(cipherData)
    }

    private fun getKeyPair(): KeyPair {
        getKeyStore().run {
            if (this.containsAlias(KEYSTORE_ALIAS_RSA)) {
                val entry = this.getEntry(KEYSTORE_ALIAS_RSA, null) as? KeyStore.PrivateKeyEntry
                return KeyPair(entry?.certificate?.publicKey, entry?.privateKey)
            }
            throw RuntimeException(STRING_ERROR_KEYPAIR)
        }
    }

    fun iOSB64DataPubKeyToPubKey(dataPubKey: ByteArray): PublicKey {
        val pkcs1PublicKey = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(dataPubKey)
        val modulus = pkcs1PublicKey.modulus
        val publicExponent = pkcs1PublicKey.publicExponent
        val ks = RSAPublicKeySpec(modulus, publicExponent)
        return KeyFactory.getInstance(ALGORITHM_TYPE).generatePublic(ks) as RSAPublicKey
    }

    fun dataPubKeyToPubKey(dataPubKey: ByteArray): PublicKey {
        return KeyFactory
            .getInstance(ALGORITHM_TYPE)
            .generatePublic(X509EncodedKeySpec(dataPubKey))
    }
}