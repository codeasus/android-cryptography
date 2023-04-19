package codeasus.projects.security.data.repository

import codeasus.projects.security.data.entity.EllipticCurveKeyPairEntity
import kotlinx.coroutines.flow.Flow

interface CryptographyRepository {
    suspend fun insertEllipticCurveKeyPairs(ellipticCurveKeyPairs: Set<EllipticCurveKeyPairEntity>)

    suspend fun insertEllipticCurveKeyPair(ellipticCurveKeyPair: EllipticCurveKeyPairEntity)

    suspend fun deleteEllipticCurveKeyPairs()

    fun getAllEllipticCurveKeyPairs(): Flow<List<EllipticCurveKeyPairEntity>>

    suspend fun deleteEllipticCurveKeyPairByID(id: Long)
}