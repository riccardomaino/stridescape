package it.unito.progmob.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.unito.progmob.core.data.manager.DataStoreManagerImpl
import it.unito.progmob.core.domain.manager.DataStoreManager
import it.unito.progmob.core.stepscounter.MeasurableSensor
import it.unito.progmob.core.stepscounter.ProximitySensor
import it.unito.progmob.home.domain.usecase.DismissPermissionDialogUseCase
import it.unito.progmob.home.domain.usecase.HomeUseCases
import it.unito.progmob.home.domain.usecase.PermissionResultUseCase
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.onboarding.domain.usecase.ReadOnboardingEntryUseCase
import it.unito.progmob.onboarding.domain.usecase.SaveOnboardingEntryUseCase
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
    fun provideOnBoardingUseCases(
        dataStoreManager: DataStoreManager
    ) = OnBoardingUseCases(
        ReadOnboardingEntryUseCase(dataStoreManager),
        SaveOnboardingEntryUseCase(dataStoreManager)
    )

    @Provides
    @Singleton
    fun provideHomeUseCases() = HomeUseCases(
        DismissPermissionDialogUseCase(),
        PermissionResultUseCase()
    )

    @Provides
    @Singleton
    fun provideProximitySensorUseCase(application: Application): MeasurableSensor {
        return ProximitySensor(application)
    }

}