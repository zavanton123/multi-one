package ru.zavanton.network_api

import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface NetworkOutApi {

    fun retrofit(): Retrofit

    fun okHttpClient(): OkHttpClient
}

interface NetworkApiProvider {

    fun provideNetworkApi(): NetworkOutApi
}
