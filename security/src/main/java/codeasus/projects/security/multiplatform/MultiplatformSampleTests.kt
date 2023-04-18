package codeasus.projects.security.multiplatform

import android.util.Base64
import android.util.Log
import codeasus.projects.security.crypto.aes.AESCryptographyUtility
import codeasus.projects.security.crypto.ecdh.ECDHUtility
import codeasus.projects.security.crypto.rsa.RSACryptographyUtility
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.nio.charset.StandardCharsets
import javax.crypto.spec.SecretKeySpec

object MultiplatformSampleTests {

    private const val TAG = "DBG@MPSampleTests"

    private val iosPublicKeys = arrayOf(
        "BDzBIx5p8ez9KmthwDJ+N+tCw+/3j/xdMUL7hAcuCtqJHMVZqD1RoIJ3oKSoJ7qYTkwtoLMavYHBrfRVb9+s0wI=",
        "BCYUx+eUcq2mPnaOmS/lTZ75rrtLRgTF3A6NznkwhRTVVnr+1bJowRvHOmhIH/5sE6v8CdNjr1534Ewqg+hfylY=",
        "BJUQXsuVpuk/4e3jdCGW1JI/3pot4ykAPU6MjW2+HOPoJLzBFXJ8EzwREExgevuGMnYI3JEcJljcrdQLWkmOVVQ=",
        "BAxmQRLm7lHDLjC+ziOlONjGpfzZqk2APdpBR/e+XfWfCl7HzpCNHJPnZsU3O5864prqwL00PoQFw3BbElLR/bk=",
        "BNX2U4LtCEUtCIbw70cOeTSWvUp3MQwN8jpQeOLMfTByR+HhqvWtxhY1I5tJUq6M5cKL2RHXT25gxBTv4jBjJvg=",
        "BNQsbi9EUu0fZcMNUXL9OQqXR1V9/gQh4WlqLyHpMxUnApzl1ZzDaIQu5khfbn5pzzETwoucIyAisEiXJOnF7as=",
        "BKRnfvUjv1ZnRiulcmUMNFlLMdKhAT+UKPO7LKf8QhX5UBecofVcjbY10jYrHqK6449Zb7PMn5RfBFXGiSjiTSU=",
        "BPkeZiryIF7HGYLxADi7JIn5pSG/SgbQG2ZkN9SKS8DgaxbeJ66aJpiz7D/UZfUv6RyULdkvVtw+iI6HmDe0KwA=",
        "BL3DwnaXnj7lORetwUN6VoxSJdvO2qVN+XLjtrKld7XmN27xNHl/SkBq4KgdUy6/O6d7mpGhPAC6/X5EjBvRCEw=",
        "BARYVdxy3QZbwGtsInJCKLE0C5kmIkIaYGxuxXk3d+S8+LAGvzAufmZwg0sIE6FoKn99LNettIXn02bTPzAg+lg=",
        "BGtAXNNzVkDsHLHKTY2KwI1+1ogpS5h7YNZ/SvIbf0+/drk8/EhUMdHLFFT4zUDeBN5LNGami+Td8gOPCYYqsEA=",
        "BCGxIO1FdtevPhZ40vJxF4RMIu2th+VCMfo3AU+qR37vgIHD/mSd2dyhwwCOb1wjXvg1nj00ONVHywgwQfq3l/4=",
        "BCvMNZts2VgbiRErD2WRNmn7OJiLgD+UzjzzhBr3pMelNAn65SCWAG3fAzzLFoMeRghPyLyiNKQ1U2E5+rVXf98=",
        "BPcXah1p0BWkb4ndfuVZGMD0jiJjo/FgLNeak+LyM+yc0cJ1hYxWMMMdJZHuUA1KTgzm86vFuZqXudcL4gMMKm0=",
        "BGHwHiVEWaPbFS8QKCOcMnWBNbMIfCTSyc/Sn2G8k63QirBchByEshCm3Q5lVL90v+8wj0y+yw7pRcaBK+qEO7Y=",
        "BJm1gxQ9dYHB0v0asTGRCVnl2FjXKt8jAl4U8tEsUANt4AWu9sntbMvTKudSbpw8qfabeG+gcGPO1T+Equ4OpcI=",
        "BE43zbUiCfQmQUtmWEMrh2NW5gJ1ZGOuislB0dLiZPZce75FJiQkZXlzeP15y2jAelvx7QoCLOJeBBFcS3GDJKQ=",
        "BD0ykTW9mJpUVS6yAAjrVogCoas8x71ShJSg1TFok/lSHyJDjHwxzSDykmhZXY1vORPeMu3HJfsXwjc5T03dfGs=",
        "BBOyaN8qrgY0pLUn6jHA55GmfbrH/xve9qoxay9hhksJXswsp5f9dTulU/55HuHukCxcmevWhj29fHMhe2kbJ8s=",
        "BIwPQJxbOdgCeTJk6H8CSJr0tCeIwixjKkwRHP1I/5NLqyqmuPG27ouXl4MHJdIeKaoLpzsPmGwpvUWym9TMEcA="
    )

