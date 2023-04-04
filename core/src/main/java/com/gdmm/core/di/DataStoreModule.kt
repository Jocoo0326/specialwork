package com.gdmm.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.gdmm.core.BaseApplication
import com.gdmm.core.extensions.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataStoreModule {
    
    @Provides
    @Singleton
    fun provideDataStore(): DataStore<Preferences> {
        return BaseApplication.applicationContext().dataStore
    }
}