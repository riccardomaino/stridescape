package it.unito.progmob.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.unito.progmob.core.data.managers.DataStoreManagerImpl
import it.unito.progmob.core.domain.managers.DataStoreManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDataStoreManager(application: Context): DataStoreManager {
        return DataStoreManagerImpl(application)
    }


}