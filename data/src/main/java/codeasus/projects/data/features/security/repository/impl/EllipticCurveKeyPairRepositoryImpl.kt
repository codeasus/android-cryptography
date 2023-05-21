package codeasus.projects.data.features.security.repository.impl

import codeasus.projects.data.features.security.dao.EllipticCurveKeyPairDAO
import codeasus.projects.data.features.security.entity.EllipticCurveKeyPairEntity
import codeasus.projects.data.features.security.model.EllipticCurveKeyPair
import codeasus.projects.data.features.security.mapper.EllipticCurveKeyPairMapper
import codeasus.projects.data.features.security.repository.EllipticCurveKeyPairRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EllipticCurveKeyPairRepositoryImpl(
    private val ellipticCurveKeyPairDAO: EllipticCurveKeyPairDAO
) : EllipticCurveKeyPairRepository {

    private val ecKeyPairMapper = EllipticCurveKeyPairMapper()

    override fun getEllipticCurveKeyPairByID(id: Long): EllipticCurveKeyPair {
        val ecKeyPair = ellipticCurveKeyPairDAO.getEllipticCurveKeyPairByID(id)
        return ecKeyPairMapper.mapToModel(ecKeyPair)
    }

    override fun getEllipticCurveKeyPairs(): List<EllipticCurveKeyPair> {
        val ecKeyPairs = ellipticCurveKeyPairDAO.getEllipticCurveKeyPairs()
        return ecKeyPairs.map { ecKeyPairMapper.mapToModel(it) }
    }

    override suspend fun insertEllipticCurveKeyPair(ellipticCurveKeyPair: EllipticCurveKeyPair): Long {
        val model = ecKeyPairMapper.mapToEntity(ellipticCurveKeyPair)
        return ellipticCurveKeyPairDAO.insertEllipticCurveKeyPair(model)
    }

    override suspend fun insertEllipticCurveKeyPairs(ellipticCurveKeyPairSet: Set<EllipticCurveKeyPair>): List<Long> {
        val ecKeyPairSet = mutableSetOf<EllipticCurveKeyPairEntity>()
        val ecKeyPairs = ellipticCurveKeyPairSet.mapTo(ecKeyPairSet) { ecKeyPairMapper.mapToEntity(it) }
        return ellipticCurveKeyPairDAO.insertEllipticCurveKeyPairs(ecKeyPairs)
    }

    override suspend fun deleteEllipticCurveKeyPairByID(id: Long) {
        ellipticCurveKeyPairDAO.deleteEllipticCurveKeyPairByID(id)
    }

    override suspend fun deleteEllipticCurveKeyPairs() {
        ellipticCurveKeyPairDAO.deleteEllipticCurveKeyPairs()
    }
}