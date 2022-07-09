package ru.zavanton.accounts_impl.di

import dagger.Binds
import dagger.Component
import dagger.Module
import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_api.di.AccountOutputDependencies
import ru.zavanton.accounts_api.domain.IAccountInteractor
import ru.zavanton.accounts_impl.data.AccountRepository
import ru.zavanton.accounts_impl.domain.AccountInteractor
import ru.zavanton.accounts_impl.ui.AccountFragment
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.transactions_api.ITransactionInteractor
import ru.zavanton.transactions_api.ITransactionRepository
import ru.zavanton.transactions_api.di.TransactionOutputDependencies
import ru.zavanton.transactions_api.di.TransactionOutputDependenciesProvider

interface AccountInputDependencies {

    fun transactionRepository(): ITransactionRepository

    fun transactionInteractor(): ITransactionInteractor
}

object AccountComponentInjector {
    private var accountComponent: AccountComponent? = null
    private var accountInputComponent: AccountInputComponent? = null

    fun getAccountComponent(): AccountComponent {
        return accountComponent
            ?: DaggerAccountComponent
                .builder()
                .accountInputDependencies(getAccountInput())
                .build()
                .apply {
                    accountComponent = this
                }
    }

    fun clear() {
        accountComponent = null
        accountInputComponent = null
    }

    private fun getAccountInput(): AccountInputComponent? {
        val application = UtilsComponentInjector.utilsComponent.application()

        val provider = application as? TransactionOutputDependenciesProvider
            ?: throw Exception("App must implement TransactionOutputDependenciesProvider ")

        return accountInputComponent
            ?: DaggerAccountInputComponent
                .builder()
                .transactionOutputDependencies(provider.provideTransactionOutputDependencies())
                .build()
                .apply {
                    accountInputComponent = this
                }
    }
}

@PerFeature
@Component(
    modules = [
        AccountsModule::class,
    ],
    dependencies = [
        AccountInputDependencies::class,
    ]
)
interface AccountComponent : AccountOutputDependencies {

    fun inject(accountFragment: AccountFragment)
}

@PerFeature
@Component(
    dependencies = [
        TransactionOutputDependencies::class,
    ]
)
interface AccountInputComponent : AccountInputDependencies

@Module
interface AccountsModule {

    @PerFeature
    @Binds
    fun bindInteractor(impl: AccountInteractor): IAccountInteractor

    @PerFeature
    @Binds
    fun bindRepository(impl: AccountRepository): IAccountRepository
}
