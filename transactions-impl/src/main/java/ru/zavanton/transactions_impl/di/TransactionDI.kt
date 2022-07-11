package ru.zavanton.transactions_impl.di

import dagger.Binds
import dagger.Component
import dagger.Module
import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_api.di.AccountOutApi
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.transactions_api.ITransactionInteractor
import ru.zavanton.transactions_api.ITransactionRepository
import ru.zavanton.transactions_api.di.TransactionOutApi
import ru.zavanton.transactions_impl.data.TransactionRepository
import ru.zavanton.transactions_impl.domain.TransactionInteractor
import ru.zavanton.transactions_impl.ui.TransactionsFragment
import java.lang.ref.WeakReference

interface TransactionInApi {

    fun accountRepository(): IAccountRepository
}

object TransactionComponentHolder {
    // Initialize in App
    lateinit var transactionInApiFactory: () -> TransactionInApi

    private var transactionComponentWeakRef: WeakReference<TransactionComponent>? = null
    private var transactionOutComponentWeakRef: WeakReference<TransactionOutComponent>? = null

    private val transactionComponentFactory: (TransactionInApi, TransactionOutApi) -> TransactionComponent =
        { transactionInApi, transactionOutApi ->
            DaggerTransactionComponent.builder()
                .transactionInApi(transactionInApi)
                .transactionOutApi(transactionOutApi)
                .build()
        }

    private val transactionOutComponentFactory: () -> TransactionOutComponent = {
        DaggerTransactionOutComponent.create()
    }

    fun accessTransactionOutApi(): TransactionOutApi {
        return accessTransactionOutComponent()
    }

    fun accessTransactionOutComponent(): TransactionOutComponent {
        return transactionOutComponentWeakRef?.get()
            ?: transactionOutComponentFactory().apply {
                transactionOutComponentWeakRef = WeakReference(this)
            }
    }

    fun accessTransactionComponent(): TransactionComponent {
        return transactionComponentWeakRef?.get()
            ?: transactionComponentFactory(transactionInApiFactory(), accessTransactionOutApi())
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
interface TransactionOutComponent : TransactionOutApi

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
