package ru.zavanton.multione.app

import android.app.Application
import ru.zavanton.accounts_api.di.AccountOutputDependencies
import ru.zavanton.accounts_api.di.AccountOutputDependenciesProvider
import ru.zavanton.accounts_impl.di.AccountComponentInjector
import ru.zavanton.db_api.DbApi
import ru.zavanton.db_api.DbApiProvider
import ru.zavanton.db_impl.DbComponentInjector
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.network_api.NetworkApi
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.network_impl.NetworkComponentInjector
import ru.zavanton.transactions_api.ITransactionInteractor
import ru.zavanton.transactions_api.ITransactionRepository
import ru.zavanton.transactions_api.di.TransactionOutputDependencies
import ru.zavanton.transactions_api.di.TransactionOutputDependenciesProvider

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

    override fun provideNetworkApi(): NetworkApi {
        return NetworkComponentInjector.getNetworkComponent()
    }

    override fun provideDbApi(): DbApi {
        return DbComponentInjector.fetchDbComponent()
    }

    override fun provideAccountOutputDependencies(): AccountOutputDependencies {
        return AccountComponentInjector.getAccountComponent()
    }

    override fun provideTransactionOutputDependencies(): TransactionOutputDependencies {
        // TODO - remove the mock
        return object : TransactionOutputDependencies {
            override fun transactionInteractor(): ITransactionInteractor {
                return object : ITransactionInteractor {
                    override fun loadTransactionInfo(id: Long): String {
                        return "mocked transaction interactor"
                    }
                }
            }

            override fun transactionRepository(): ITransactionRepository {
                return object : ITransactionRepository {
                    override fun fetchTransactionInfo(id: Long): String {
                        return "mocked transaction repository"
                    }
                }
            }
        }
    }
}
