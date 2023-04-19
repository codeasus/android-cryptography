package codeasus.projects.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import codeasus.projects.security.data.dao.CryptographyDAO
import codeasus.projects.security.data.entity.EllipticCurveKeyPairEntity

@Database(
    entities = [
        EllipticCurveKeyPairEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AndroidCryptographyDatabase : RoomDatabase() {
    abstract fun cryptographyDAO(): CryptographyDAO
}