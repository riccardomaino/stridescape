package it.unito.progmob.di

import android.app.Application
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.unito.progmob.core.data.manager.DataStoreManagerImpl
import it.unito.progmob.core.domain.manager.DataStoreManager
import it.unito.progmob.core.data.manager.LocationTrackingManagerImpl
import it.unito.progmob.core.domain.manager.LocationTrackingManager
import it.unito.progmob.core.stepscounter.MeasurableSensor
import it.unito.progmob.core.stepscounter.StepCounterSensor
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
        @ApplicationContext context: Context
    ): DataStoreManager = DataStoreManagerImpl(context)

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideLocationTrackingManager(
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationTrackingManager = LocationTrackingManagerImpl(fusedLocationProviderClient)

    @Provides
    @Singleton
    fun provideStepCounterSensor(
        @ApplicationContext context: Context
    ): MeasurableSensor {
        return StepCounterSensor(context)
    }

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

}