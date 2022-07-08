package ru.zavanton.db_api

interface IAppDatabase {

    fun appDatabaseDao(): IAppDatabaseDao
}