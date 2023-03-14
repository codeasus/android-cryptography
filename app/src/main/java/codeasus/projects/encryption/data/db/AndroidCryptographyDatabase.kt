package codeasus.projects.encryption.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import codeasus.projects.encryption.data.dao.CryptographyDAO
import codeasus.projects.encryption.data.entity.EllipticCurveKeyPairEntity

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