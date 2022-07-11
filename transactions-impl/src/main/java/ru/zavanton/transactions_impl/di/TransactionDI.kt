package ru.zavanton.transactions_impl.di

import dagger.Binds
import dagger.Component
import dagger.Module
import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_api.di.AccountOutApi
import ru.zavanton.accounts_api.di.AccountOutputDependenciesProvider
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.transactions_api.ITransactionInteractor
import ru.zavanton.transactions_api.ITransactionRepository
import ru.zavanton.transactions_api.di.TransactionOutApi
import ru.zavanton.transactions_impl.data.TransactionRepository
import ru.zavanton.transactions_impl.domain.TransactionInteractor
import ru.zavanton.transactions_impl.ui.TransactionsFragment

interface TransactionInApi {

    fun accountRepository(): IAccountRepository
}

object TransactionComponentHolder {
    private var transactionComponent: TransactionComponent? = null
    private var transactionInApiComponent: TransactionInApiComponent? = null
    private var transactionOutApiComponent: TransactionOutApiComponent? = null

    fun getTransactionComponent(): TransactionComponent {
        return transactionComponent
            ?: DaggerTransactionComponent
                .builder()
                .transactionInApi(getTransactionInApi())
                .transactionOutApi(getTransactionOutApi())
                .build()
                .apply { transactionComponent = this }
    }

    fun getTransactionOutApi(): TransactionOutApi {
        return transactionOutApiComponent
            ?: DaggerTransactionOutApiComponent
                .create()
                .apply { transactionOutApiComponent = this }
    }

    fun clear() {
        transactionComponent = null
        transactionInApiComponent = null
        transactionOutApiComponent = null
    }

    private fun getTransactionInApi(): TransactionInApi {
        return transactionInApiComponent
            ?: DaggerTransactionInApiComponent
                .builder()
                .accountOutApi(provideAccountOutApi())
                .build()
                .apply { transactionInApiComponent = this }
    }

    private fun provideAccountOutApi(): AccountOutApi {
        val application = UtilsComponentInjector.utilsComponent.application()
        val provider = application as? AccountOutputDependenciesProvider
            ?: throw Exception("App must implement AccountOutputDependenciesProvider")
        return provider.provideAccountOutputDependencies()
    }
}

@PerFeature
@Component(
    dependencies = [
        TransactionOutApi::class,
        TransactionInApi::class,
    ],
    modules = [
        TransactionInteractorModule::class,
    ],
)
interface TransactionComponent {

    fun inject(transactionsFragment: TransactionsFragment)
}

@PerFeature
@Component(
    modules = [
        TransactionRepositoryModule::class,
    ]
)
interface TransactionOutApiComponent : TransactionOutApi

@PerFeature
@Component(
    dependencies = [
        AccountOutApi::class,
    ]
)
interface TransactionInApiComponent : TransactionInApi

@Module
interface TransactionRepositoryModule {

    @PerFeature
    @Binds
    fun bindRepository(impl: TransactionRepository): ITransactionRepository
}

@Module
interface TransactionInteractorModule {

    @PerFeature
    @Binds
    fun bindInteractor(impl: TransactionInteractor): ITransactionInteractor
}
