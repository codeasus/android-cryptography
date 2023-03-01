package codeasus.projects.encryption.crypto.rsa

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.security.*
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSACryptographyUtility {
    private const val KEYSTORE_ALIAS_RSA = "EnigmaApp_RSAKeyPairAlias"
    private const val PROVIDER_ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding"
    private const val ALGORITHM_TYPE = "RSA"

    private const val FILE_RSA_PUBLIC_KEY = "PublicKey.pem"

    private const val STRING_ERROR_KEYPAIR = "Encryption/Decryption KeyPair has not been generated"
    private const val STRING_ERROR_DELETE_CERTIFICATE = "Public Key Certificate could not be deleted"

    private const val TAG = "DBG@RSACryptoUtil"

    // b64, B64 -> base64, Base64

    fun generateKeyPair() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            val kPG = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, PROVIDER_ANDROID_KEY_STORE)
            val kGPS = KeyGenParameterSpec
                .Builder(KEYSTORE_ALIAS_RSA, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setKeySize(2048)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build()

            kPG.initialize(kGPS)
            kPG.generateKeyPair()
        }
    }

    fun generatePubKeyCertificate(context: Context) {
        val ks = getKeyStore()
        if (ks.containsAlias(KEYSTORE_ALIAS_RSA)) {
            val pk: PublicKey = getPublicKey()
            var fOS: FileOutputStream? = null
            try {
                fOS = context.openFileOutput(FILE_RSA_PUBLIC_KEY, Context.MODE_APPEND)
                fOS.write(pk.encoded)
            } catch (e: IOException) {
                e.message?.let { Log.d(TAG, it) }
            } finally {
                fOS?.close()
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
        val kS = getKeyStore()
        if (kS.containsAlias(KEYSTORE_ALIAS_RSA)) {
            return kS.getCertificate(KEYSTORE_ALIAS_RSA) as X509Certificate
        }
        throw RuntimeException(STRING_ERROR_KEYPAIR)
    }

    fun getPublicKey(): PublicKey {
        return getKeyPair().public.also {
            Log.d(TAG, " -> PK: ${pKToB64EncodedStr(it)};")
        }
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

    fun decrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_RSA_ECB_PKCS1_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey())
        return cipher.doFinal(data)
    }

    private fun getKeyPair(): KeyPair {
        val kS = getKeyStore()
        if (kS.containsAlias(KEYSTORE_ALIAS_RSA)) {
            val entry = kS.getEntry(KEYSTORE_ALIAS_RSA, null) as? KeyStore.PrivateKeyEntry
            return KeyPair(entry?.certificate?.publicKey, entry?.privateKey)
        }
        throw RuntimeException(STRING_ERROR_KEYPAIR)
    }

    private fun getPrivateKey(): PrivateKey {
        return getKeyPair().private
    }

    private fun getKeyStore(): KeyStore {
        return KeyStore.getInstance(PROVIDER_ANDROID_KEY_STORE).apply {
            load(null)
        }
    }

    fun b64EncodedStrToPKForIOS(b64EncodedPK: String): PublicKey {
        val decodedByteArrayPK = Base64.decode(b64EncodedPK, Base64.NO_WRAP)
        val pkcs1PublicKey = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(decodedByteArrayPK)
        val modulus = pkcs1PublicKey.modulus
        val publicExponent = pkcs1PublicKey.publicExponent
        val ks = RSAPublicKeySpec(modulus, publicExponent)
        return KeyFactory.getInstance(ALGORITHM_TYPE).generatePublic(ks) as RSAPublicKey
    }

    fun b64EncodedStrToPKForANDROID(b64EncodedPK: String): PublicKey {
        return KeyFactory
            .getInstance(ALGORITHM_TYPE)
            .generatePublic(X509EncodedKeySpec(Base64.decode(b64EncodedPK, Base64.NO_WRAP)))
    }

    fun pKToB64EncodedStr(pK: PublicKey): String {
        return Base64.encodeToString(pK.encoded, Base64.NO_WRAP)
    }
}