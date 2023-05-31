package codeasus.projects.data.features.security.repository

import codeasus.projects.data.features.security.model.HybridCryptoParameter


interface HybridCryptoParameterRepository {
    fun getHybridCryptoParameterByPhoneNumber(phoneNumber: String): HybridCryptoParameter

    fun getHybridCryptoParameters(): List<HybridCryptoParameter>

    suspend fun insertHybridCryptoParameter(hybridCryptoParameter: HybridCryptoParameter): Long

    suspend fun insertHybridCryptoParameters(hybridCryptoParameterSet: Set<HybridCryptoParameter>): List<Long>

    suspend fun deleteHybridCryptoParameterByPhoneNumber(phoneNumber: String)

    suspend fun deleteHybridCryptoParametersByPhoneNumbers(phoneNumbers: List<String>)

    suspend fun deleteHybridCryptoParameters()
}