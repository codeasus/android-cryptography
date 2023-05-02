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

    private var iv = SecureRandom().generateSeed(16)


    fun generateSecretKeyWithHKDF(secretKeyEncoded: ByteArray): ByteArray {
        val data = ByteArray(32)
        val kdfBytesGenerator = HKDFBytesGenerator(SHA256Digest())
        kdfBytesGenerator.init(HKDFParameters(secretKeyEncoded, null, null))
        kdfBytesGenerator.generateBytes(data, 0, 32)
        return data
    }

    fun generateSecretKeyWithArgon2(secretKeyEncoded: ByteArray): ByteArray {
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
        argon2Generator.generateBytes(secretKeyEncoded, hash)
        return hash
    }

    fun generateECKeys(): KeyPair {
        val ecGenParameterSpec = ECGenParameterSpec("secp256r1")
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(ecGenParameterSpec)
        return keyPairGenerator.generateKeyPair()
    }

    fun generateSharedSecret(privateKey: PrivateKey?, publicKey: PublicKey?): SecretKey {
        val keyAgreement = KeyAgreement.getInstance("ECDH")
        keyAgreement.init(privateKey)
        keyAgreement.doPhase(publicKey, true)
        return keyAgreement.generateSecret("AES")
    }

    fun b64EncodedStrPKtoPriKey(b64EncodedPK: String): PrivateKey {
        val keyFactory = KeyFactory.getInstance("EC")
        val dataPK = Base64.decode(b64EncodedPK, Base64.NO_WRAP)
        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(dataPK))
    }

    fun androidB64EncodedStrPKtoPK(androidB64EncodedPK: String): PublicKey {
        return KeyFactory.getInstance("EC")
            .generatePublic(X509EncodedKeySpec(Base64.decode(androidB64EncodedPK, Base64.NO_WRAP)))
    }

    fun iosB64EncodedStrPKToPK(iosB64EncodedPK: String): PublicKey {
        // Bc,bC  -> BouncyCastle
        // EC  -> Elliptic Curve
        // p,P -> Point
        // j,J -> Java (Standard Java Version), meaning it is specific to standard Java
        val decodedPK = Base64.decode(iosB64EncodedPK, Base64.NO_WRAP)
        val x9ECParamSpec = SECNamedCurves.getByName("secp256r1")
        val curve = x9ECParamSpec.curve
        val bCECPoint = curve.decodePoint(decodedPK)
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

    fun encrypt(key: SecretKey?, plainText: String): String {
        val ivSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val plainTextBytes = plainText.toByteArray(charset("UTF-8"))
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val cipherText = ByteArray(cipher.getOutputSize(plainTextBytes.size))
        var encryptLength = cipher.update(
            plainTextBytes, 0,
            plainTextBytes.size, cipherText, 0
        )
        encryptLength += cipher.doFinal(cipherText, encryptLength)
        return Base64.encodeToString(cipherText, Base64.NO_WRAP)
    }

    fun decrypt(key: SecretKey?, cipherText: String?): String {
        val decryptionKey: Key = SecretKeySpec(key!!.encoded, key.algorithm)
        val ivSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val cipherTextBytes = Base64.decode(cipherText, Base64.NO_WRAP)
        cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec)
        val plainText = ByteArray(cipher.getOutputSize(cipherTextBytes.size))
        var decryptLength = cipher.update(
            cipherTextBytes, 0,
            cipherTextBytes.size, plainText, 0
        )
        decryptLength += cipher.doFinal(plainText, decryptLength)
        return String(plainText, StandardCharsets.UTF_8)
    }
}