package ru.zavanton.accounts_api.di

import ru.zavanton.accounts_api.data.IAccountRepository

interface AccountOutApi {

    fun accountRepository(): IAccountRepository
}

interface AccountOutputDependenciesProvider {

    fun provideAccountOutputDependencies(): AccountOutApi
}
