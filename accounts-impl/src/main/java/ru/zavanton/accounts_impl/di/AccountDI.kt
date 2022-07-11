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
import ru.zavanton.transactions_api.ITransactionRepository
import java.lang.ref.WeakReference

interface AccountInApi {

    fun transactionRepository(): ITransactionRepository
}

// Note: AccountComponent is not the same as AccountOutComponent ->
// this way we have solved the circular dependency problem
// between the account and the transaction modules
object AccountComponentHolder {
    // initialize in App
    lateinit var accountInApiFactory: () -> AccountInApi

    // WeakReference provides automatic memory management ->
    // when there are no more references to the component
    // it is cleared by the Garbage Collector from the memory
    private var accountOutComponentWeakRef: WeakReference<AccountOutComponent>? = null
    private var accountComponentWeakRef: WeakReference<AccountComponent>? = null

    // A new component instance is created only when needed by calling the lambda (i.e. factory)
    private val accountComponentFactory: (AccountInApi, AccountOutApi) -> AccountComponent =
        { accountInApi, accountOutApi ->
            DaggerAccountComponent
                .builder()
                .accountInApi(accountInApi)
                .accountOutApi(accountOutApi)
                .build()
        }

    private val accountOutComponentFactory: () -> AccountOutComponent = {
        DaggerAccountOutComponent.create()
    }

    // Used for providing account-related OutApi in the App
    fun getAccountOutApi(): AccountOutApi {
        return getAccountOutComponent()
    }

    // We get the component from the weak reference.
    // But if it has been cleared, we create a new instance of component by calling the lambda
    // and assign it to the weak reference for further use
    private fun getAccountOutComponent(): AccountOutComponent {
        return accountOutComponentWeakRef?.get()
            ?: accountOutComponentFactory().apply {
                accountOutComponentWeakRef = WeakReference(this)
            }
    }

    // Used for field injection in AccountFragment.
    fun getAccountComponent(): AccountComponent {
        return accountComponentWeakRef?.get()
            ?: accountComponentFactory(accountInApiFactory(), getAccountOutComponent())
                .apply {
                    accountComponentWeakRef = WeakReference(this)
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
