package ru.zavanton.accounts_api.data

interface IAccountRepository {

    fun fetchAccountInfo(id: Long): String
}
