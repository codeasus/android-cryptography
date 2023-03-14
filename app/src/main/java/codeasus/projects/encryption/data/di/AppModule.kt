package codeasus.projects.encryption.data.di

import android.app.Application
import androidx.room.Room
import codeasus.projects.encryption.data.dao.CryptographyDAO
import codeasus.projects.encryption.data.db.AndroidCryptographyDatabase
import codeasus.projects.encryption.data.repository.CryptographyRepository
import codeasus.projects.encryption.data.repository.CryptographyRepositoryImpl
import codeasus.projects.encryption.data.util.DatabaseConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesCryptographyDAO(db: AndroidCryptographyDatabase): CryptographyDAO {
        return db.cryptographyDAO()
    }

    @Provides
    @Singleton
    fun providesCryptographyRepository(cryptographyDAO: CryptographyDAO): CryptographyRepository {
        return CryptographyRepositoryImpl(cryptographyDAO)
    }

    @Provides
    @Singleton
    fun providesAndroidCryptographyDatabase(app: Application): AndroidCryptographyDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            AndroidCryptographyDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).build()
    }
}