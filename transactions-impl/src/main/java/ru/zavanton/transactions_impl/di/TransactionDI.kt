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

// Note: TransactionComponent is not the same as TransactionOutComponent ->
// this way we have solved the circular dependency problem
// between the account and the transaction modules
object TransactionComponentHolder {
    // Initialize in App
    lateinit var transactionInApiFactory: () -> TransactionInApi

    // WeakReference provides automatic memory management ->
    // when there are no more references to the component
    // it is cleared by the Garbage Collector from the memory
    private var transactionComponentWeakRef: WeakReference<TransactionComponent>? = null
    private var transactionOutComponentWeakRef: WeakReference<TransactionOutComponent>? = null

    // A new component instance is created only when needed by calling the lambda (i.e. factory)
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

    // Used for providing transaction-related OutApi in the App
    fun getTransactionOutApi(): TransactionOutApi {
        return getTransactionOutComponent()
    }

    // We get the component from the weak reference.
    // But if it has been cleared, we create a new instance of component by calling the lambda
    // and assign it to the weak reference for further use
    private fun getTransactionOutComponent(): TransactionOutComponent {
        return transactionOutComponentWeakRef?.get()
            ?: transactionOutComponentFactory().apply {
                transactionOutComponentWeakRef = WeakReference(this)
            }
    }

    // Used for field injection in TransactionFragment.
    fun getTransactionComponent(): TransactionComponent {
        return transactionComponentWeakRef?.get()
            ?: transactionComponentFactory(transactionInApiFactory(), getTransactionOutApi())
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
