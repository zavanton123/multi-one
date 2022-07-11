package ru.zavanton.transactions_api.di

import ru.zavanton.transactions_api.ITransactionRepository

interface TransactionOutApi {

    fun transactionRepository(): ITransactionRepository
}
