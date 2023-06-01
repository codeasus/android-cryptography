package codeasus.projects.data.features.security.service

import codeasus.projects.data.features.security.repository.EllipticCurveKeyPairRepository
import codeasus.projects.security.crypto.keyprotector.KeyProtector
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ECDHKeyManagementService @Inject constructor(
    private val eCKeyPairRepository: EllipticCurveKeyPairRepository,
    private val keyProtector: KeyProtector
) : CryptoKeyManagementService