package codeasus.projects.data.features.contact.repository

import androidx.room.Transaction
import codeasus.projects.data.features.contact.dao.ContactDAO
import codeasus.projects.data.features.contact.mapper.ContactMapper
import codeasus.projects.data.features.contact.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(private val contactDAO: ContactDAO) :
    ContactRepository {

    private val contactMapper = ContactMapper()

    override fun getContactByPhoneNumber(phoneNumber: String): Contact {
        return contactMapper.mapToModel(contactDAO.getContactByPhoneNumber(phoneNumber))
    }

    override fun getContactsAsFlow(): Flow<List<Contact>> {
        return contactDAO.getContactsAsFlow().map { contacts ->
            contacts.map { contact ->
                contactMapper.mapToModel(contact)
            }
        }
    }

    override fun getContactsMappedByPhoneNumber(): Map<String, Contact> {
        val contactsMappedByPhoneNumber = contactDAO.getContactsMappedByPhoneNumber()
        return contactsMappedByPhoneNumber.mapValues {
            contactMapper.mapToModel(it.value)
        }
    }

    override fun getContacts(): List<Contact> {
        return contactDAO.getContacts().map { contactMapper.mapToModel(it) }
    }

    override suspend fun insertContact(contact: Contact): Long {
        return contactDAO.insertContact(contactMapper.mapToEntity(contact))
    }

    override suspend fun insertContacts(contacts: List<Contact>): List<Long> {
        return contactDAO.insertContacts(contacts.map { contactMapper.mapToEntity(it) })
    }

    override suspend fun deleteContactByPhoneNumber(phoneNumber: String) {
        contactDAO.deleteContactByPhoneNumber(phoneNumber)
    }

    override suspend fun deleteContacts() {
        contactDAO.deleteContacts()
    }

    @Transaction
    override suspend fun completeUpdate(contacts: List<Contact>): List<Long> {
        deleteContacts()
        return insertContacts(contacts)
    }
}