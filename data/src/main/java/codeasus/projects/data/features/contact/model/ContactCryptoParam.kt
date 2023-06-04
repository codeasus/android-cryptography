package codeasus.projects.data.features.contact.model

import androidx.room.ColumnInfo

data class ContactCryptoParam(
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String?,
    @ColumnInfo(name = "public_key")
    val publicKey: String?,
)