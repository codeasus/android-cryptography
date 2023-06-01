package codeasus.projects.data.features.security.service

import codeasus.projects.data.features.security.repository.HybridCryptoParameterRepository
import codeasus.projects.security.crypto.keyprotector.KeyProtector
import javax.inject.Inject

class HybridKeyManagementService @Inject constructor(
    hybridCryptoParameterRepository: HybridCryptoParameterRepository,
    keyProtector: KeyProtector
): CryptoKeyManagementService