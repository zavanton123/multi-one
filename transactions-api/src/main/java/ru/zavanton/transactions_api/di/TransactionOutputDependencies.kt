package ru.zavanton.transactions_api.di

import ru.zavanton.transactions_api.ITransactionRepository

interface TransactionOutputDependencies {

    fun transactionRepository(): ITransactionRepository
}

interface TransactionOutputDependenciesProvider {

    fun provideTransactionOutputDependencies(): TransactionOutputDependencies
}
