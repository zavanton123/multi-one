package ru.zavanton.transactions_impl.di

import dagger.Binds
import dagger.Component
import dagger.Module
import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_api.di.AccountOutputDependencies
import ru.zavanton.accounts_api.di.AccountOutputDependenciesProvider
import ru.zavanton.accounts_api.domain.IAccountInteractor
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.mylibrary.PerScreen
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.transactions_api.ITransactionInteractor
import ru.zavanton.transactions_api.ITransactionRepository
import ru.zavanton.transactions_api.di.TransactionOutputDependencies
import ru.zavanton.transactions_impl.data.TransactionRepository
import ru.zavanton.transactions_impl.domain.TransactionInteractor
import ru.zavanton.transactions_impl.ui.TransactionsFragment

interface TransactionInputDependencies {

    fun accountInteractor(): IAccountInteractor

    fun accountRepository(): IAccountRepository
}

object TransactionComponentInjector {
    private var transactionComponent: TransactionComponent? = null
    private var transactionInputComponent: TransactionInputComponent? = null

    fun getTransactionComponent(): TransactionComponent {
        return transactionComponent
            ?: DaggerTransactionComponent
                .builder()
                .transactionInputDependencies(getTransactionInputComponent())
                .build()
                .apply { transactionComponent = this }
    }

    fun clear() {
        transactionComponent = null
        transactionInputComponent = null
    }

    private fun getTransactionInputComponent(): TransactionInputComponent {
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
        TransactionModule::class,
    ],
    dependencies = [
        TransactionInputDependencies::class,
    ]
)
interface TransactionComponent : TransactionOutputDependencies {

    fun inject(transactionsFragment: TransactionsFragment)
}

@PerScreen
@Component(
    dependencies = [
        AccountOutputDependencies::class,
    ]
)
interface TransactionInputComponent : TransactionInputDependencies

@Module
interface TransactionModule {

    @PerFeature
    @Binds
    fun bindRepository(impl: TransactionRepository): ITransactionRepository

    @PerFeature
    @Binds
    fun bindInteractor(impl: TransactionInteractor): ITransactionInteractor
}
