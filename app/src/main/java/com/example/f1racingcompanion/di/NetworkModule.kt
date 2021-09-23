package com.example.f1racingcompanion.di

import com.example.f1racingcompanion.api.Formula1Service
import com.example.f1racingcompanion.api.LiveTimingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideFormula1LiveTimingApi(): Formula1Service = Formula1Service.create()

}