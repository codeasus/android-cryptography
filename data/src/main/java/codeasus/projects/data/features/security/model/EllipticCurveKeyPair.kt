package codeasus.projects.data.features.security.model

import java.util.Objects

data class EllipticCurveKeyPair(
    val id: Long,
    val publicKey: String,
    val privateKey: String
) {
    override fun hashCode(): Int {
        return Objects.hash(id, publicKey, privateKey)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as EllipticCurveKeyPair
        if (this.id == other.id &&
            this.publicKey == other.publicKey &&
            this.privateKey == other.privateKey
        ) return true
        return false
    }
}