package ru.zavanton.db_api

interface IAppDatabaseDao {

    suspend fun insert(repos: List<GithubRepoEntity>)

    suspend fun delete()
}