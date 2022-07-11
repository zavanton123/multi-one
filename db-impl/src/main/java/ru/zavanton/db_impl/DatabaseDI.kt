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
import java.lang.ref.WeakReference


interface DatabaseInApi {

    @AppContext
    fun appContext(): Context
}

object DatabaseComponentHolder {

    private var databaseComponentWeakRef: WeakReference<DatabaseComponent>? = null
    lateinit var databaseInApiFactory: () -> DatabaseInApi
    private val databaseComponentFactory: (DatabaseInApi) -> DatabaseComponent = { databaseInApi ->
        DaggerDatabaseComponent
            .builder()
            .databaseInApi(databaseInApi)
            .build()
    }

    fun getDatabaseOutApi(): DatabaseOutApi {
        return databaseComponentWeakRef?.get()
            ?: databaseComponentFactory(databaseInApiFactory()).apply {
                databaseComponentWeakRef = WeakReference(this)
            }
    }
}

@PerApplication
@Component(
    modules = [
        DatabaseModule::class,
    ],
    dependencies = [
        DatabaseInApi::class,
    ],
)
interface DatabaseComponent : DatabaseOutApi

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
