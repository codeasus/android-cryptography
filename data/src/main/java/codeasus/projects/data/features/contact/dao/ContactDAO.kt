package codeasus.projects.data.features.contact.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import codeasus.projects.data.features.app.util.DatabaseConstants
import codeasus.projects.data.features.contact.entity.ContactEntity

@Dao
interface ContactDAO {
    @Query("SELECT * FROM ${DatabaseConstants.ENTITY_CONTACT} WHERE phone_number=:phoneNumber")
    fun getContactByPhoneNumber(phoneNumber: String): ContactEntity

    @Query("SELECT * FROM ${DatabaseConstants.ENTITY_CONTACT}")
    fun getContacts(): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contactEntity: ContactEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contactEntitySet: Set<ContactEntity>): List<Long>

    @Query("DELETE FROM ${DatabaseConstants.ENTITY_CONTACT} WHERE phone_number=:phoneNumber")
    suspend fun deleteContactByPhoneNumber(phoneNumber: String)

    @Query("DELETE FROM ${DatabaseConstants.ENTITY_CONTACT}")
    suspend fun deleteContacts()
}