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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playECDH()
    }

    private fun playECDH() {
        val androidPublicKeys: MutableList<String> = mutableListOf()
        val secretKeys: MutableList<String> = mutableListOf()

        for (publicKey in iosPublicKeys) {
            val iosPK = ECDHGround.iosB64EncodedStrPKToPK(publicKey)
            val keyPair = ECDHGround.generateECKeys()
            val sharedSecretKey = ECDHGround.generateSharedSecret(keyPair!!.private, iosPK)
            androidPublicKeys.add(Base64.encodeToString(keyPair.public?.encoded, Base64.NO_WRAP))
            secretKeys.add(Base64.encodeToString(sharedSecretKey?.encoded, Base64.NO_WRAP))
        }
        androidPublicKeys.forEach { Log.e(TAG, it) }
        secretKeys.forEach { Log.i(TAG, it) }
    }
}

