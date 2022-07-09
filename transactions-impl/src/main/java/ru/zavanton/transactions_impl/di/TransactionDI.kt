package ru.zavanton.transactions_impl.di

import dagger.Binds
import dagger.Component
import dagger.Module
import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_api.di.AccountOutputDependencies
import ru.zavanton.accounts_api.di.AccountOutputDependenciesProvider
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.transactions_api.ITransactionInteractor
import ru.zavanton.transactions_api.ITransactionRepository
import ru.zavanton.transactions_api.di.TransactionOutputDependencies
import ru.zavanton.transactions_impl.data.TransactionRepository
import ru.zavanton.transactions_impl.domain.TransactionInteractor
import ru.zavanton.transactions_impl.ui.TransactionsFragment

interface TransactionInputDependencies {

    fun accountRepository(): IAccountRepository
}

object TransactionComponentInjector {
    private var transactionComponent: TransactionComponent? = null
    private var transactionInputComponent: TransactionInputComponent? = null
    private var transactionOutputComponent: TransactionOutputComponent? = null

    fun getTransactionComponent(): TransactionComponent {
        return transactionComponent
            ?: DaggerTransactionComponent
                .builder()
                .transactionInputDependencies(getTransactionInputComponent())
                .transactionOutputDependencies(getTransactionOutputDependencies())
                .build()
                .apply { transactionComponent = this }
    }

    fun getTransactionOutputDependencies(): TransactionOutputDependencies {
        return transactionOutputComponent
            ?: DaggerTransactionOutputComponent
                .create()
                .apply { transactionOutputComponent = this }
    }

    fun clear() {
        transactionComponent = null
        transactionInputComponent = null
        transactionOutputComponent = null
    }

    private fun getTransactionInputComponent(): TransactionInputDependencies {
        val application = UtilsComponentInjector.utilsComponent.application()
        val provider = application as? AccountOutputDependenciesProvider
            ?: throw Exception("App must implement AccountOutputDependenciesProvider")
        return transactionInputComponent
            ?: DaggerTransactionInputComponent
                .builder()
                .accountOutputDependencies(provider.provideAccountOutputDependencies())
                .build()
                .apply { transactionInputComponent = this }
    }
}

@PerFeature
@Component(
    modules = [
        TransactionInteractorModule::class,
    ],
    dependencies = [
        TransactionOutputDependencies::class,
        TransactionInputDependencies::class,
    ]
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
interface TransactionOutputComponent : TransactionOutputDependencies


@PerFeature
@Component(
    dependencies = [
        AccountOutputDependencies::class,
    ]
)
interface TransactionInputComponent : TransactionInputDependencies

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
