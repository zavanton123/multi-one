package ru.zavanton.db_impl

import android.content.Context
import androidx.room.Room
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.zavanton.db_api.DatabaseOutApi
import ru.zavanton.db_api.IAppDatabase
import ru.zavanton.db_api.IAppDatabaseDao
import ru.zavanton.mylibrary.AppContext
import ru.zavanton.mylibrary.PerApplication

object DatabaseComponentInjector {

    private var databaseComponent: DatabaseComponent? = null

    fun fetchDbComponent(context: Context): DatabaseComponent {
        val databaseInApi = object : DatabaseInApi {
            override fun appContext(): Context {
                return context
            }
        }

        return databaseComponent ?: DaggerDatabaseComponent
            .builder()
            .databaseInApi(databaseInApi)
            .build()
            .apply {
                databaseComponent = this
            }
    }

    fun clear() {
        databaseComponent = null
    }
}

@PerApplication
@Component(
    modules = [
        DatabaseModule::class,
    ],
    dependencies = [
        DatabaseInApi::class,
    ]
)
interface DatabaseComponent : DatabaseOutApi {

}

interface DatabaseInApi {

    @AppContext
    fun appContext(): Context
}

@Module
class DatabaseModule {

    @PerApplication
    @Provides
    fun provideAppDatabase(@AppContext appContext: Context): IAppDatabase {
        return Room
            .databaseBuilder(appContext, AppDatabase::class.java, "my-app.db")
            .build()
    }

    @PerApplication
    @Provides
    fun provideDao(appDatabase: IAppDatabase): IAppDatabaseDao {
        return appDatabase.appDatabaseDao()
    }
}
