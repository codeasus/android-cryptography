package codeasus.projects.data.features.contact.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import codeasus.projects.data.features.app.util.DatabaseConstants
import java.util.Objects

@Entity(
    tableName = DatabaseConstants.ENTITY_CONTACT,
    indices = [Index(value = ["phone_number"], unique = true)],
)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,
    @ColumnInfo(name = "display_name")
    val displayName: String? = null,
    @ColumnInfo(name = "raw_id")
    var rawID: Long? = 0,
    @ColumnInfo(name = "lookup_key")
    var lookupKey: String? = null,
    @ColumnInfo(name = "public_key")
    val publicKey: String?,
) {
    override fun hashCode(): Int {
        return Objects.hash(phoneNumber)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as ContactEntity
        if (this.phoneNumber == other.phoneNumber) return true
        return false
    }
}
