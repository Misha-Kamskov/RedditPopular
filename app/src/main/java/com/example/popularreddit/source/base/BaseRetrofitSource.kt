package com.example.popularreddit.source.base

import com.example.popularreddit.source.AppException
import com.example.popularreddit.source.BackendException
import com.example.popularreddit.source.ConnectionException
import com.example.popularreddit.source.ParseBackendResponseException
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

/**
 * Base class for all OkHttp sources.
 */
open class BaseRetrofitSource(
    config: RetrofitConfig
) {

    val retrofit: Retrofit = config.retrofit
    private val errorAdapter = config.gson.getAdapter(ErrorResponseBody::class.java)

    /**
     * Map network and parse exceptions into in-app exceptions.
     * @throws BackendException
     * @throws ParseBackendResponseException
     * @throws ConnectionException
     */
    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: AppException) {
            throw e
        } catch (e: JsonDataException) {
            throw ParseBackendResponseException(e)
        } catch (e: HttpException) {
            throw createBackendException(e)
        } catch (e: IOException) {
            throw ConnectionException(e)
        }
    }

    private fun createBackendException(e: HttpException): Exception {
        return try {
            val errorBody: ErrorResponseBody = errorAdapter.fromJson(
                e.response()!!.errorBody()!!.string()
            )!!
            BackendException(e.code(), errorBody.error)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

    class ErrorResponseBody(
        val error: String
    )

}