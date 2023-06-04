package codeasus.projects.data.features.contact.mapper

import codeasus.projects.data.features.contact.entity.ContactEntity
import codeasus.projects.data.features.contact.model.Contact
import codeasus.projects.data.util.Mapper

class ContactMapper : Mapper<ContactEntity, Contact> {
    override fun mapToModel(entity: ContactEntity): Contact {
        return Contact(
            entity.phoneNumber,
            entity.displayName,
            entity.rawID,
            entity.lookupKey,
            entity.publicKey
        )
    }

    override fun mapToEntity(model: Contact): ContactEntity {
        return ContactEntity(
            0,
            model.phoneNumber,
            model.displayName,
            model.rawID,
            model.lookupKey,
            model.publicKey
        )
    }
}