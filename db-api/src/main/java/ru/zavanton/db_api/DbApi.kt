package ru.zavanton.db_api

interface DbApi {

    fun appDatabase(): IAppDatabase

    fun appDatabaseDao(): IAppDatabaseDao
}

interface DbApiProvider {

    fun provideDbApi(): DbApi
}
