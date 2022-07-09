package ru.zavanton.accounts_impl.domain

import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_api.domain.IAccountInteractor
import ru.zavanton.transactions_api.ITransactionRepository
import javax.inject.Inject

class AccountInteractor @Inject constructor(
    private val accountRepository: IAccountRepository,
    private val transactionRepository: ITransactionRepository,
) : IAccountInteractor {

    override fun loadAccountInfo(id: Long): String {
        val accountInfo = accountRepository.fetchAccountInfo(id)
        val transactionInfo = transactionRepository.fetchTransactionInfo(id)
        return "$accountInfo $transactionInfo"
    }
}
