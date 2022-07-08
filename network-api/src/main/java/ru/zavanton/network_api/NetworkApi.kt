package ru.zavanton.network_api

import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface NetworkApi {

    fun retrofit(): Retrofit

    fun okHttpClient(): OkHttpClient
}
