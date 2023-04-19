package codeasus.projects.app.data.di

import android.app.Application
import androidx.room.Room
import codeasus.projects.security.data.dao.CryptographyDAO
import codeasus.projects.app.data.db.AndroidCryptographyDatabase
import codeasus.projects.security.data.repository.CryptographyRepository
import codeasus.projects.security.data.repository.impl.CryptographyRepositoryImpl
import codeasus.projects.app.data.util.DatabaseConstants
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