package codeasus.projects.data.features.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import codeasus.projects.data.features.contact.dao.ContactDAO
import codeasus.projects.data.features.contact.entity.ContactEntity
import codeasus.projects.data.features.security.dao.EllipticCurveKeyPairDAO
import codeasus.projects.data.features.security.entity.EllipticCurveKeyPairEntity

@Database(
    entities = [
        EllipticCurveKeyPairEntity::class,
        ContactEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AndroidCryptographyDatabase : RoomDatabase() {
    abstract fun getEllipticCurveKeyPairDAO(): EllipticCurveKeyPairDAO
    abstract fun getContactDAO(): ContactDAO
}