package codeasus.projects.data.features.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import codeasus.projects.data.features.security.entity.EllipticCurveKeyPairEntity

@Database(
    entities = [
        EllipticCurveKeyPairEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AndroidCryptographyDatabase : RoomDatabase() {
    abstract fun cryptographyDAO(): codeasus.projects.data.features.security.dao.CryptographyDAO
}