package codeasus.projects.data.features.security.repository.impl

import codeasus.projects.data.features.security.dao.HybridCryptoParameterDAO
import codeasus.projects.data.features.security.entity.HybridCryptoParameterEntity
import codeasus.projects.data.features.security.mapper.HybridCryptoParameterMapper
import codeasus.projects.data.features.security.model.HybridCryptoParameter
import codeasus.projects.data.features.security.repository.HybridCryptoParameterRepository
import javax.inject.Inject

class HybridCryptoParameterRepositoryImpl @Inject constructor
    (private val hybridCryptoParameterDAO: HybridCryptoParameterDAO) :
    HybridCryptoParameterRepository {

    private val hybridCryptoParameterMapper = HybridCryptoParameterMapper()

    override fun getHybridCryptoParameterByPhoneNumber(phoneNumber: String): HybridCryptoParameter {
        val entity = hybridCryptoParameterDAO.getHybridCryptoParameterByPhoneNumber(phoneNumber)
        return hybridCryptoParameterMapper.mapToModel(entity)
    }

    override fun getHybridCryptoParameters(): List<HybridCryptoParameter> {
        val entities = hybridCryptoParameterDAO.getHybridCryptoParameters()
        return entities.map { hybridCryptoParameterMapper.mapToModel(it) }
    }

    override suspend fun insertHybridCryptoParameter(hybridCryptoParameter: HybridCryptoParameter): Long {
        val entity = hybridCryptoParameterMapper.mapToEntity(hybridCryptoParameter)
        return hybridCryptoParameterDAO.insertHybridCryptoParameter(entity)
    }

    override suspend fun insertHybridCryptoParameters(hybridCryptoParameterSet: Set<HybridCryptoParameter>): List<Long> {
        val entitySet = mutableSetOf<HybridCryptoParameterEntity>()
        val entities = hybridCryptoParameterSet.mapTo(entitySet) { hybridCryptoParameterMapper.mapToEntity(it) }
        return  hybridCryptoParameterDAO.insertHybridCryptoParameters(entities)
    }

    override suspend fun deleteHybridCryptoParameterByPhoneNumber(phoneNumber: String) {
        hybridCryptoParameterDAO.deleteHybridCryptoParameterByPhoneNumber(phoneNumber)
    }

    override suspend fun deleteHybridCryptoParametersByPhoneNumbers(phoneNumbers: List<String>) {
        hybridCryptoParameterDAO.deleteHybridCryptoParametersByPhoneNumbers(phoneNumbers)
    }

    override suspend fun deleteHybridCryptoParameters() {
        hybridCryptoParameterDAO.deleteHybridCryptoParameters()
    }
}