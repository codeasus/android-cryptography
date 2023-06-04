package codeasus.projects.data.features.contact.model

import androidx.room.Entity
import codeasus.projects.data.features.app.util.DatabaseConstants
import java.util.Objects

@Entity(tableName = DatabaseConstants.ENTITY_CONTACT)
data class Contact(
    val phoneNumber: String? = null,
    val displayName: String? = null,
    val rawID: Long? = null,
    val lookupKey: String? = null,
    val publicKey: String? = null
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

    override fun toString(): String {
        return "{displayName: $displayName; phoneNumber: $phoneNumber}"
    }
}