    private val androidPublicKeys = arrayOf(
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEcP5XMHcdZQg/SIWhWax7aM2XZ5HdWZr0iJEauWLsY2IRsfABn6CfVvyl1TnoxQJlu70Lv8USVfdAK6BTcxIICw==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEEtIy/QUl4Ih/P3BOiSdoqwon8zdi2CMTdLTr6CBP1Pa3SJGVlLCqXsCVcpZMQPbvy71pM/pSJ505en3s6Ql07w==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE3ZRGohsSHdx1OL8hC1oGHe8DRx10agZ/TLysx+cwroF93rnV4T0a/Y2u0rs9fwBfgO2oG8oT5j5hl7cMf7gRmw==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEUkzCcsnOUJoFzGw4hfYuVrHbYVvdgv7AFKvCSSzYibWCn2UT+PHdviJ/mzEOWcNji1PQoBsrgPv79pZ0j5Cilg==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE/A7iWQ6FREAGyJx7wgvhbOucGCzqWribxtTMozk2b8hMKWuBrvzZTWdmj4ATGAGXmr7b6EYgBC/6dFC9MCyRlw==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEQy8FPEsMnAVlBeyFWFv12vSZABQ5biapUPHJL4yotEU8ZOGyMZBYjqnYT4qsPkett2TIPvEQyQAXJ4O3/lL6Og==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE5X7wu1Mdn/qOID68eDLFzYiGwrGNG5a8KCVZyGEaY8gjYH+X079ixlZwVA0EvGN78R3HzsffCnoNFMb9ACTNVA==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEPCGpQMTGAoASbEsRtab/R0vMZuQMc1aARp4++oUxhKoElDLqckIEnfzsjRIr+Y5bfft3zLRm5pD1C78V4ZLpWQ==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEOQrxLYQxzlRupxzT0Hq91BOriFhlEAl/U757v6P/eSPxgaRwJVT3TsW0/dXPZBEWb5UdluPFfBVzuDE6BBKGNQ==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEci32XNDd+EXBRD+32JNwX5X9iHCZsvVhkRRRdOdqIGfvbEkHbDUvDYgrhd9fr/MfFj0HZy1KMZSdqv+ISnHoWA==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEUyxNOQh8F9L9yaPOBB9xo14tffQL7qTYmVdQn46ML+x/JbSOt052/0uQzBZcVaV2B0+6JzQP0iXLYCDJobx5ig==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEts3Kd39iOqth277L0UN/9nWtwNgfQ/WyqKjjZjA693nzZ+Y6z2FbiOgfc6kuA3g+kd0CVy+TKvStMR4/fmGXmg==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEvi4bxURHp7uo2PeIJiKcggvsrdPXBH1fOKTVWi/yEp9f7Si3zWGetqyiWjgd7UJAskM4qaQYBNstDT0m6U1S6Q==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEz+051gFXFm3L4UqcX3wD2Dp1JSMqWIDrvy0kqJp/C80GmDbDRi8u3vXXT6o0UjKih9XiPG8MGlT4nhEHh45Adg==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEWxluo7Kere+opCbjC1n10zfF5mKrkpIk5q5oMQylP5faB+xRdmwNMeYYZzJm4KmHiN54yc2l1FG4lIjOA5Yvpw==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEBTGKsaXA3oPJCoRFYJwT7lbPPz5rD7ODkxuf+Urs9EaLpEbBo7q1jnMCCAc/Edfw/uA8xTWclQ37u047NHCdHg==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEVCOx11sQFzuLGHnXdO66uGV+wynpHF+gm4QsgiuhY1umoKWjHg1NhOpv6DevREU7LRB8GCXBV2BfS4ROEvE8RQ==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEwsojMyMsqIssO1wXr1swM1LsN8Q9fp84+xTo4VrRDes25PI89X2K8u3KX8lQhyTyz/LhjhD7MGaiD/oLMNmvVA==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEk1rhQBHIjRlSG8Sj20exuTvNwaPnGQEewdLcTWmKssHgX9+VRjSK6RKrJuak1IGexm+nXA0HEYzX2SPIssO0aA==",
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAExZK+YkNhLLGMSmobl1ocbYWYcp2isx63jXVsdpUg4OKZjSVNy5Ggbc1r18ogRHh2yZILolVy1HymK2L3NaLxGQ=="
    )

