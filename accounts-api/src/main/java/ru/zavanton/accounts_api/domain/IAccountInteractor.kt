package ru.zavanton.accounts_api.domain

interface IAccountInteractor {

    fun loadAccountInfo(id: Long): String
}