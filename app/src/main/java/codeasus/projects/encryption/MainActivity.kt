package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toBlockPaddedUTF8ByteArray
import codeasus.projects.encryption.crypto.CustomCryptoUtils.toUTF8ByteArray
import codeasus.projects.encryption.crypto.IOSSpecificAESCryptoUtil
import codeasus.projects.encryption.crypto.RSACryptoUtil
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DBG@MainActivity"

        private const val base64IOSSecretKeyAES = "QU9ghA/W4UMbQXslGW26AkEsDR00Sdr3yKcHLJP0+Vc="
        private const val base64IOSInitializationVector = "gNRfVR+C8HzZHhA2Ian6Qw=="

        private const val data = "Message: 'əıöğw@#><\",352:?%!)(*?öçş32423🍺🍺🥞🥞😒👌'"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        //performIOSSecretKeyCryptography()
        //performRSAUTF8MessageCryptography()
        performCryptographyWithIOSKeyAndIV()
    }

    private fun setup() {
        RSACryptoUtil.generateKeyPair()
    }

    private fun pKDecryption() {
        val b64Data = "DNkooYGFAO0w4qleK+SHG8g/HRskBhsHBbGOsR3RUYjgtTd0ac02QtuUHM1M2e1WkPwdfj8TNnQ7KwsnQtIcJGx6/a/qR4QWH0vSt/JNszIosTTZ0g6SrfyAbm53tjmQ7SAkMWWEZGj/eULT3HVv391XSF6d9tCiZZ79vMPaJpqK1QiO5MGj9ujCLRnN8WVfOqpTgyPjGuy49ZDlw3ZPRSCGmHZlnkcXloAeaAn+vM7ojHebspuNuU18oSKcKgUW82w0UnD5tvguC3eG64lmN6/JmUy+DdaY+R4ecZSLXJMVhvGxVTE+5A3oo9amfZRXtkLitxDVwZ/oo7+C3Pgd2g=="
        val decodedData = Base64.decode(b64Data, Base64.NO_WRAP)
        val decryptedData = RSACryptoUtil.decrypt(decodedData)
        val utf8EncodedData = String(decryptedData, StandardCharsets.UTF_8)
        Timber.d("DBG@CRYPTO -> SingleActivity: PK: $utf8EncodedData")
    }

    private fun iosPublicKeyEncryption() {
        val b64PK = "MIIBCgKCAQEApfl+NAQAUhfaayLwvd/ZjJqz36p1nN2GjqtKfNcJ06zIvTKUxTJA14jxXcYRdctqzU9t1YLJgwQsKx/s0I81yMWX45Bpn6j/4zKvplV7o/DGMU8gL/aegT8KjGbNSYwah6StLXVlMJKqykehooqgr6YotashXDncuMn5MEHlZIl+vMb31u+7C7cd1j0kGjDyPUQmYdXaOaMPxYmKUoQz4rhzt8r49NpsCHW9HHWB4/3y7fRfu4uHjtKAPASi2gKgYmdoO3iPnBqBAlEWtO9WPcIM0yuQWPqqLhbcDB1A3RgciFzBDlq1HEXRXq773Xd/nEIxzvFsXIO8UZhv9WMXqwIDAQAB"
        val pk = RSACryptoUtil.b64EncodedStrToPK(b64PK)
        val data = "TestPublicKey -> üööğşç"
        val b64EncodedRawData = data.toByteArray(StandardCharsets.UTF_8)
        val encryptedData = RSACryptoUtil.encrypt(b64EncodedRawData, pk)
        val b64EncodedEncryptedData = Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        Timber.d("DBG@CRYPTO -> SingleActivity: PK: $b64EncodedEncryptedData;")
    }

    private fun iosSecretKeyDecryption() {
        val b64SK = "NDhvmLDBLfXcFOpu75JszgLJYu/OlqNyAIeO7z07guc="
        val b64IV = "b9wA8zflE7QYLoPzXhr1NA=="
        val b64Data = "QAKb+MvEgocYpZEG5RzxCg=="
        val iV = AESCryptoUtil.b64EncodedStrToIV(b64IV)
        val sK = AESCryptoUtil.b64EncodedStrToSK(b64SK)
        val decodedB64Data = Base64.decode(b64Data, Base64.NO_WRAP)
        val decryptedData = AESCryptoUtil.decrypt(decodedB64Data, sK, iV)
        val utf8EncodedData = String(decryptedData, StandardCharsets.UTF_8)
        Timber.d("DBG@CRYPTO -> SingleActivity: SK: $utf8EncodedData;")
    }

    private fun performFullMultiplatformCryptography() {
        Log.d(TAG, "Message to be encrypted with IOS crypto keys: $data")

    }

    private fun performCryptographyWithIOSKeyAndIV() {
        IOSSpecificAESCryptoUtil.init(base64IOSSecretKeyAES, base64IOSInitializationVector)
        Log.d(TAG, "Message to be encrypted with IOS Key: $data")
        val byteArrayMessage = data.toUTF8ByteArray()
        val base64BasedByteArrayMessage = Base64.encode(byteArrayMessage, Base64.NO_WRAP)
        val encryptedMessage = IOSSpecificAESCryptoUtil.encrypt(base64BasedByteArrayMessage)
        val base64EncodedEncryptedMessage = Base64.encodeToString(encryptedMessage, Base64.NO_WRAP)
        Log.d(TAG, "IOS Key encrypted message: $base64EncodedEncryptedMessage")
        val byteArrayDecryptedMessage = Base64.decode(base64EncodedEncryptedMessage, Base64.NO_WRAP)
        val decryptedByteArrayMessage = IOSSpecificAESCryptoUtil.decrypt(byteArrayDecryptedMessage)
        val decryptedDecodedMessage = Base64.decode(decryptedByteArrayMessage, Base64.NO_WRAP)
        val strDecryptedMessage = String(decryptedDecodedMessage, StandardCharsets.UTF_8)
        Log.d(
            TAG,
            "IOS Key decrypted message: $strDecryptedMessage, areEqual: ${
                data == strDecryptedMessage
            }"
        )
    }

    private fun performIOSSecretKeyCryptography() {
        Log.d(TAG, "Secret key to be encrypted: $base64IOSSecretKeyAES")
        val byteArraySecretKey = Base64.decode(base64IOSSecretKeyAES, Base64.NO_WRAP)
        val encryptedSecretKey = RSACryptoUtil.encrypt(byteArraySecretKey)
        Log.d(TAG, "Encrypted secret key: $encryptedSecretKey")
        val byteArrayEncryptedSecretKey = Base64.decode(encryptedSecretKey, Base64.NO_WRAP)
        val decryptedSecretKey = RSACryptoUtil.decrypt(byteArrayEncryptedSecretKey)
        val strDecryptedSecretKey = Base64.encodeToString(decryptedSecretKey, Base64.NO_WRAP)
        Log.d(
            TAG,
            "Decrypted secret key: $strDecryptedSecretKey, areEqual: ${
                base64IOSSecretKeyAES == strDecryptedSecretKey
            }"
        )
    }

    private fun performRSAUTF8MessageCryptography() {
        Log.d(TAG, "Message to be encrypted: $data")
        val byteArrayMessage = data.toUTF8ByteArray()
        val base64BasedByteArrayMessage = Base64.encode(byteArrayMessage, Base64.NO_WRAP)
        val encryptedMessage = RSACryptoUtil.encrypt(base64BasedByteArrayMessage)
        Log.d(TAG, "Encrypted message: $encryptedMessage")
        val byteArrayEncryptedMessage = Base64.decode(encryptedMessage, Base64.NO_WRAP)
        val decryptedMessage = RSACryptoUtil.decrypt(byteArrayEncryptedMessage)
        val base64StrDecryptedMessage = Base64.decode(decryptedMessage, Base64.NO_WRAP)
        val strDecryptedMessage = String(base64StrDecryptedMessage, StandardCharsets.UTF_8)
        Log.d(
            TAG, "Decrypted Message: $strDecryptedMessage, areEqual: ${
                data == strDecryptedMessage
            }"
        )
    }

    private fun performCustomPadding(data: String) {
        Log.d(TAG, "Data: $data")
        Log.d(TAG, "Array Data: ${data.toUTF8ByteArray().contentToString()}")
        Log.d(
            TAG,
            "Block Padded Array Data: ${
                data.toBlockPaddedUTF8ByteArray().contentToString()
            }"
        )
    }
}