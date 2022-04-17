package com.example.f1racingcompanion.di

import com.example.f1racingcompanion.BuildConfig
import com.example.f1racingcompanion.api.ErgastService
import com.example.f1racingcompanion.api.LiveTimingFormula1Service
import com.example.f1racingcompanion.data.ErgastRepository
import com.example.f1racingcompanion.data.LiveTimingFormula1Repository
import com.example.f1racingcompanion.utils.Constants
import com.example.f1racingcompanion.utils.DateParser
import com.example.f1racingcompanion.utils.LiveTimingDataParser
import com.example.f1racingcompanion.utils.NegotiateCookieJar
import com.example.f1racingcompanion.utils.PreviousDataParser
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
    fun provideLiveTimingFormula1Service(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): LiveTimingFormula1Service = Retrofit.Builder()
        .baseUrl(Constants.LIVETIMING_NEGOTIATE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(LiveTimingFormula1Service::class.java)

    @Singleton
    @Provides
    fun provideErgastService(okHttpClient: OkHttpClient, moshi: Moshi): ErgastService =
        Retrofit.Builder().baseUrl(Constants.ERGAST_API_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(ErgastService::class.java)

    @Singleton
    @Provides
    fun provideFormula1Repository(api: ErgastService): ErgastRepository =
        ErgastRepository(api)

    @Singleton
    @Provides
    fun provideLiveTimingFormula1Repository(api: LiveTimingFormula1Service): LiveTimingFormula1Repository =
        LiveTimingFormula1Repository(api)

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder().add(Wrapped.ADAPTER_FACTORY).add(FirstElement.ADAPTER_FACTORY)
            .add(PreviousDataParser.Factory)
            .add(LiveTimingDataParser.Factory).add(DateParser()).add(KotlinJsonAdapterFactory())
            .build()

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
