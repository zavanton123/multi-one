package ru.zavanton.mylibrary

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
