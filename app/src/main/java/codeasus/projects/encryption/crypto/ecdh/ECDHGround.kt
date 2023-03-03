package codeasus.projects.encryption.crypto.ecdh

import android.util.Base64
import org.bouncycastle.asn1.sec.SECNamedCurves
import org.bouncycastle.math.ec.ECCurve
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object ECDHGround {

    private var iv = SecureRandom().generateSeed(16)
    fun generateECKeys(): KeyPair? {
        val ecGenParameterSpec = ECGenParameterSpec("secp256r1")
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(ecGenParameterSpec)
        return keyPairGenerator.generateKeyPair()
    }

    fun generateSharedSecret(privateKey: PrivateKey?, publicKey: PublicKey?): SecretKey? {
        return try {
            val keyAgreement = KeyAgreement.getInstance("ECDH")
            keyAgreement.init(privateKey)
            keyAgreement.doPhase(publicKey, true)
            keyAgreement.generateSecret("AES")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun iosB64EncodedStrPKToPK(iOSB64EncodedPK: String): PublicKey {
        val decodedPK = Base64.decode(iOSB64EncodedPK, Base64.NO_WRAP)
        val x9ECParamSpec = SECNamedCurves.getByName("secp256r1")
        val curve = x9ECParamSpec.curve
        val point = curve.decodePoint(decodedPK)
        val xBcEC = point.affineXCoord.toBigInteger()
        val yBcEC = point.affineYCoord.toBigInteger()
        val gBcEC = x9ECParamSpec.g
        val xGBcEC = gBcEC.affineXCoord.toBigInteger()
        val yGBcEC = gBcEC.affineYCoord.toBigInteger()
        val hBcEC = x9ECParamSpec.h.toInt()
        val nBcEC = x9ECParamSpec.n
        val jPEC = ECPoint(xBcEC, yBcEC)
        val gJpEC = ECPoint(xGBcEC, yGBcEC)
        val jEllipticCurve = convertECCurveToEllipticCurve(curve, gJpEC, nBcEC, hBcEC)
        val eCParameterSpec = ECParameterSpec(jEllipticCurve, gJpEC, nBcEC, hBcEC)
        val ecPubLicKeySpec = ECPublicKeySpec(jPEC, eCParameterSpec)
        val keyFactorySpec = KeyFactory.getInstance("EC")
        return keyFactorySpec.generatePublic(ecPubLicKeySpec)
    }

    private fun convertECCurveToEllipticCurve(
        curve: ECCurve,
        ecPoint: ECPoint,
        n: BigInteger,
        h: Int
    ): EllipticCurve {
        val ecField = ECFieldFp(curve.field.characteristic)
        val firstCoefficient = curve.a.toBigInteger()
        val secondCoefficient = curve.b.toBigInteger()
        val ecParams = ECParameterSpec(
            EllipticCurve(ecField, firstCoefficient, secondCoefficient),
            ecPoint,
            n,
            h
        )
        return ecParams.curve
    }

    fun encryptString(key: SecretKey?, plainText: String): String? {
        return try {
            val ivSpec = IvParameterSpec(iv)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC")
            val plainTextBytes = plainText.toByteArray(charset("UTF-8"))
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
            val cipherText = ByteArray(cipher.getOutputSize(plainTextBytes.size))
            var encryptLength = cipher.update(
                plainTextBytes, 0,
                plainTextBytes.size, cipherText, 0
            )
            encryptLength += cipher.doFinal(cipherText, encryptLength)
            Base64.encodeToString(cipherText, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun decryptString(key: SecretKey?, cipherText: String?): String? {
        return try {
            val decryptionKey: Key = SecretKeySpec(
                key!!.encoded,
                key.algorithm
            )
            val ivSpec = IvParameterSpec(iv)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC")
            val cipherTextBytes = Base64.decode(cipherText, Base64.NO_WRAP)
            cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec)
            val plainText = ByteArray(cipher.getOutputSize(cipherTextBytes.size))
            var decryptLength = cipher.update(
                cipherTextBytes, 0,
                cipherTextBytes.size, plainText, 0
            )
            decryptLength += cipher.doFinal(plainText, decryptLength)
            String(plainText, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}