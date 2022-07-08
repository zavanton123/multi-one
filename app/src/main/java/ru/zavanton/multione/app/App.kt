package ru.zavanton.multione.app

import android.app.Application
import android.util.Log
import ru.zavanton.network_api.NetworkApi
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.network_impl.NetworkComponentInjector

class App : Application(), NetworkApiProvider {

    override fun onCreate() {
        super.onCreate()

        Log.d("zavanton", "zavanton - init")
    }

    override fun provide(): NetworkApi {
        return NetworkComponentInjector.getNetworkComponent()
    }
}
