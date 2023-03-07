package codeasus.projects.encryption

import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import codeasus.projects.encryption.crypto.ecdh.ECDHGround

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DBG@MainActivity"
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        iosAndroidKeyCompareLogs()
//        androidKeyCompareLogs()
        testAndroidSecretKeys()
    }

    private fun iosAndroidKeyCompareLogs() {
        val androidPublicKeys: MutableList<String> = mutableListOf()
        val secretKeys: MutableList<String> = mutableListOf()

        for (publicKey in iosPublicKeys) {
            val iosPK = ECDHGround.iosB64EncodedStrPKToPK(publicKey)
            val keyPair = ECDHGround.generateECKeys()
            val sharedSecretKey = ECDHGround.generateSharedSecret(keyPair.private, iosPK)
            androidPublicKeys.add(Base64.encodeToString(keyPair.public?.encoded, Base64.NO_WRAP))
            secretKeys.add(Base64.encodeToString(sharedSecretKey.encoded, Base64.NO_WRAP))
        }
        androidPublicKeys.forEach { Log.e(TAG, it) }
        secretKeys.forEach { Log.i(TAG, it) }
    }

    private fun androidKeyCompareLogs() {
        androidPublicKeys.forEach {
            Log.d(
                TAG,
                Base64.encodeToString(
                    ECDHGround.androidB64EncodedStrPKtoPK(it).encoded,
                    Base64.NO_WRAP
                )
            )
        }
    }

    private fun testAndroidSecretKeys() {
        val aKP = ECDHGround.generateECKeys()
        val aB64EncodedStrPK = Base64.encodeToString(aKP.public.encoded, Base64.NO_WRAP)
        val aPK = ECDHGround.androidB64EncodedStrPKtoPK(aB64EncodedStrPK)

        val bKP = ECDHGround.generateECKeys()
        val bB64EncodedStrPK = Base64.encodeToString(bKP.public.encoded, Base64.NO_WRAP)
        val bPK = ECDHGround.androidB64EncodedStrPKtoPK(bB64EncodedStrPK)

        val aSharedSecretKey = ECDHGround.generateSharedSecret(aKP.private, bPK)
        val bSharedsecretkey = ECDHGround.generateSharedSecret(bKP.private, aPK)

        Log.w(TAG, Base64.encodeToString(aSharedSecretKey.encoded, Base64.NO_WRAP))
        Log.w(TAG, Base64.encodeToString(bSharedsecretkey.encoded, Base64.NO_WRAP))
    }
}

