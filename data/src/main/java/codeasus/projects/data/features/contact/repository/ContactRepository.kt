package codeasus.projects.data.features.contact.repository

import codeasus.projects.data.features.contact.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getContactByPhoneNumber(phoneNumber: String): Contact

    fun getContactsAsFlow(): Flow<List<Contact>>

    fun getContactsMappedByPhoneNumber(): Map<String, Contact>

    fun getContacts(): List<Contact>

    suspend fun insertContact(contact: Contact): Long

    suspend fun insertContacts(contacts: List<Contact>): List<Long>

    suspend fun deleteContactByPhoneNumber(phoneNumber: String)

    suspend fun deleteContacts()

    suspend fun completeUpdate(contacts: List<Contact>): List<Long>
}