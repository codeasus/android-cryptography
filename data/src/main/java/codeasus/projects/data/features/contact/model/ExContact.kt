package codeasus.projects.data.features.contact.model

import java.util.HashSet

data class ExContact(
    val id: Long = 0,
    val rawID: Long,
    val lookupKey: String? = null,
    val displayName: String? = null,
    var phoneNumber: String? = null,
    val phoneNumbers: HashSet<String>
)