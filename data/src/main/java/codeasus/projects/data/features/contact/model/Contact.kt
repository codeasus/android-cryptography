package codeasus.projects.data.features.contact.model

import java.util.Objects

data class Contact(
    val phoneNumber: String? = null,
    val displayName: String? = null,
    val rawID: Long? = null,
    val lookupKey: String? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(phoneNumber)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Contact
        if (this.phoneNumber == other.phoneNumber) return true
        return false
    }
}