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
import it.unito.progmob.core.domain.state.WalkStateHandler
import it.unito.progmob.home.domain.usecase.DismissPermissionDialogUseCase
import it.unito.progmob.home.domain.usecase.HomeUseCases
import it.unito.progmob.home.domain.usecase.PermissionResultUseCase
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.core.domain.usecase.ReadOnboardingEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUserWeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveOnboardingEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserNameEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserWeightEntryUseCase
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

    @Provides
    @Singleton
    fun provideTrackingServiceManager(
        @ApplicationContext context: Context
    ): TrackingServiceManager = TrackingServiceManagerImpl(context)

    @Provides
    @Singleton
    fun providesWalkStateHandler() = WalkStateHandler()

    @Provides
    @Singleton
    @Named("StepCounterSensor")
    fun provideStepCounterSensor(
        @ApplicationContext context: Context
    ): MeasurableSensor = StepCounterSensor(context)

    @Provides
    @Singleton
    @Named("AccelerometerSensor")
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
        SaveUserNameEntryUseCase(dataStoreManager),
        SaveUserWeightEntryUseCase(dataStoreManager),
        SaveUserHeightEntryUseCase(dataStoreManager)
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
        dataStoreManager: DataStoreManager,
        trackingServiceManager: TrackingServiceManager
    ) = TrackingUseCases(
        StartTrackingUseCase(trackingServiceManager),
        ResumeTrackingUseCase(trackingServiceManager),
        PauseTrackingUseCase(trackingServiceManager),
        StopTrackingUseCase(trackingServiceManager),
        ReadUserWeightEntryUseCase(dataStoreManager),
        ReadUserHeightEntryUseCase(dataStoreManager)
    )
}