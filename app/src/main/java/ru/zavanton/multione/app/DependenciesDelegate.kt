package ru.zavanton.multione.app

import ru.zavanton.accounts_api.di.AccountOutApi
import ru.zavanton.accounts_api.di.AccountOutputDependenciesProvider
import ru.zavanton.accounts_impl.di.AccountComponentInjector
import ru.zavanton.db_api.DbOutApi
import ru.zavanton.db_api.DbApiProvider
import ru.zavanton.db_impl.DbComponentInjector
import ru.zavanton.network_api.NetworkOutApi
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.network_impl.NetworkComponentInjector
import ru.zavanton.transactions_api.di.TransactionOutApi
import ru.zavanton.transactions_api.di.TransactionOutputDependenciesProvider
import ru.zavanton.transactions_impl.di.TransactionComponentInjector

object DependenciesDelegate :
    NetworkApiProvider,
    DbApiProvider,
    AccountOutputDependenciesProvider,
    TransactionOutputDependenciesProvider {

    override fun provideNetworkApi(): NetworkOutApi {
        return NetworkComponentInjector.getNetworkComponent()
    }

    override fun provideDbApi(): DbOutApi {
        return DbComponentInjector.fetchDbComponent()
    }

    override fun provideAccountOutputDependencies(): AccountOutApi {
        return AccountComponentInjector.getAccountOutApi()
    }

    override fun provideTransactionOutApi(): TransactionOutApi {
        return TransactionComponentInjector.getTransactionOutApi()
    }
}
