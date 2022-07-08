package ru.zavanton.db_impl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.zavanton.db_api.GithubRepoEntity
import ru.zavanton.db_api.IAppDatabaseDao

@Dao
interface AppDatabaseDao : IAppDatabaseDao {

    @Insert
    override suspend fun insert(repos: List<GithubRepoEntity>)

    @Query("delete from repository")
    override suspend fun delete()
}