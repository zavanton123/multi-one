package ru.zavanton.multione.app

import android.app.Application
import ru.zavanton.db_api.DbApi
import ru.zavanton.db_api.DbApiProvider
import ru.zavanton.db_impl.DbComponentInjector
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.network_api.NetworkApi
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.network_impl.NetworkComponentInjector

class App : Application(), NetworkApiProvider, DbApiProvider {

    override fun onCreate() {
        super.onCreate()

        // Make the application available to all the modules
        // (app is needed for their initialization)
        UtilsComponentInjector.init(this)
    }

    override fun provideNetworkApi(): NetworkApi {
        return NetworkComponentInjector.getNetworkComponent()
    }

    override fun provideDbApi(): DbApi {
        return DbComponentInjector.fetchDbComponent()
    }
}
