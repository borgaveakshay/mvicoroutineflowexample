package com.example.gitusersassignment.base.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun getOkhttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.apply {
                addHeader("Accept", "application/vnd.github+json")
                addHeader("auth", "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")
                addHeader("X-GitHub-Api-Version", "2022-11-28")
            }
            chain.proceed(builder.build())
        }.build()
}