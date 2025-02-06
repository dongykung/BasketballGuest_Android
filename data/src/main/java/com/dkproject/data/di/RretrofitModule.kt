package com.dkproject.data.di

import com.dkproject.data.network.RetrofitInterceptor
import com.dkproject.data.network.SearchPlaceService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object RetrofitModule {

    @Provides
    fun provideOkhttpClient(retrofitInterceptor: RetrofitInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retrofitInterceptor)
            .build()
    }
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl("https://apis.openapi.sk.com")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    fun provideSeaerchPlaceService(retrofit: Retrofit): SearchPlaceService {
        return retrofit.create(SearchPlaceService::class.java)
    }
}