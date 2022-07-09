package ru.zavanton.transactions_api.di

import ru.zavanton.transactions_api.ITransactionInteractor
import ru.zavanton.transactions_api.ITransactionRepository

interface TransactionOutputDependencies {

    fun transactionInteractor(): ITransactionInteractor

    fun transactionRepository(): ITransactionRepository
}

interface TransactionOutputDependenciesProvider {

    fun provideTransactionOutputDependencies(): TransactionOutputDependencies
}
