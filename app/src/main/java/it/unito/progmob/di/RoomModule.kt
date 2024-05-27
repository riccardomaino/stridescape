package it.unito.progmob.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.unito.progmob.core.data.data_source.WalkDatabase
import it.unito.progmob.core.data.repository.TargetRepositoryImpl
import it.unito.progmob.core.data.repository.WalkRepositoryImpl
import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.core.domain.repository.WalkRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideWalkDatabase(context: Application): WalkDatabase = Room.databaseBuilder(
        context,
        WalkDatabase::class.java,
        WalkDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideWalkRepository(walkDatabase: WalkDatabase): WalkRepository = WalkRepositoryImpl(walkDatabase.walkDao)

    @Provides
    @Singleton
    fun provideTargetRepository(walkDatabase: WalkDatabase): TargetRepository = TargetRepositoryImpl(walkDatabase.targetDao)
}