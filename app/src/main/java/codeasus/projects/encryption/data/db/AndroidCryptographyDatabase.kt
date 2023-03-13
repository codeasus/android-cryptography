package codeasus.projects.encryption.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import codeasus.projects.encryption.data.dao.EllipticCurveKeyPairEntity

@Database(
    entities = [
        EllipticCurveKeyPairEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AndroidCryptographyDatabase : RoomDatabase() {
    companion object {

        @Volatile
        private var INSTANCE: AndroidCryptographyDatabase? = null

        fun getDatabase(context: Context): AndroidCryptographyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AndroidCryptographyDatabase::class.java,
                    "android_cryptography_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}