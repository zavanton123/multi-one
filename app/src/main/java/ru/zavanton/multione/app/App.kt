package ru.zavanton.multione.app

import android.app.Application
import android.content.Context
import retrofit2.Retrofit
import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_impl.di.AccountComponentHolder
import ru.zavanton.accounts_impl.di.AccountInApi
import ru.zavanton.db_api.IAppDatabaseDao
import ru.zavanton.db_impl.DatabaseComponentHolder
import ru.zavanton.db_impl.DatabaseInApi
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.network_impl.NetworkComponentHolder
import ru.zavanton.scanner_impl.di.ScannerComponentHolder
import ru.zavanton.scanner_impl.di.ScannerInApi
import ru.zavanton.transactions_api.ITransactionRepository
import ru.zavanton.transactions_impl.di.TransactionComponentHolder
import ru.zavanton.transactions_impl.di.TransactionInApi

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Make the application available to all the modules
        // (app is needed for their initialization)
        UtilsComponentInjector.init(this)


        // Setup the lambdas to create dependencies for the components in all modules
        DatabaseComponentHolder.databaseInApiFactory = {
            object : DatabaseInApi {
                override fun appContext(): Context {
                    return this@App
                }
            }
        }

        ScannerComponentHolder.scannerInApiFactory = {
            object : ScannerInApi {
                override fun retrofit(): Retrofit {
                    return NetworkComponentHolder.getNetworkOutApi().retrofit()
                }

                override fun appDatabaseDao(): IAppDatabaseDao {
                    return DatabaseComponentHolder.getDatabaseOutApi().appDatabaseDao()
                }
            }
        }

        AccountComponentHolder.accountInApiFactory = {
            object : AccountInApi {
                override fun transactionRepository(): ITransactionRepository {
                    return TransactionComponentHolder.getTransactionOutApi()
                        .transactionRepository()
                }
            }
        }

        TransactionComponentHolder.transactionInApiFactory = {
            object : TransactionInApi {
                override fun accountRepository(): IAccountRepository {
                    return AccountComponentHolder.getAccountOutApi().accountRepository()
                }
            }
        }
    }
}
