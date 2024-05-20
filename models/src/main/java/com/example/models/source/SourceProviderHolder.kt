package com.example.models.source

import com.example.models.models.Const
import com.example.models.source.base.RetrofitConfig
import com.example.models.source.base.RetrofitSourcesProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SourceProviderHolder {

    val sourcesProvider: SourcesProvider by lazy<SourcesProvider> {
        val gson = GsonBuilder().setLenient().create()
        val config = RetrofitConfig(retrofit = createRetrofit(gson), gson = gson)
        RetrofitSourcesProvider(config)
    }

    private fun createRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}
