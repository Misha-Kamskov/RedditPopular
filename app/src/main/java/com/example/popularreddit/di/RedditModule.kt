package com.example.popularreddit.di

import android.content.Context
import com.example.popularreddit.models.appsettings.AppSettingsProtoDataStore
import com.example.popularreddit.models.posts.PostsRepository
import com.example.popularreddit.models.posts.PostsSource
import com.example.popularreddit.source.SourceProviderHolder
import com.example.popularreddit.source.SourcesProvider
import com.example.popularreddit.models.appsettings.model.AppSettingsPrefs
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

    /*@Provides
    fun provideRetrofit(
        factory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    fun provideConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()*/

}