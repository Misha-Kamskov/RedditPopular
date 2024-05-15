package com.example.popularreddit.di

import android.content.Context
import com.example.popularreddit.models.appsettings.AppSettingsProtoDataStore
import com.m.andrii.phonicsabc.models.appsettings.model.AppSettingsPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RedditModule {

    @Provides
    @Singleton
    fun provideAppSettingsDataStore(@ApplicationContext context: Context): AppSettingsPrefs {
        return AppSettingsProtoDataStore(context)
    }

}