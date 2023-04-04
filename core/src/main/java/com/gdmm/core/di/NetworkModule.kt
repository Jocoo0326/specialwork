package com.gdmm.core.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.gdmm.core.BuildConfig
import com.gdmm.core.network.*
import com.gdmm.core.network.interceptor.DateTimeInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL = BuildConfig.BASE_URL
//    const val BASE_URL_2 = BuildConfig.BASE_URL_2

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(DateTimeInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            })
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(
                ApiConverterFactory.Builder()
                    .add(Php::class) { inputStream ->
                        transformInterface(inputStream, BaseResponse::class.java)
                    }
                    .add(Java::class) { inputStream ->
                        transformInterface(inputStream, BaseResp::class.java)
                    }
                    .add(Java2::class) { inputStream ->
                        transformInterface(inputStream, BaseJsonCallback::class.java)
                    }
                    .build()
            ).build()
    }
}