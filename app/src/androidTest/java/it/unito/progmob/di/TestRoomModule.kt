package it.unito.progmob.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import it.unito.progmob.core.data.local.WalkDatabase
import it.unito.progmob.core.data.repository.TargetRepositoryImpl
import it.unito.progmob.core.data.repository.WalkRepositoryImpl
import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.core.domain.repository.WalkRepository
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RoomModule::class])
object TestRoomModule {
    @Provides
    @Singleton
    fun provideWalkDatabase(context: Application): WalkDatabase = Room.inMemoryDatabaseBuilder(
        context,
        WalkDatabase::class.java,
    ).build()

    @Provides
    @Singleton
    fun provideWalkRepository(walkDatabase: WalkDatabase): WalkRepository = WalkRepositoryImpl(walkDatabase.walkDao)

    @Provides
    @Singleton
    fun provideTargetRepository(walkDatabase: WalkDatabase): TargetRepository = TargetRepositoryImpl(walkDatabase.targetDao)
}