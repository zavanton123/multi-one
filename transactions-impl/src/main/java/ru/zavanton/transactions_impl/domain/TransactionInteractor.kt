package ru.zavanton.transactions_impl.domain

import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.transactions_api.ITransactionInteractor
import ru.zavanton.transactions_api.ITransactionRepository
import javax.inject.Inject

class TransactionInteractor @Inject constructor(
    private val transactionRepository: ITransactionRepository,
    private val accountRepository: IAccountRepository,
) : ITransactionInteractor {

    override fun loadTransactionInfo(id: Long): String {
        val transactionInfo = transactionRepository.fetchTransactionInfo(id)
        val accountInfo = accountRepository.fetchAccountInfo(id)

        return "$transactionInfo $accountInfo"
    }
}
