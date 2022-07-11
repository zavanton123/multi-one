package ru.zavanton.db_api

interface DatabaseOutApi {

    fun appDatabase(): IAppDatabase

    fun appDatabaseDao(): IAppDatabaseDao
}
