package codeasus.projects.data.features.app.di

import android.app.Application
import androidx.room.Room
import codeasus.projects.data.features.app.db.AndroidCryptographyDatabase
import codeasus.projects.data.features.security.util.DatabaseConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesCryptographyDAO(db: AndroidCryptographyDatabase): codeasus.projects.data.features.security.dao.CryptographyDAO {
        return db.cryptographyDAO()
    }

    @Provides
    @Singleton
    fun providesCryptographyRepository(cryptographyDAO: codeasus.projects.data.features.security.dao.CryptographyDAO): codeasus.projects.data.features.security.repository.CryptographyRepository {
        return codeasus.projects.data.features.security.repository.impl.CryptographyRepositoryImpl(
            cryptographyDAO
        )
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