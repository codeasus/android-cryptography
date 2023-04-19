package codeasus.projects.app.features.keypair.viewmodel

import android.app.Application
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import codeasus.projects.security.crypto.ecdh.ECDHUtility
import codeasus.projects.security.crypto.keystore.AndroidKeystore
import codeasus.projects.security.data.entity.EllipticCurveKeyPairEntity
import codeasus.projects.app.data.model.features.keypair.KeyPair
import codeasus.projects.security.data.repository.CryptographyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyPairViewModel @Inject constructor(
    private val cryptographyRepository: CryptographyRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _ecKeyPairs = MutableStateFlow<List<KeyPair>?>(null)
    val ecKeyPairs = _ecKeyPairs.asStateFlow()

    init {
        getAllECKeyPairs()
    }

    private fun getAllECKeyPairs() = viewModelScope.launch(Dispatchers.IO) {
        cryptographyRepository.getAllEllipticCurveKeyPairs().collectLatest {
            _ecKeyPairs.emit(it.map { ecKeyPair ->
                KeyPair(
                    ecKeyPair.id,
                    ecKeyPair.publicKey,
                    ecKeyPair.privateKey
                )
            })
        }
    }

    fun generateECKeyPairs(size: Int = 10) = viewModelScope.launch(Dispatchers.IO) {
        val ecKeyPairSet: MutableSet<EllipticCurveKeyPairEntity> = mutableSetOf()
        for (i in 0 until size) {
            val ecKeyPair = ECDHUtility.generateECKeys()
            val b64EncodedPubKey =
                Base64.encodeToString(ecKeyPair.public.encoded, Base64.NO_WRAP)
            val b64EncodedDecryptedPriKey =
                Base64.encodeToString(
                    AndroidKeystore.encrypt(ecKeyPair.private.encoded),
                    Base64.NO_WRAP
                )
            ecKeyPairSet.add(
                EllipticCurveKeyPairEntity(
                    0,
                    b64EncodedPubKey,
                    b64EncodedDecryptedPriKey
                )
            )
        }
        cryptographyRepository.insertEllipticCurveKeyPairs(ecKeyPairSet)
    }

    fun deleteAllECKeyPairs() = viewModelScope.launch(Dispatchers.IO) {
        cryptographyRepository.deleteEllipticCurveKeyPairs()
    }
}