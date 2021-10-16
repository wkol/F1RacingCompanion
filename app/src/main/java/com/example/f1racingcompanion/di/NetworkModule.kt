package com.example.f1racingcompanion.di

import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.api.Formula1Service
import com.example.f1racingcompanion.data.Formula1Repository
import com.example.f1racingcompanion.utils.Constants
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideFormula1LService(okHttpClient: OkHttpClient, moshi: Moshi): Formula1Service {
        return Retrofit.Builder()
            .baseUrl(Constants.LIVETIMING_NEGOTIATE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(Formula1Service::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(api: Formula1Service): Formula1Repository = Formula1Repository(api)

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().add(Wrapped.ADAPTER_FACTORY).add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideOkHTTPClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.HEADERS
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
}
