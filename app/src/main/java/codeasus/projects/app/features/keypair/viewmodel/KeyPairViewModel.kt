package codeasus.projects.app.features.keypair.viewmodel

import android.app.Application
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import codeasus.projects.app.data.model.features.keypair.KeyPair
import codeasus.projects.data.features.security.repository.CryptographyRepository
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

    fun deleteAllECKeyPairs() = viewModelScope.launch(Dispatchers.IO) {
        cryptographyRepository.deleteEllipticCurveKeyPairs()
    }
}