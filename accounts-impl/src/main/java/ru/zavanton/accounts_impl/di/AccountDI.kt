package ru.zavanton.accounts_impl.di

import dagger.Binds
import dagger.Component
import dagger.Module
import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_api.di.AccountOutApi
import ru.zavanton.accounts_api.domain.IAccountInteractor
import ru.zavanton.accounts_impl.data.AccountRepository
import ru.zavanton.accounts_impl.domain.AccountInteractor
import ru.zavanton.accounts_impl.ui.AccountFragment
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.transactions_api.ITransactionRepository
import ru.zavanton.transactions_api.di.TransactionOutApi
import ru.zavanton.transactions_api.di.TransactionOutputDependenciesProvider
import java.lang.ref.WeakReference

interface AccountInApi {

    fun transactionRepository(): ITransactionRepository
}

object AccountComponentHolder {

    lateinit var accountInApiFactory: () -> AccountInApi
    private val accountComponentWeakRef: WeakReference<AccountComponent>? = null
    private val accountOutComponentWeakRef: WeakReference<AccountOutComponent>? = null
    private val accountComponentFactory: (AccountInApi, AccountOutApi) -> AccountComponent =
        { accountInApi, accountOutApi ->
            DaggerAccountComponent
                .builder()
                .accountInApi(accountInApi)
                .accountOutApi(accountOutApi)
                .build()
        }

    private var accountComponent: AccountComponent? = null
    private var accountInputComponent: AccountInComponent? = null
    private var accountOutputComponent: AccountOutComponent? = null

    fun getAccountOutApi(): AccountOutApi {
        return accountOutputComponent
            ?: DaggerAccountOutComponent
                .create()
                .apply { accountOutputComponent = this }
    }

    fun getAccountComponent(): AccountComponent {
        return accountComponent
            ?: DaggerAccountComponent
                .builder()
                .accountInApi(getAccountInput())
                .accountOutApi(getAccountOutApi())
                .build()
                .apply {
                    accountComponent = this
                }
    }

    fun clear() {
        accountComponent = null
        accountInputComponent = null
        accountOutputComponent = null
    }

    private fun getAccountInput(): AccountInApi {
        val application = UtilsComponentInjector.utilsComponent.application()

        val provider = application as? TransactionOutputDependenciesProvider
            ?: throw Exception("App must implement TransactionOutputDependenciesProvider ")

        return accountInputComponent
            ?: DaggerAccountInComponent
                .builder()
                .transactionOutApi(provider.provideTransactionOutApi())
                .build()
                .apply {
                    accountInputComponent = this
                }
    }
}

@PerFeature
@Component(
    modules = [
        AccountsInteractorModule::class
    ],
    dependencies = [
        AccountOutApi::class,
        AccountInApi::class,
    ]
)
interface AccountComponent {

    fun inject(accountFragment: AccountFragment)
}

@PerFeature
@Component(
    modules = [
        AccountsRepositoryModule::class
    ]
)
interface AccountOutComponent : AccountOutApi

@PerFeature
@Component(
    dependencies = [
        TransactionOutApi::class,
    ]
)
interface AccountInComponent : AccountInApi

@Module
interface AccountsRepositoryModule {

    @PerFeature
    @Binds
    fun bindRepository(impl: AccountRepository): IAccountRepository
}

@Module
interface AccountsInteractorModule {

    @PerFeature
    @Binds
    fun bindInteractor(impl: AccountInteractor): IAccountInteractor
}
