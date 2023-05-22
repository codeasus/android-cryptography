package codeasus.projects.data.features.contact.repository

import codeasus.projects.data.features.contact.model.Contact


interface ContactRepository {

    fun getContactByPhoneNumber(phoneNumber: String): Contact

    fun getContacts(): List<Contact>

    suspend fun insertContact(contact: Contact): Long

    suspend fun insertContacts(contactSet: Set<Contact>): List<Long>

    suspend fun deleteContactByPhoneNumber(phoneNumber: String)

    suspend fun deleteContacts()
}