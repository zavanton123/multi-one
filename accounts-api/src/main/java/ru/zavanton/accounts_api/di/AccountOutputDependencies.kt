package ru.zavanton.accounts_api.di

import ru.zavanton.accounts_api.data.IAccountRepository

interface AccountOutputDependencies {

    fun accountRepository(): IAccountRepository
}

interface AccountOutputDependenciesProvider {

    fun provideAccountOutputDependencies(): AccountOutputDependencies
}
