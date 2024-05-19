package com.example.popularreddit.source.base

import com.google.gson.Gson
import retrofit2.Retrofit

/**
 * All stuffs required for making HTTP-requests with Retrofit client and
 * for parsing JSON-messages.
 */
class RetrofitConfig(
    val retrofit: Retrofit,
    val gson: Gson
)