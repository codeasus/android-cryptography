package codeasus.projects.encryption.features.keypair.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import codeasus.projects.encryption.data.repository.CryptographyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KeyPairViewModel @Inject constructor(
    private val cryptographyRepository: CryptographyRepository,
    application: Application
) : AndroidViewModel(application) {
}