    private val allocatedMemories = listOf(8192, 16384, 32768)
    private val numberOfThreads = listOf(1, 2, 4)
    private val numberOfIterations = listOf(10, 20, 30)

    data class Argon2ParameterBundle(
        val memoryAllocatedInKb: Int,
        val numberOfThreadsAllocated: Int,
        val numberOfIterations: Int
    )

    fun benchmarkHKDF(cycles: Int): Long {
        val startTimeInNs = System.nanoTime()
        repeat(cycles) {
            ECDHUtility.generateSecretKeyWithHKDF("password".toByteArray())
        }
        val endTimeInNs = System.nanoTime()
        return (endTimeInNs - startTimeInNs) / cycles
    }

    fun benchmarkArgon2(cycles: Int): Map<Argon2ParameterBundle, Long> {
        val result: MutableMap<Argon2ParameterBundle, Long> = mutableMapOf()

        for (allocatedMemory in allocatedMemories) {
            for (threadCount in numberOfThreads) {
                for (iterationCount in numberOfIterations) {
                    val startTimeInNs = System.nanoTime()
                    for (i in 0 until cycles) {
                        Log.d(TAG, "Generating: {$allocatedMemory, $threadCount, $iterationCount}")
                        val argon2Gen = Argon2BytesGenerator().apply {
                            init(
                                Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                                    .withMemoryAsKB(allocatedMemory)
                                    .withParallelism(threadCount)
                                    .withIterations(iterationCount)
                                    .build()
                            )
                        }
                        argon2Gen.generateBytes("password".toByteArray(), ByteArray(32))
                    }
                    val elapsedTimeInNs = System.nanoTime() - startTimeInNs
                    val avgElapsedTimePerCombinationInNs = elapsedTimeInNs / cycles
                    result[Argon2ParameterBundle(allocatedMemory, threadCount, iterationCount)] =
                        avgElapsedTimePerCombinationInNs
                }
            }
        }
        return result
    }

