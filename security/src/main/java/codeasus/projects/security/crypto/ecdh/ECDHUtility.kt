package codeasus.projects.security.crypto.ecdh

import android.util.Base64
import org.bouncycastle.asn1.sec.SECNamedCurves
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.generators.HKDFBytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import org.bouncycastle.crypto.params.HKDFParameters
import org.bouncycastle.math.ec.ECCurve
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object ECDHUtility {
//    A. First set of actions
//    ----------------------------------
//    0. Login or Signup.
//    1. Generate AES Key that is stored in Android KeyStore(A piece of hardware designed for cryptographic keys).
//    2. Generate Elliptic Curve Public Key and Private Key Pair (First version one, later versions 10 or more).
//    3. Write AES encrypted EC KeyPair to database .
//    4. Send EC Public Key to the server.
//
//    B. You create a conversation with a user
//    ------------------------------------------
//    0. Get the user's public key.
//    1. Merge it with your private key and create a shared secret key.
//    2. Derive a new key from KDF that your shared secret key is passed in as an argument.
//    3. Either encrypt the secret key and store it in a column in local database or generate it everytime on conversion screen.

    // b64, B64 -> base64, Base64
    // data -> ByteArray

    private const val TAG = "DBG@CRYPTO@ECDH"
    private const val CURVE_NAME = "secp256r1"

    fun generateSecKeyWithHKDF(dataSecKey: ByteArray): ByteArray {
        val data = ByteArray(32)
        val kdfBytesGenerator = HKDFBytesGenerator(SHA256Digest())
        kdfBytesGenerator.init(HKDFParameters(dataSecKey, null, null))
        kdfBytesGenerator.generateBytes(data, 0, 32)
        return data
    }

    fun generateSecKeyWithArgon2(dataSecretKey: ByteArray): ByteArray {
        val hash = ByteArray(32)
        val numberOfThreads = 1
        val memory = 8192
        val numberOfIterations = 10
        val argon2Generator = Argon2BytesGenerator().apply {
            init(
                Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                    .withMemoryAsKB(memory)
                    .withParallelism(numberOfThreads)
                    .withIterations(numberOfIterations)
                    .build()
            )
        }
        argon2Generator.generateBytes(dataSecretKey, hash)
        return hash
    }

    fun generateECKeys(): KeyPair {
        val ecGenParameterSpec = ECGenParameterSpec(CURVE_NAME)
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(ecGenParameterSpec)
        return keyPairGenerator.generateKeyPair()
    }

    fun generateSharedSecKey(priKey: PrivateKey?, pubKey: PublicKey?): SecretKey {
        val keyAgreement = KeyAgreement.getInstance("ECDH")
        keyAgreement.init(priKey)
        keyAgreement.doPhase(pubKey, true)
        return keyAgreement.generateSecret("AES")
    }

    fun dataPubKeyToPubKey(dataPubKey: ByteArray): PublicKey {
        return KeyFactory.getInstance("EC").generatePublic(X509EncodedKeySpec(dataPubKey))
    }

    fun dataPriKeyToPriKey(dataPriKey: ByteArray): PrivateKey {
        return KeyFactory.getInstance("EC").generatePrivate(PKCS8EncodedKeySpec(dataPriKey))
    }

    fun iOSB64StrPubKeyToPubKey(dataPubKey: ByteArray): PublicKey {
        // Bc,bC  -> BouncyCastle
        // EC  -> Elliptic Curve
        // p,P -> Point
        // j,J -> Java (Standard Java Version), meaning it is specific to standard Java
        val x9ECParamSpec = SECNamedCurves.getByName("secp256r1")
        val curve = x9ECParamSpec.curve
        val bCECPoint = curve.decodePoint(dataPubKey)
        val affineXOnBCEC = bCECPoint.affineXCoord.toBigInteger()
        val affineYOnBCEC = bCECPoint.affineYCoord.toBigInteger()
        val gBcEC = x9ECParamSpec.g
        val xGBcEC = gBcEC.affineXCoord.toBigInteger()
        val yGBcEC = gBcEC.affineYCoord.toBigInteger()
        val hBcEC = x9ECParamSpec.h.toInt()
        val nBcEC = x9ECParamSpec.n
        val jPEC = ECPoint(affineXOnBCEC, affineYOnBCEC)
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

    fun encrypt(data: ByteArray, secKey: SecretKeySpec): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secKey)
        val cipherData = ByteArray(cipher.getOutputSize(data.size))
        var encryptLength = cipher.update(
            data, 0,
            data.size, cipherData, 0
        )
        encryptLength += cipher.doFinal(cipherData, encryptLength)
        return cipherData
    }

    fun decrypt(cipherData: ByteArray, secKey: SecretKeySpec): ByteArray {
        val decryptionKey: Key = SecretKeySpec(secKey.encoded, secKey.algorithm)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, decryptionKey)
        val data = ByteArray(cipher.getOutputSize(cipherData.size))
        var decryptLength = cipher.update(
            cipherData, 0,
            cipherData.size, data, 0
        )
        decryptLength += cipher.doFinal(data, decryptLength)
        return data
    }
}