package codeasus.projects.data.features.security.service

import codeasus.projects.data.features.security.model.EllipticCurveKeyPair
import codeasus.projects.data.features.security.repository.EllipticCurveKeyPairRepository
import codeasus.projects.security.crypto.ecdh.ECDHUtility
import codeasus.projects.security.crypto.keyprotector.KeyProtector
import codeasus.projects.security.crypto.util.CryptoUtil.dataToB64StrKey
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ECDHKeyManagementService @Inject constructor(
    private val eCKeyPairRepository: EllipticCurveKeyPairRepository,
    private val keyProtector: KeyProtector
) {

    suspend fun generateEllipticCurveKeyPair() {
        val keyPair = ECDHUtility.generateECKeys()
        val encryptedPriKey = keyProtector.encrypt(keyPair.private.encoded)
        val b64PriKey = dataToB64StrKey(encryptedPriKey)
        val b64PubKey = dataToB64StrKey(keyPair.public.encoded)
        keyPair.private.destroy()
        val eCKeyPair = EllipticCurveKeyPair(
            0,
            b64PubKey,
            b64PriKey
        )
        eCKeyPairRepository.insertEllipticCurveKeyPair(eCKeyPair)
    }

    suspend fun generateEllipticCurveKeyPairs(count: Int) {
        val ecKeyPairSet = mutableSetOf<EllipticCurveKeyPair>()
        repeat(count) {
            val keyPair = ECDHUtility.generateECKeys()
            val encryptedPriKey = keyProtector.encrypt(keyPair.private.encoded)
            val b64PriKey = dataToB64StrKey(encryptedPriKey)
            val b64PubKey = dataToB64StrKey(keyPair.public.encoded)
            ecKeyPairSet.add(
                EllipticCurveKeyPair(
                    0,
                    b64PubKey,
                    b64PriKey
                )
            )
        }
        eCKeyPairRepository.insertEllipticCurveKeyPairs(ecKeyPairSet)
    }

    suspend fun deleteEllipticCurveKeyPairs() {
        eCKeyPairRepository.deleteEllipticCurveKeyPairs()
    }
}