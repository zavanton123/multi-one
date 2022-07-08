package ru.zavanton.scanner_impl.di

import retrofit2.Retrofit
import ru.zavanton.db_api.IAppDatabaseDao

interface ScannerDependencies {

    fun retrofit(): Retrofit

    fun appDatabaseDao(): IAppDatabaseDao
}
