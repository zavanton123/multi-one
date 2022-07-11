package ru.zavanton.mylibrary

import javax.inject.Qualifier
import javax.inject.Scope


@Scope
@Retention
annotation class PerApplication

@Scope
@Retention
annotation class PerFeature

@Scope
@Retention
annotation class PerScreen

const val ACCOUNTS_SCREEN = "https://zavanton.ru/accounts"
const val TRANSACTIONS_SCREEN = "https://zavanton.ru/transactions"

@Qualifier
@Retention
annotation class AppContext