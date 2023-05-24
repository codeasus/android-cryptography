package codeasus.projects.data.features.security.model

import java.util.Objects

data class HybridCryptoParameter(
    val id: Long,
    val phoneNumber: String,
    val publicKey: String,
) {
    override fun hashCode(): Int {
        return Objects.hash(publicKey)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as HybridCryptoParameter
        if (this.publicKey == other.publicKey) return true
        return false
    }
}
