package ru.zavanton.multione.app

import android.app.Application
import ru.zavanton.accounts_api.di.AccountOutputDependenciesProvider
import ru.zavanton.db_api.DbApiProvider
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.transactions_api.di.TransactionOutputDependenciesProvider

class App : Application(),
    NetworkApiProvider by DependenciesDelegate,
    DbApiProvider by DependenciesDelegate,
    AccountOutputDependenciesProvider by DependenciesDelegate,
    TransactionOutputDependenciesProvider by DependenciesDelegate {

    override fun onCreate() {
        super.onCreate()

        // Make the application available to all the modules
        // (app is needed for their initialization)
        UtilsComponentInjector.init(this)
    }
}
