package ru.zavanton.transactions_api

interface ITransactionRepository {

    fun fetchTransactionInfo(id: Long): String
}