    fun iosAndroidKeyCompareLogs() {
        val androidPublicKeys: MutableList<String> = mutableListOf()
        val secretKeysAndKDFedDerivations: MutableList<Pair<String, String>> = mutableListOf()

        for (publicKey in iosPublicKeys) {
            val iosPubKey = ECDHUtility.iosB64EncodedStrPKToPK(publicKey)
            val keyPair = ECDHUtility.generateECKeys()
            val secretKey = ECDHUtility.generateSharedSecret(keyPair.private, iosPubKey)
            androidPublicKeys.add(Base64.encodeToString(keyPair.public?.encoded, Base64.NO_WRAP))
            val b64EncodedSecretKey = Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)
            val b64EncodedKDFDerivation = Base64.encodeToString(
                ECDHUtility.generateSecretKeyWithHKDF(secretKey.encoded),
                Base64.NO_WRAP
            )
            secretKeysAndKDFedDerivations.add(Pair(b64EncodedSecretKey, b64EncodedKDFDerivation))
        }
        androidPublicKeys.forEach { Log.e(TAG, it) }
        secretKeysAndKDFedDerivations.forEach { Log.i(TAG, "${it.first} : ${it.second}") }
    }

    fun androidKeyCompareLogs() {
        androidPublicKeys.forEach {
            Log.d(
                TAG,
                Base64.encodeToString(
                    ECDHUtility.androidB64EncodedStrPKtoPK(it).encoded,
                    Base64.NO_WRAP
                )
            )
        }
    }

    private fun testAndroidSecretKeys() {
        val keyPairByA = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByA =
            Base64.encodeToString(keyPairByA.public.encoded, Base64.NO_WRAP)
        val pubKeyByA = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByA)

        val keyPairByB = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByB =
            Base64.encodeToString(keyPairByB.public.encoded, Base64.NO_WRAP)
        val pubKeyByB = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByB)

        val secretKeyByA = ECDHUtility.generateSharedSecret(keyPairByA.private, pubKeyByB)
        val secretKeyByB = ECDHUtility.generateSharedSecret(keyPairByB.private, pubKeyByA)

        Log.w(TAG, Base64.encodeToString(secretKeyByA.encoded, Base64.NO_WRAP))
        Log.w(TAG, Base64.encodeToString(secretKeyByB.encoded, Base64.NO_WRAP))
    }

    fun testAndroidSecretKeysWithHKDF() {
        val keyPairByA = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByA =
            Base64.encodeToString(keyPairByA.public.encoded, Base64.NO_WRAP)
        val pubKeyByA = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByA)

        val keyPairByB = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByB =
            Base64.encodeToString(keyPairByB.public.encoded, Base64.NO_WRAP)
        val pubKeyByB = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByB)

        val secretKeyByA = ECDHUtility.generateSharedSecret(keyPairByA.private, pubKeyByB)
        val secretKeyByB = ECDHUtility.generateSharedSecret(keyPairByB.private, pubKeyByA)

        val hashedSecretKeyByA = ECDHUtility.generateSecretKeyWithHKDF(secretKeyByA.encoded)
        val hashedSecretKeyByB = ECDHUtility.generateSecretKeyWithHKDF(secretKeyByB.encoded)

        Log.w(TAG, Base64.encodeToString(hashedSecretKeyByA, Base64.NO_WRAP))
        Log.w(TAG, Base64.encodeToString(hashedSecretKeyByB, Base64.NO_WRAP))
    }

    fun testAndroidSecretKeysWithArgon2() {
        val keyPairByA = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByA =
            Base64.encodeToString(keyPairByA.public.encoded, Base64.NO_WRAP)
        val pubKeyByA = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByA)

        val keyPairByB = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByB =
            Base64.encodeToString(keyPairByB.public.encoded, Base64.NO_WRAP)
        val pubKeyByB = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByB)

        val secretKeyByA = ECDHUtility.generateSharedSecret(keyPairByA.private, pubKeyByB)
        val secretKeyByB = ECDHUtility.generateSharedSecret(keyPairByB.private, pubKeyByA)

        val hashedByteArraySecretKeyByA =
            ECDHUtility.generateSecretKeyWithArgon2(secretKeyByA.encoded)
        val hashedByteArraySecretKeyByB =
            ECDHUtility.generateSecretKeyWithArgon2(secretKeyByB.encoded)

        Log.w(TAG, Base64.encodeToString(hashedByteArraySecretKeyByA, Base64.NO_WRAP))
        Log.w(TAG, Base64.encodeToString(hashedByteArraySecretKeyByB, Base64.NO_WRAP))
    }

    fun testMessageCryptographyWithHKDF() {
        val keyPairByA = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByA =
            Base64.encodeToString(keyPairByA.public.encoded, Base64.NO_WRAP)
        val pubKeyByA = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByA)

        val keyPairByB = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByB =
            Base64.encodeToString(keyPairByB.public.encoded, Base64.NO_WRAP)
        val pubKeyByB = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByB)

        val secretKeyByA = ECDHUtility.generateSharedSecret(keyPairByA.private, pubKeyByB)
        val secretKeyByB = ECDHUtility.generateSharedSecret(keyPairByB.private, pubKeyByA)

        val hashedByteArraySecretKeyByA = ECDHUtility.generateSecretKeyWithHKDF(secretKeyByA.encoded)
        val hashedByteArraySecretKeyByB = ECDHUtility.generateSecretKeyWithHKDF(secretKeyByB.encoded)

        val hashedSecretKeyByA = SecretKeySpec(
            hashedByteArraySecretKeyByA,
            0,
            hashedByteArraySecretKeyByA.size,
            "AES"
        )

        val hashedSecretKeyByB = SecretKeySpec(
            hashedByteArraySecretKeyByB,
            0,
            hashedByteArraySecretKeyByB.size,
            "AES"
        )

        val message =
            "I don’t understand. Please repeat. – Не понимаю. Повторите пожалуйста üeç.ıəığölış 🥞🍺🍺🤣🤣❤"

        val encryptedMessageByA = ECDHUtility.encrypt(hashedSecretKeyByA, message)
        val encryptedMessageByB = ECDHUtility.encrypt(hashedSecretKeyByB, message)

        val decryptedMessageByA = ECDHUtility.decrypt(hashedSecretKeyByA, encryptedMessageByA)
        val decryptedMessageByB = ECDHUtility.decrypt(hashedSecretKeyByB, encryptedMessageByB)

        Log.w(TAG, "Encrypted Message: $encryptedMessageByA")
        Log.w(TAG, "Encrypted Message: $encryptedMessageByB")

        Log.w(TAG, "Decrypted Message: $decryptedMessageByA")
        Log.w(TAG, "Decrypted Message: $decryptedMessageByB")
    }

    fun testMessageCryptographyWithArgon2() {
        val keyPairByA = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByA =
            Base64.encodeToString(keyPairByA.public.encoded, Base64.NO_WRAP)
        val pubKeyByA = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByA)

        val keyPairByB = ECDHUtility.generateECKeys()
        val b64EncodedStrPubKeyByB =
            Base64.encodeToString(keyPairByB.public.encoded, Base64.NO_WRAP)
        val publicKeyB = ECDHUtility.androidB64EncodedStrPKtoPK(b64EncodedStrPubKeyByB)

        val secretKeyByA = ECDHUtility.generateSharedSecret(keyPairByA.private, publicKeyB)
        val secretKeyByB = ECDHUtility.generateSharedSecret(keyPairByB.private, pubKeyByA)

        val hashedByteArraySecretKeyByA =
            ECDHUtility.generateSecretKeyWithArgon2(secretKeyByA.encoded)
        val hashedByteArraySecretKeyByB =
            ECDHUtility.generateSecretKeyWithArgon2(secretKeyByB.encoded)

        val hashedSecretKeyByA = SecretKeySpec(
            hashedByteArraySecretKeyByA,
            0,
            hashedByteArraySecretKeyByA.size,
            "AES"
        )

        val hashedSecretKeyByB = SecretKeySpec(
            hashedByteArraySecretKeyByB,
            0,
            hashedByteArraySecretKeyByB.size,
            "AES"
        )

        val message = "Salam qaqaş, ürəyim çatdayır. What the heck 👾🍕🥞👾👾😂🤣"

        val encryptedMessageByA = ECDHUtility.encrypt(hashedSecretKeyByA, message)
        val encryptedMessageByB = ECDHUtility.encrypt(hashedSecretKeyByB, message)

        val decryptedMessageByA = ECDHUtility.decrypt(hashedSecretKeyByA, encryptedMessageByA)
        val decryptedMessageByB = ECDHUtility.decrypt(hashedSecretKeyByB, encryptedMessageByB)

        Log.i(TAG, "Encrypted Message: $encryptedMessageByA")
        Log.i(TAG, "Encrypted Message: $encryptedMessageByB")

        Log.i(TAG, "Decrypted Message: $decryptedMessageByA")
        Log.i(TAG, "Decrypted Message: $decryptedMessageByB")
    }

    fun decryptIOSEncryptedData0() {
        val encryptedIOSData =
            "uXhGVYPIhZLJRZWbjiDd7mzGkHCyKt80vf4LwsqCg+CFHZQn8XKHJQGy4VHj877xhnHnckPvMl1czQYNCddrRkyKG1qLZOjr78m31bp83FB/VRoUf/GIvp+zFPVGKJXT5ANXqrEqjRvZyjLzT+Ei9ZsMbC76KXWmVXw1dvde9GmuYomiTkSvwSvFHkYGrtJbRI0HpUkk1nfaPXRFvj5COmpqt9lexpYcZ25uVrUxKIadZ1GSvrWMjeE3P9WFlP56t/5qLJ2gv7cYGKgepyBYCxd2HzayCV75zmXveXviPfXvF/lUQhhyGkcQX+lx4F6pbBldKHJPoLGv31BYraW7eQ=="
        val decodedData = Base64.decode(encryptedIOSData, Base64.NO_WRAP)
        val decryptedData = RSACryptographyUtility.decrypt(decodedData)
        Log.d(TAG, "Decrypted Data: ${String(decryptedData, StandardCharsets.UTF_8)}")
    }

    fun decryptIOSEncryptedData1() {
        val encryptedIOSData =
            "0G+t6U3sqnossmGv7pRGG8xpmvh+asIujtzN6HxYyLkKUYCtpsqm8ZUsxM1rsj43a/Y/8/9+iu2mjAJBR4z8hyVfCnefN92Z393+LPWKUMy8+yM5fvN5uwpzbzFKLJB8"
        val encryptedSK =
            "ZJ0KptsV68dl2IInYeyQDk84gPP7WaBRDSKaG+2+Gp7YiTcpJTT+fpCHtXuwKxLE5qy+OP+XSNRifrIAnL7sb+krYi+6lUdH4qnLBFV8T3odViXvhth6PT/cnUhMaWqY4GQ6jQA4QgaYq/ymlPrGdkZaLOJXbfxggpgvyxm0IASiGfmJHK+uDre2jviNPj+ga+F1Yso7G3YPjUAYBXSp/1wvlf+z5XVHNHzyh7XRGl/z/2JVL2NcACluu6socYr4+qvbutoO6HMGx5Im4oCXF2S4Wq+tzEBcZjSy/Z7u+PaD1e56+oB04aE7aMzuLfYeCn8WB59ttEaY2/G4Snb1Kw=="
        val encryptedIV =
            "jnj5LrPMzztibXs9hMT4VVjv70pywYJOjZBX8w+huqc9fsqJ6NYOm+mEd55qrTtbskvhAusm4dwtnxjNxOoNGv9eorV6VoE9DHGioaDssZTpFADDiVdspD5jAFAzWtzIGcKfRnz40a8PmTqVY5biCEJeTp7V+7O/H5DklZgnv3U/QVEhR1AKQygDKNUmda+qbbUlFO3Tidw5lywffIW8ItdeuFlKOOm15o6POADxHZs9plr824EQsf5vogSQ7PdOH2rnA8C+H1pww82y7++F+2C5xvu2NtTCAF+RT9M3iVL163HXi3pRYkZhuu1nEv84HAYLDuog5tEObu9q17peRw=="
        val decodedData = Base64.decode(encryptedIOSData, Base64.NO_WRAP)
        val decodedSK = Base64.decode(encryptedSK, Base64.NO_WRAP)
        val decodedIV = Base64.decode(encryptedIV, Base64.NO_WRAP)
        val decryptedSK = RSACryptographyUtility.decrypt(decodedSK)
        val decryptedIV = RSACryptographyUtility.decrypt(decodedIV)
        val sK = AESCryptographyUtility.b64EncodedByteArrayToSK(decryptedSK)
        val iV = AESCryptographyUtility.b64EncodedByteArrayToIV(decryptedIV)
        val decryptedData = AESCryptographyUtility.decrypt(decodedData, sK, iV)
        Log.d(TAG, "Decrypted Data: ${String(decryptedData, StandardCharsets.UTF_8)}")
    }

    fun encryptDataForIOS() {
        val data = "Message: 'əıöğw@#><\",352:?%!)(*?öçş32423🍺🍺🥞🥞😒👌'"
        val iosPK =
            "MIIBCgKCAQEApfl+NAQAUhfaayLwvd/ZjJqz36p1nN2GjqtKfNcJ06zIvTKUxTJA14jxXcYRdctqzU9t1YLJgwQsKx/s0I81yMWX45Bpn6j/4zKvplV7o/DGMU8gL/aegT8KjGbNSYwah6StLXVlMJKqykehooqgr6YotashXDncuMn5MEHlZIl+vMb31u+7C7cd1j0kGjDyPUQmYdXaOaMPxYmKUoQz4rhzt8r49NpsCHW9HHWB4/3y7fRfu4uHjtKAPASi2gKgYmdoO3iPnBqBAlEWtO9WPcIM0yuQWPqqLhbcDB1A3RgciFzBDlq1HEXRXq773Xd/nEIxzvFsXIO8UZhv9WMXqwIDAQAB"
        val pair = AESCryptographyUtility.getSKAndIVPair()
        val encryptedData = AESCryptographyUtility.encrypt(
            data.toByteArray(StandardCharsets.UTF_8),
            pair.first,
            pair.second
        )
        val b64EncodedEncryptedData = Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        val pK = RSACryptographyUtility.iOSB64EncodedStrPKToPK(iosPK)
        val encryptedSK = RSACryptographyUtility.encrypt(pair.first.encoded, pK)
        val encryptedIV = RSACryptographyUtility.encrypt(pair.second.iv, pK)
        val b64EncodedEncryptedSK = Base64.encodeToString(encryptedSK, Base64.NO_WRAP)
        val b64EncodedEncryptedIV = Base64.encodeToString(encryptedIV, Base64.NO_WRAP)

        Log.d(TAG, "b64EncodedEncryptedData: $b64EncodedEncryptedData;")
        Log.d(TAG, "b64EncodedEncryptedSK: $b64EncodedEncryptedSK;")
        Log.d(TAG, "b64EncodedEncryptedIV: $b64EncodedEncryptedIV;")
    }

    fun setupRSA() {
        RSACryptographyUtility.generateKeyPair()
    }
}