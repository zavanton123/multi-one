package ru.zavanton.accounts_api.di

import ru.zavanton.accounts_api.data.IAccountRepository
import ru.zavanton.accounts_api.domain.IAccountInteractor

interface AccountOutputDependencies {

    fun accountInteractor(): IAccountInteractor

    fun accountRepository(): IAccountRepository
}

interface AccountOutputDependenciesProvider {

    fun provideAccountOutputDependencies(): AccountOutputDependencies
}
