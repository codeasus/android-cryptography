package codeasus.projects.encryption.data.repository

import codeasus.projects.encryption.data.entity.EllipticCurveKeyPairEntity
import kotlinx.coroutines.flow.Flow

interface CryptographyRepository {
    suspend fun insertEllipticCurveKeyPairs(ellipticCurveKeyPairs: Set<EllipticCurveKeyPairEntity>)

    suspend fun insertEllipticCurveKeyPair(ellipticCurveKeyPair: EllipticCurveKeyPairEntity)

    fun getAllEllipticCurveKeyPairs(): Flow<List<EllipticCurveKeyPairEntity>>

    suspend fun deleteEllipticCurveKeyPairByID(id: Long)
}