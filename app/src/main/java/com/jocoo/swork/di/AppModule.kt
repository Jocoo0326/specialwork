package com.jocoo.swork.di

import com.jocoo.swork.bean.AppEvent
import com.jocoo.swork.data.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideEventFlow(): MutableSharedFlow<AppEvent> {
        return MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    }
}