package ru.zavanton.scanner_impl.di

import android.app.Application
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.network_api.NetworkApi
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.scanner_api.ScannerApi
import ru.zavanton.scanner_impl.data.ScannerNetworkService
import ru.zavanton.scanner_impl.ui.ScannerFragment

object ScannerComponentInjector {

    private var scannerComponent: ScannerComponent? = null

    fun getScannerComponent(app: Application): ScannerComponent {
        val networkApiProvider = app as? NetworkApiProvider ?: throw Exception("Terrible")
        return DaggerScannerComponent
            .builder()
            .networkApi(networkApiProvider.provide())
            .build()
            .apply {
                scannerComponent = this
            }
    }

    fun clear() {
        scannerComponent = null
    }
}

@PerFeature
@Component(
    modules = [
        ScannerNetworkServiceModule::class,
    ],
    dependencies = [
        NetworkApi::class,
    ]
)
interface ScannerComponent : ScannerApi {

    fun inject(scannerFragment: ScannerFragment)
}

@Module
class ScannerNetworkServiceModule {

    @PerFeature
    @Provides
    fun provideService(retrofit: Retrofit): ScannerNetworkService {
        return retrofit.create(ScannerNetworkService::class.java)
    }
}
