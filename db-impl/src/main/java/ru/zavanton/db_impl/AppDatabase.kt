package ru.zavanton.db_impl

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.zavanton.db_api.GithubRepoEntity
import ru.zavanton.db_api.IAppDatabase
import ru.zavanton.db_api.IAppDatabaseDao

@Database(
    entities = [
        GithubRepoEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase(), IAppDatabase {

    override fun appDatabaseDao(): IAppDatabaseDao {
        return addDatabaseDaoImpl()
    }

    abstract fun addDatabaseDaoImpl(): AppDatabaseDao
}
