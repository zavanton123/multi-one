package ru.zavanton.multione.app

import android.app.Application
import ru.zavanton.accounts_api.di.AccountOutApi
import ru.zavanton.accounts_api.di.AccountOutputDependenciesProvider
import ru.zavanton.accounts_impl.di.AccountComponentInjector
import ru.zavanton.db_api.DatabaseOutApi
import ru.zavanton.db_api.DbApiProvider
import ru.zavanton.db_impl.DatabaseComponentInjector
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.network_api.NetworkOutApi
import ru.zavanton.network_impl.NetworkComponentInjector
import ru.zavanton.transactions_api.di.TransactionOutApi
import ru.zavanton.transactions_api.di.TransactionOutputDependenciesProvider
import ru.zavanton.transactions_impl.di.TransactionComponentInjector

class App : Application(),
    NetworkApiProvider,
    DbApiProvider,
    AccountOutputDependenciesProvider,
    TransactionOutputDependenciesProvider {

    override fun onCreate() {
        super.onCreate()

        // Make the application available to all the modules
        // (app is needed for their initialization)
        UtilsComponentInjector.init(this)
    }

    override fun provideNetworkApi(): NetworkOutApi {
        return NetworkComponentInjector.getNetworkOutApi()
    }

    override fun provideDbApi(): DatabaseOutApi {
        return DatabaseComponentInjector.fetchDbComponent(this)
    }

    override fun provideAccountOutputDependencies(): AccountOutApi {
        return AccountComponentInjector.getAccountOutApi()
    }

    override fun provideTransactionOutApi(): TransactionOutApi {
        return TransactionComponentInjector.getTransactionOutApi()
    }
}
