package ru.zavanton.scanner_impl.data

import retrofit2.http.GET

interface ScannerNetworkService {

    @GET("users/zavanton123/repos")
    suspend fun load(): List<GithubRepo>
}
