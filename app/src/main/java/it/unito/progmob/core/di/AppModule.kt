package it.unito.progmob.core.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.unito.progmob.core.data.managers.DataStoreManagerImpl
import it.unito.progmob.core.domain.managers.DataStoreManager
import it.unito.progmob.onboarding.domain.usecases.ReadOnboardingEntry
import it.unito.progmob.onboarding.domain.usecases.SaveOnboardingEntry
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDataStoreManager(
        application: Application
    ): DataStoreManager = DataStoreManagerImpl(application)

    @Provides
    @Singleton
    fun provideReadOnboardingEntry(
        dataStoreManager: DataStoreManager
    ) = ReadOnboardingEntry(dataStoreManager)


    @Provides
    @Singleton
    fun provideSaveOnboardingEntry(
        dataStoreManager: DataStoreManager
    ) = SaveOnboardingEntry(dataStoreManager)

}