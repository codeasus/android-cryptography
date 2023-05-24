package codeasus.projects.data.features.contact.util

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import codeasus.projects.data.features.contact.model.Contact
import codeasus.projects.data.features.contact.model.ExContact
import java.util.HashSet

object ContactUtility {
    private val TAG = "DBG@${ContactUtility::class.java}"

    fun getDeviceContactsMappedByPhoneNumber(ctx: Context): HashMap<String, Contact> {
        val results = hashMapOf<String, Contact>()

        val queryURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projections = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        )

        val cursor = ctx.contentResolver.query(
            queryURI,
            projections,
            null,
            null,
            null
        )

        cursor?.use {
            try {
                val phoneNumberIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val nameIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val contactIDIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
                val lookupKeyIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)

                while (cursor.moveToNext()) {
                    val contactID = cursor.getLongOrNull(contactIDIndex)
                    val lookupKey = cursor.getStringOrNull(lookupKeyIndex)
                    val phone = cursor.getString(phoneNumberIndex)
                    val name = cursor.getString(nameIndex)
                    contactID?.let {
                        val contact = Contact(phone, name, contactID, lookupKey)
                        results.put(phone, contact)
                    }
                }
            } catch (e: SecurityException) {
                Log.e(TAG, "Contact access security exception: ${e.localizedMessage}")
            }
        }
        return results
    }

    fun getDeviceContacts(ctx: Context): MutableList<ExContact> {
        val contactSet: MutableList<ExContact> = mutableListOf()

        val contactContentProvider = ContactsContract.Contacts.CONTENT_URI
        val dataContentProvider = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        // Do not change the order of the array elements
        // If you must change the order or add a new column name,
        // Please update the related columns indexes too
        val contactTableProjections = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        )

        // Do not change the order of the array elements
        // If you must change the order or add a new column name,
        // Please update the related columns indexes too
        val dataTableProjections = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val contactCur = ctx.contentResolver.query(
            contactContentProvider,
            contactTableProjections,
            null,
            null,
            null
        )

        contactCur?.use { cCur ->
            val idColIdx = cCur.getColumnIndex(contactTableProjections[0])
            val lookupKeyColIdx = cCur.getColumnIndex(contactTableProjections[1])
            val displayNameColIdx = cCur.getColumnIndex(contactTableProjections[2])

            var id: Long = 0
            var rawID: Long?
            var lookupKey: String?
            var displayName: String?

            while (cCur.moveToNext()) {
                rawID = cCur.getLongOrNull(idColIdx)
                lookupKey = cCur.getStringOrNull(lookupKeyColIdx)
                displayName = cCur.getStringOrNull(displayNameColIdx)

                if (rawID == null) return@use

                val tempTC = ExContact(
                    id = id++,
                    rawID = rawID,
                    lookupKey = lookupKey,
                    displayName = displayName,
                    phoneNumbers = HashSet()
                )

                val dataCur = ctx.contentResolver.query(
                    dataContentProvider,
                    dataTableProjections,
                    "${dataTableProjections[0]}=?",
                    arrayOf(rawID.toString()),
                    null
                )
                dataCur?.use { dCur ->
                    while (dCur.moveToNext()) {
                        val numberColIdx = dCur.getColumnIndex(dataTableProjections[1])
                        val number = dCur.getString(numberColIdx)
                        tempTC.phoneNumbers.add(number)
                    }
                }
                contactSet.add(tempTC)
            }
        }
        return contactSet
    }

    fun getLocalContactsCount(ctx: Context): Long {
        val contentProvider = ContactsContract.Contacts.CONTENT_URI
        val projections = arrayOf(ContactsContract.Contacts.LOOKUP_KEY)
        val cursor = ctx.contentResolver.query(
            contentProvider,
            projections,
            null,
            null,
            null
        )
        cursor?.use {
            return it.count.toLong()
        }
        return 0
    }
}