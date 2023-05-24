package codeasus.projects.data.features.security.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import codeasus.projects.data.features.app.util.DatabaseConstants
import codeasus.projects.data.features.contact.entity.ContactEntity

@Entity(
    tableName = DatabaseConstants.ENTITY_HYBRID_CRYPTO_PARAMETER,
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["phone_number"],
            childColumns = ["phone_number"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
data class HybridCryptoParameterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "public_key")
    val publicKey: String,
)