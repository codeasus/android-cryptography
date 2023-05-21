package codeasus.projects.data.features.security.repository

import codeasus.projects.data.features.security.model.EllipticCurveKeyPair
import kotlinx.coroutines.flow.Flow

interface EllipticCurveKeyPairRepository {

    fun getEllipticCurveKeyPairByID(id: Long): EllipticCurveKeyPair

    fun getEllipticCurveKeyPairs(): List<EllipticCurveKeyPair>

    suspend fun insertEllipticCurveKeyPair(ellipticCurveKeyPair: EllipticCurveKeyPair): Long

    suspend fun insertEllipticCurveKeyPairs(ellipticCurveKeyPairSet: Set<EllipticCurveKeyPair>): List<Long>

    suspend fun deleteEllipticCurveKeyPairByID(id: Long)

    suspend fun deleteEllipticCurveKeyPairs()
}