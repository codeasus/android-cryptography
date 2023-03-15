package codeasus.projects.encryption.features.keypair.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import codeasus.projects.encryption.data.model.features.keypair.KeyPair
import codeasus.projects.encryption.data.repository.CryptographyRepository
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

    fun getAllECKeyPairs() = viewModelScope.launch(Dispatchers.IO) {
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
//        for (i in 0 until size) {
//            val ecKeyPairSet = muta<>(10) { it * 2}
//        }
    }
}