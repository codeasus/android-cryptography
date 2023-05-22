package codeasus.projects.data.features.contact.repository

import codeasus.projects.data.features.contact.dao.ContactDAO
import codeasus.projects.data.features.contact.entity.ContactEntity
import codeasus.projects.data.features.contact.mapper.ContactMapper
import codeasus.projects.data.features.contact.model.Contact
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(private val contactDAO: ContactDAO): ContactRepository {

    private val contactMapper = ContactMapper()

    override fun getContactByPhoneNumber(phoneNumber: String): Contact {
        return contactMapper.mapToModel(contactDAO.getContactByPhoneNumber(phoneNumber))
    }

    override fun getContacts(): List<Contact> {
        return contactDAO.getContacts().map { contactMapper.mapToModel(it) }
    }

    override suspend fun insertContact(contact: Contact): Long {
        return  contactDAO.insertContact(contactMapper.mapToEntity(contact))
    }

    override suspend fun insertContacts(contactSet: Set<Contact>): List<Long> {
        val mContactSet = mutableSetOf<ContactEntity>()
        return contactDAO.insertContacts(contactSet.mapTo(mContactSet){ contactMapper.mapToEntity(it)})
    }

    override suspend fun deleteContactByPhoneNumber(phoneNumber: String) {
        contactDAO.deleteContactByPhoneNumber(phoneNumber)
    }

    override suspend fun deleteContacts() {
        contactDAO.deleteContacts()
    }
}