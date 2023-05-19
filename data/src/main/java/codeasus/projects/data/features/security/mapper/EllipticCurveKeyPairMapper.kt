package codeasus.projects.data.features.security.mapper

import codeasus.projects.data.features.security.entity.EllipticCurveKeyPairEntity
import codeasus.projects.data.features.security.model.EllipticCurveKeyPair
import codeasus.projects.data.util.Mapper

class EllipticCurveKeyPairMapper: Mapper<EllipticCurveKeyPairEntity, EllipticCurveKeyPair> {
    override fun mapToModel(entity: EllipticCurveKeyPairEntity): EllipticCurveKeyPair {
        return EllipticCurveKeyPair(entity.id, entity.publicKey, entity.privateKey)
    }

    override fun mapToEntity(model: EllipticCurveKeyPair): EllipticCurveKeyPairEntity {
        return EllipticCurveKeyPairEntity(model.id, model.publicKey, model.privateKey)
    }
}