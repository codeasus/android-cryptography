package codeasus.projects.data.features.security.mapper

import codeasus.projects.data.features.security.entity.HybridCryptoParameterEntity
import codeasus.projects.data.features.security.model.HybridCryptoParameter
import codeasus.projects.data.util.Mapper

class HybridCryptoParameterMapper :
    Mapper<HybridCryptoParameterEntity, HybridCryptoParameter> {
    override fun mapToModel(entity: HybridCryptoParameterEntity): HybridCryptoParameter {
        return HybridCryptoParameter(entity.id, entity.phoneNumber, entity.publicKey)
    }

    override fun mapToEntity(model: HybridCryptoParameter): HybridCryptoParameterEntity {
        return HybridCryptoParameterEntity(0, model.phoneNumber, model.publicKey)
    }
}