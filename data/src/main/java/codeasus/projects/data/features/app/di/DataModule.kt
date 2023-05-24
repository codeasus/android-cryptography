package codeasus.projects.data.features.app.di

import android.app.Application
import androidx.room.Room
import codeasus.projects.data.features.app.db.AndroidCryptographyDatabase
import codeasus.projects.data.features.app.util.DatabaseConstants
import codeasus.projects.data.features.contact.dao.ContactDAO
import codeasus.projects.data.features.contact.repository.ContactRepository
import codeasus.projects.data.features.contact.repository.ContactRepositoryImpl
import codeasus.projects.data.features.security.dao.EllipticCurveKeyPairDAO
import codeasus.projects.data.features.security.dao.HybridCryptoParameterDAO
import codeasus.projects.data.features.security.repository.EllipticCurveKeyPairRepository
import codeasus.projects.data.features.security.repository.HybridCryptoParameterRepository
import codeasus.projects.data.features.security.repository.impl.EllipticCurveKeyPairRepositoryImpl
import codeasus.projects.data.features.security.repository.impl.HybridCryptoParameterRepositoryImpl
import codeasus.projects.security.crypto.keyprotector.AESKeyProtector
import codeasus.projects.security.crypto.keyprotector.KeyProtector
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
    fun provideHybridCryptoParameterRepository(hybridCryptoParameterDAO: HybridCryptoParameterDAO): HybridCryptoParameterRepository {
        return HybridCryptoParameterRepositoryImpl(hybridCryptoParameterDAO)
    }
    @Provides
    @Singleton
    fun provideHybridCryptoParameter(database: AndroidCryptographyDatabase): HybridCryptoParameterDAO {
        return database.getHybridCryptoParameterDAO()
    }

    @Provides
    @Singleton
    fun provideContactRepository(contactDAO: ContactDAO): ContactRepository {
        return ContactRepositoryImpl(contactDAO)
    }

    @Provides
    @Singleton
    fun provideContactDAO(database: AndroidCryptographyDatabase): ContactDAO {
        return database.getContactDAO()
    }

    @Provides
    @Singleton
    fun provideKeyProtector(): KeyProtector {
        return AESKeyProtector
    }

    @Provides
    @Singleton
    fun provideEllipticCurveKeyPairRepository(ellipticCurveKeyPairDAO: EllipticCurveKeyPairDAO): EllipticCurveKeyPairRepository {
        return EllipticCurveKeyPairRepositoryImpl(ellipticCurveKeyPairDAO)
    }

    @Provides
    @Singleton
    fun provideEllipticCurveKeyPairDAO(database: AndroidCryptographyDatabase): EllipticCurveKeyPairDAO {
        return database.getEllipticCurveKeyPairDAO()
    }

    @Provides
    @Singleton
    fun providesAndroidCryptographyDatabase(app: Application): AndroidCryptographyDatabase {
        return Room
            .databaseBuilder(
                app.applicationContext,
                AndroidCryptographyDatabase::class.java,
                DatabaseConstants.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}