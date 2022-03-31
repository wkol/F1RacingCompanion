package com.example.f1racingcompanion.di

import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.api.Formula1Service
import com.example.f1racingcompanion.api.LiveTimingProxyService
import com.example.f1racingcompanion.data.Formula1Repository
import com.example.f1racingcompanion.data.LiveTimingProxyRepository
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.DateParser
import com.example.f1racingcompanion.utils.LiveTimingDataParser
import com.example.f1racingcompanion.utils.NegotiateCookieJar
import com.serjltt.moshi.adapters.FirstElement
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
    fun provideLiveTimingProxyService(okHttpClient: OkHttpClient, moshi: Moshi): LiveTimingProxyService {
        return Retrofit.Builder()
            .baseUrl(Constants.LIVETIMING_PROXY_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(LiveTimingProxyService::class.java)
    }

    @Singleton
    @Provides
    fun provideProxyRepository(api: LiveTimingProxyService): LiveTimingProxyRepository = LiveTimingProxyRepository(api)

    @Singleton
    @Provides
    fun provideRepository(api: Formula1Service): Formula1Repository = Formula1Repository(api)

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder().add(Wrapped.ADAPTER_FACTORY).add(FirstElement.ADAPTER_FACTORY)
            .add(LiveTimingDataParser.Factory).add(DateParser()).add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideOkHTTPClient(cookieJar: NegotiateCookieJar): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
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

    @Provides
    @Singleton
    fun provideCookieJar(): NegotiateCookieJar {
        return NegotiateCookieJar()
    }
}
