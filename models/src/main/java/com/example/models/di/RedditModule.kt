package com.example.models.di

import android.content.Context
import com.example.models.models.appsettings.AppSettingsProtoDataStore
import com.example.models.models.posts.PostsRepository
import com.example.models.models.posts.PostsSource
import com.example.models.source.SourceProviderHolder
import com.example.models.source.SourcesProvider
import com.example.models.models.appsettings.model.AppSettingsPrefs
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
    fun provideSourcesProvider(): SourcesProvider {
        return SourceProviderHolder.sourcesProvider

    }

    @Provides
    @Singleton
    fun providePostSource(sourcesProvider: SourcesProvider): PostsSource {
        return sourcesProvider.getPostsSource()
    }

    @Provides
    @Singleton
    fun providePostsRepository(postsSource: PostsSource): PostsRepository {
        return PostsRepository(postsSource)
    }

    @Provides
    @Singleton
    fun provideAppSettingsDataStore(@ApplicationContext context: Context): AppSettingsPrefs {
        return AppSettingsProtoDataStore(context)
    }
}