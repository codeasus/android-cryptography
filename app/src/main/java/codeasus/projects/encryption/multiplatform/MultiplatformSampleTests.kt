package codeasus.projects.encryption.multiplatform

import android.util.Base64
import android.util.Log
import codeasus.projects.encryption.crypto.aes.AESCryptographyUtility
import codeasus.projects.encryption.crypto.rsa.RSACryptographyUtility
import java.nio.charset.StandardCharsets

object MultiplatformSampleTests {

    private const val TAG = "DBG@MPSampleTests"

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
        val encryptedData =
            AESCryptographyUtility.encrypt(data.toByteArray(StandardCharsets.UTF_8), pair.first, pair.second)
        val b64EncodedEncryptedData = Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        val pK = RSACryptographyUtility.b64EncodedStrToPKForIOS(iosPK)
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