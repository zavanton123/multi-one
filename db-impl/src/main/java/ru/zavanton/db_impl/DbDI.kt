package ru.zavanton.db_impl

import android.content.Context
import androidx.room.Room
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.zavanton.db_api.DbApi
import ru.zavanton.db_api.IAppDatabase
import ru.zavanton.db_api.IAppDatabaseDao
import ru.zavanton.mylibrary.AppContext
import ru.zavanton.mylibrary.ApplicationApi
import ru.zavanton.mylibrary.PerApplication
import ru.zavanton.mylibrary.UtilsComponentInjector

object DbComponentInjector {

    private var dbComponent: DbComponent? = null

    fun fetchDbComponent(): DbComponent {
        val applicationApi = UtilsComponentInjector.utilsComponent

        return dbComponent ?: DaggerDbComponent
            .builder()
            .applicationApi(applicationApi)
            .build()
            .apply {
                dbComponent = this
            }
    }

    fun clear() {
        dbComponent = null
    }
}

@PerApplication
@Component(
    modules = [
        DbModule::class,
    ],
    dependencies = [
        ApplicationApi::class,
    ]
)
interface DbComponent : DbApi {

}

@Module
class DbModule {

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
