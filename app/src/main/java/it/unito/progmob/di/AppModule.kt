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
import it.unito.progmob.tracking.data.manager.TrackingServiceManagerImpl
import it.unito.progmob.tracking.data.manager.LocationTrackingManagerImpl
import it.unito.progmob.tracking.data.manager.TimeTrackingManagerImpl
import it.unito.progmob.core.domain.manager.DataStoreManager
import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.tracking.domain.manager.TrackingServiceManager
import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import it.unito.progmob.tracking.domain.manager.TimeTrackingManager
import it.unito.progmob.core.domain.sensor.AccelerometerSensor
import it.unito.progmob.core.domain.sensor.MeasurableSensor
import it.unito.progmob.core.domain.sensor.StepCounterSensor
import it.unito.progmob.tracking.domain.service.WalkHandler
import it.unito.progmob.home.domain.usecase.HomeUseCases
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.main.domain.usecase.ReadOnboardingEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUserWeightEntryUseCase
import it.unito.progmob.onboarding.domain.usecase.SaveOnboardingEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserNameEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserWeightEntryUseCase
import it.unito.progmob.tracking.domain.usecase.AddPathPointUseCase
import it.unito.progmob.tracking.domain.usecase.AddWalkUseCase
import it.unito.progmob.main.domain.usecase.CheckTargetExistUseCase
import it.unito.progmob.main.domain.usecase.MainUseCases
import it.unito.progmob.home.domain.usecase.AddTargetUseCase
import it.unito.progmob.home.domain.usecase.GetDateTargetUseCase
import it.unito.progmob.home.domain.usecase.GetDayCaloriesUseCase
import it.unito.progmob.home.domain.usecase.GetDayDistanceUseCase
import it.unito.progmob.home.domain.usecase.GetDayStepsUseCase
import it.unito.progmob.home.domain.usecase.GetDayTimeUseCase
import it.unito.progmob.stats.domain.usecase.StatsUseCases
import it.unito.progmob.home.domain.usecase.GetWeeklyStepsUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthCaloriesStatUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthDistanceStatUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthSpeedStatUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthStepsStatUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthTimeStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearCaloriesStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearDistanceStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearSpeedStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearStepsStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearTimeStatUseCase
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
    fun providesWalkHandler() = WalkHandler()

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
    fun provideMainUseCases(
        dataStoreManager: DataStoreManager,
        targetRepository: TargetRepository
    ) = MainUseCases(
        ReadOnboardingEntryUseCase(dataStoreManager),
        CheckTargetExistUseCase(targetRepository)
    )

    @Provides
    @Singleton
    fun provideOnBoardingUseCases(
        dataStoreManager: DataStoreManager
    ) = OnBoardingUseCases(
        SaveOnboardingEntryUseCase(dataStoreManager),
        SaveUserNameEntryUseCase(dataStoreManager),
        SaveUserWeightEntryUseCase(dataStoreManager),
        SaveUserHeightEntryUseCase(dataStoreManager)
    )

    @Provides
    @Singleton
    fun provideHomeUseCases(
        walkRepository: WalkRepository,
        targetRepository: TargetRepository
    ) = HomeUseCases(
        GetDayStepsUseCase(walkRepository),
        GetDayCaloriesUseCase(walkRepository),
        GetDayDistanceUseCase(walkRepository),
        GetDayTimeUseCase(walkRepository),
        AddTargetUseCase(targetRepository),
        GetDateTargetUseCase(targetRepository),
        GetWeeklyStepsUseCase(walkRepository)
    )

    @Provides
    @Singleton
    fun provideTrackingUseCases(
        dataStoreManager: DataStoreManager,
        trackingServiceManager: TrackingServiceManager,
        walkRepository: WalkRepository
    ) = TrackingUseCases(
        StartTrackingUseCase(trackingServiceManager),
        ResumeTrackingUseCase(trackingServiceManager),
        PauseTrackingUseCase(trackingServiceManager),
        StopTrackingUseCase(trackingServiceManager),
        ReadUserWeightEntryUseCase(dataStoreManager),
        ReadUserHeightEntryUseCase(dataStoreManager),
        AddPathPointUseCase(walkRepository),
        AddWalkUseCase(walkRepository)
    )

     @Provides
     @Singleton
     fun provideStatsUseCases(
         walkRepository: WalkRepository
     ) = StatsUseCases(
         GetWeekOrMonthDistanceStatUseCase(walkRepository),
         GetWeekOrMonthTimeStatUseCase(walkRepository),
         GetWeekOrMonthCaloriesStatUseCase(walkRepository),
         GetWeekOrMonthStepsStatUseCase(walkRepository),
         GetWeekOrMonthSpeedStatUseCase(walkRepository),
         GetYearDistanceStatUseCase(walkRepository),
         GetYearTimeStatUseCase(walkRepository),
         GetYearCaloriesStatUseCase(walkRepository),
         GetYearStepsStatUseCase(walkRepository),
         GetYearSpeedStatUseCase(walkRepository)
     )
}