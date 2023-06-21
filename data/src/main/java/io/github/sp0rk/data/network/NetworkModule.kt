package io.github.sp0rk.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun networkModule() = module {
    single {
        @Suppress("JSON_FORMAT_REDUNDANT")
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }.asConverterFactory(CONTENT_TYPE.toMediaType())
    }
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single {
        OkHttpClient.Builder()
            .addNetworkInterceptor(get<HttpLoggingInterceptor>())
            .writeTimeout(API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .addConverterFactory(get())
            .client(get())
            .baseUrl(BASE_URL)
            .build()
            .create(GithubApi::class.java)
    }
}

private const val CONTENT_TYPE = "application/json"
private const val API_TIMEOUT_SECONDS = 10L
private const val BASE_URL = "https://api.github.com/"
