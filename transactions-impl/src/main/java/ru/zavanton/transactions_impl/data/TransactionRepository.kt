package ru.zavanton.transactions_impl.data

import ru.zavanton.transactions_api.ITransactionRepository
import javax.inject.Inject

class TransactionRepository @Inject constructor() : ITransactionRepository {

    override fun fetchTransactionInfo(id: Long): String {
        return "transaction info for id $id"
    }
}
