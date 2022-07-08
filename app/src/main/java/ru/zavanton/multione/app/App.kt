package ru.zavanton.multione.app

import android.app.Application
import android.util.Log
import ru.zavanton.mylibrary.AppHolder
import ru.zavanton.network_api.NetworkApi
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.network_impl.NetworkComponentInjector

class App : Application(), NetworkApiProvider {

    override fun onCreate() {
        super.onCreate()

        // Make the application available to all the modules
        // (app is needed for their initialization)
        AppHolder.application = this
    }

    override fun provide(): NetworkApi {
        return NetworkComponentInjector.getNetworkComponent()
    }
}
