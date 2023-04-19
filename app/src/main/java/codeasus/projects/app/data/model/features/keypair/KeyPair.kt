package codeasus.projects.app.data.model.features.keypair

import java.util.Objects

data class KeyPair(
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
        other as KeyPair
        if (this.id == other.id &&
            this.publicKey == other.publicKey &&
            this.privateKey == other.privateKey
        ) return true
        return false
    }
}