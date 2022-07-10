package ru.zavanton.db_api

interface DbOutApi {

    fun appDatabase(): IAppDatabase

    fun appDatabaseDao(): IAppDatabaseDao
}

interface DbApiProvider {

    fun provideDbApi(): DbOutApi
}
