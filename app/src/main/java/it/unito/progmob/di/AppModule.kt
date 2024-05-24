package it.unito.progmob.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.unito.progmob.core.data.manager.DataStoreManagerImpl
import it.unito.progmob.core.data.manager.TrackingServiceManagerImpl
import it.unito.progmob.core.data.manager.LocationTrackingManagerImpl
import it.unito.progmob.core.data.manager.TimeTrackingManagerImpl
import it.unito.progmob.core.domain.manager.DataStoreManager
import it.unito.progmob.core.domain.manager.TrackingServiceManager
import it.unito.progmob.core.domain.manager.LocationTrackingManager
import it.unito.progmob.core.domain.manager.TimeTrackingManager
import it.unito.progmob.core.domain.sensor.AccelerometerSensor
import it.unito.progmob.core.domain.sensor.MeasurableSensor
import it.unito.progmob.core.domain.sensor.StepCounterSensor
import it.unito.progmob.home.domain.usecase.DismissPermissionDialogUseCase
import it.unito.progmob.home.domain.usecase.HomeUseCases
import it.unito.progmob.home.domain.usecase.PermissionResultUseCase
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.onboarding.domain.usecase.ReadOnboardingEntryUseCase
import it.unito.progmob.onboarding.domain.usecase.ReadUserHeightUseCase
import it.unito.progmob.onboarding.domain.usecase.ReadUserNameUseCase
import it.unito.progmob.onboarding.domain.usecase.ReadUserWeightUseCase
import it.unito.progmob.onboarding.domain.usecase.SaveOnboardingEntryUseCase
import it.unito.progmob.onboarding.domain.usecase.SaveUserHeightUseCase
import it.unito.progmob.onboarding.domain.usecase.SaveUserNameUseCase
import it.unito.progmob.onboarding.domain.usecase.SaveUserWeightUseCase
import it.unito.progmob.tracking.domain.usecase.PauseTrackingUseCase
import it.unito.progmob.tracking.domain.usecase.ResumeTrackingUseCase
import it.unito.progmob.tracking.domain.usecase.StartTrackingUseCase
import it.unito.progmob.tracking.domain.usecase.StopTrackingUseCase
import it.unito.progmob.tracking.domain.usecase.TrackingUseCases
import javax.inject.Named
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
    fun provideTimeTrackingManager(): TimeTrackingManager = TimeTrackingManagerImpl()

    // Potrebbe essere un @Binds invece che un @Provides. @Binds permette di iniettare l'interfaccia
    // invece che la classe concreta.
    @Provides
    @Singleton
    fun provideTrackingServiceManager(
        @ApplicationContext context: Context
    ): TrackingServiceManager = TrackingServiceManagerImpl(context)

    @Provides
    @Singleton
    fun provideStepCounterSensor(
        @ApplicationContext context: Context
    ): MeasurableSensor = StepCounterSensor(context)

    @Named("accelerometer")
    @Provides
    @Singleton
    fun provideAccelerometerSensor(
        @ApplicationContext context: Context
    ): MeasurableSensor = AccelerometerSensor(context)

    @Provides
    @Singleton
    fun provideOnBoardingUseCases(
        dataStoreManager: DataStoreManager
    ) = OnBoardingUseCases(
        ReadOnboardingEntryUseCase(dataStoreManager),
        SaveOnboardingEntryUseCase(dataStoreManager),

        SaveUserNameUseCase(dataStoreManager),
        ReadUserNameUseCase(dataStoreManager),

        SaveUserHeightUseCase(dataStoreManager),
        ReadUserHeightUseCase(dataStoreManager),

        SaveUserWeightUseCase(dataStoreManager),
        ReadUserWeightUseCase(dataStoreManager)
    )

    @Provides
    @Singleton
    fun provideHomeUseCases() = HomeUseCases(
        DismissPermissionDialogUseCase(),
        PermissionResultUseCase()
    )

    @Provides
    @Singleton
    fun provideTrackingUseCases(
        trackingServiceManager: TrackingServiceManager
    ) = TrackingUseCases(
        StartTrackingUseCase(trackingServiceManager),
        ResumeTrackingUseCase(trackingServiceManager),
        PauseTrackingUseCase(trackingServiceManager),
        StopTrackingUseCase(trackingServiceManager)
    )
}