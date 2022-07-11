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

object AccountComponentHolder {
    // initialize in App
    lateinit var accountInApiFactory: () -> AccountInApi

    private var accountOutComponentWeakRef: WeakReference<AccountOutComponent>? = null
    private var accountComponentWeakRef: WeakReference<AccountComponent>? = null

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

    fun accessAccountOutApi(): AccountOutApi {
        return accessAccountOutComponent()
    }

    fun accessAccountComponent(): AccountComponent {
        return accountComponentWeakRef?.get()
            ?: accountComponentFactory(accountInApiFactory(), accessAccountOutComponent())
                .apply {
                    accountComponentWeakRef = WeakReference(this)
                }
    }

    fun accessAccountOutComponent(): AccountOutComponent {
        return accountOutComponentWeakRef?.get()
            ?: accountOutComponentFactory().apply {
                accountOutComponentWeakRef = WeakReference(this)
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
