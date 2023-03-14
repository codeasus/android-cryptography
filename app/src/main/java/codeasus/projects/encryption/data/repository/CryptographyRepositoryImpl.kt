package codeasus.projects.encryption.data.repository

import codeasus.projects.encryption.data.dao.CryptographyDAO
import codeasus.projects.encryption.data.entity.EllipticCurveKeyPairEntity
import kotlinx.coroutines.flow.Flow

class CryptographyRepositoryImpl(
    private val cryptographyDAO: CryptographyDAO
) : CryptographyRepository {
    override suspend fun insertEllipticCurveKeyPairs(ellipticCurveKeyPairs: Set<EllipticCurveKeyPairEntity>) {
        cryptographyDAO.insertEllipticCurveKeyPairs(ellipticCurveKeyPairs)
    }

    override suspend fun insertEllipticCurveKeyPair(ellipticCurveKeyPair: EllipticCurveKeyPairEntity) {
        cryptographyDAO.insertEllipticCurveKeyPair(ellipticCurveKeyPair)
    }

    override fun getAllEllipticCurveKeyPairs(): Flow<List<EllipticCurveKeyPairEntity>> {
        return cryptographyDAO.getAllEllipticCurveKeyPairs()
    }

    override suspend fun deleteEllipticCurveKeyPairByID(id: Long) {
        cryptographyDAO.deleteEllipticCurveKeyPairByID(id)
    }
}