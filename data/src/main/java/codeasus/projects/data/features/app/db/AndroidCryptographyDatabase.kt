package codeasus.projects.data.features.app.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import codeasus.projects.data.features.contact.dao.ContactDAO
import codeasus.projects.data.features.contact.entity.ContactEntity

@Database(
    entities = [
        ContactEntity::class
    ],
    version = 2,
    exportSchema = false,
)
abstract class AndroidCryptographyDatabase : RoomDatabase() {
    abstract fun getContactDAO(): ContactDAO
}