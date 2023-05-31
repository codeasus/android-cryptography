package codeasus.projects.data.features.security.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import codeasus.projects.data.features.app.util.DatabaseConstants
import codeasus.projects.data.features.security.entity.HybridCryptoParameterEntity

@Dao
interface HybridCryptoParameterDAO {
    @Query("SELECT * FROM ${DatabaseConstants.ENTITY_HYBRID_CRYPTO_PARAMETER} WHERE phone_number=:phoneNumber")
    fun getHybridCryptoParameterByPhoneNumber(phoneNumber: String): HybridCryptoParameterEntity

    @Query("SELECT * FROM ${DatabaseConstants.ENTITY_HYBRID_CRYPTO_PARAMETER}")
    fun getHybridCryptoParameters(): List<HybridCryptoParameterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHybridCryptoParameter(hybridCryptoParameter: HybridCryptoParameterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHybridCryptoParameters(hybridCryptoParameterSet: Set<HybridCryptoParameterEntity>): List<Long>

    @Query("DELETE FROM ${DatabaseConstants.ENTITY_HYBRID_CRYPTO_PARAMETER} WHERE phone_number=:phoneNumber")
    suspend fun deleteHybridCryptoParameterByPhoneNumber(phoneNumber: String)

    @Query("DELETE FROM ${DatabaseConstants.ENTITY_HYBRID_CRYPTO_PARAMETER} WHERE phone_number IN (:phoneNumbers)")
    suspend fun deleteHybridCryptoParametersByPhoneNumbers(phoneNumbers: List<String>)

    @Query("DELETE FROM ${DatabaseConstants.ENTITY_HYBRID_CRYPTO_PARAMETER}")
    suspend fun deleteHybridCryptoParameters()
}