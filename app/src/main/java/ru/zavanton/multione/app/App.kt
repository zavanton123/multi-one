package ru.zavanton.multione.app

import android.app.Application
import android.content.Context
import retrofit2.Retrofit
import ru.zavanton.accounts_api.di.AccountOutApi
import ru.zavanton.accounts_api.di.AccountOutputDependenciesProvider
import ru.zavanton.accounts_impl.di.AccountComponentInjector
import ru.zavanton.db_api.IAppDatabaseDao
import ru.zavanton.db_impl.DatabaseComponentInjector
import ru.zavanton.db_impl.DatabaseInApi
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.network_impl.NetworkComponentInjector
import ru.zavanton.scanner_impl.di.ScannerComponentInjector
import ru.zavanton.scanner_impl.di.ScannerInApi
import ru.zavanton.transactions_api.di.TransactionOutApi
import ru.zavanton.transactions_api.di.TransactionOutputDependenciesProvider
import ru.zavanton.transactions_impl.di.TransactionComponentInjector

class App : Application(),
    AccountOutputDependenciesProvider,
    TransactionOutputDependenciesProvider {

    override fun onCreate() {
        super.onCreate()

        // Make the application available to all the modules
        // (app is needed for their initialization)
        UtilsComponentInjector.init(this)


        DatabaseComponentInjector.databaseInApiFactory = {
            object : DatabaseInApi {
                override fun appContext(): Context {
                    return this@App
                }
            }
        }

        ScannerComponentInjector.scannerInApiFactory = {
            object : ScannerInApi {
                override fun retrofit(): Retrofit {
                    return NetworkComponentInjector.getNetworkOutApi().retrofit()
                }

                override fun appDatabaseDao(): IAppDatabaseDao {
                    return DatabaseComponentInjector.getDatabaseOutApi().appDatabaseDao()
                }
            }
        }
    }

    override fun provideAccountOutputDependencies(): AccountOutApi {
        return AccountComponentInjector.getAccountOutApi()
    }

    override fun provideTransactionOutApi(): TransactionOutApi {
        return TransactionComponentInjector.getTransactionOutApi()
    }
}
