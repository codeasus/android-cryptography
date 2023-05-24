package codeasus.projects.data.features.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import codeasus.projects.data.features.contact.dao.ContactDAO
import codeasus.projects.data.features.contact.entity.ContactEntity
import codeasus.projects.data.features.security.dao.EllipticCurveKeyPairDAO
import codeasus.projects.data.features.security.dao.HybridCryptoParameterDAO
import codeasus.projects.data.features.security.entity.EllipticCurveKeyPairEntity
import codeasus.projects.data.features.security.entity.HybridCryptoParameterEntity

@Database(
    entities = [
        EllipticCurveKeyPairEntity::class,
        ContactEntity::class,
        HybridCryptoParameterEntity::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class AndroidCryptographyDatabase : RoomDatabase() {
    abstract fun getEllipticCurveKeyPairDAO(): EllipticCurveKeyPairDAO
    abstract fun getContactDAO(): ContactDAO
    abstract fun getHybridCryptoParameterDAO(): HybridCryptoParameterDAO
}