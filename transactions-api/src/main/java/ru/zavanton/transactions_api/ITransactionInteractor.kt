package ru.zavanton.transactions_api

interface ITransactionInteractor {

    fun loadTransactionInfo(id: Long): String
}