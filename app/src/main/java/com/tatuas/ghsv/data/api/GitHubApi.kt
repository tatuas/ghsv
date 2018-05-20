package com.tatuas.ghsv.data.api

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class GitHubApi {
    companion object {
        private const val BASE_URL = "https://api.github.com/"

        @Volatile
        private var service: GitHubApiService? = null

        fun getService() = service ?: synchronized(this) {
            service ?: buildService()
                    .also { service = it }
        }

        private fun buildService(): GitHubApiService {
            // parser
            val moshi = Moshi.Builder().build()

            // factory
            val converterFactory = MoshiConverterFactory.create(moshi)
            val callAdapterFactory = RxJava2CallAdapterFactory.create()

            // logging
            val logging = HttpLoggingInterceptor(
                    HttpLoggingInterceptor.Logger { Timber.tag("GitHubApi").d(it) })
                    .apply { level = HttpLoggingInterceptor.Level.BODY }

            // client
            val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(callAdapterFactory)
                    .client(client)
                    .build()
                    .create(GitHubApiService::class.java)
        }
    }
}
