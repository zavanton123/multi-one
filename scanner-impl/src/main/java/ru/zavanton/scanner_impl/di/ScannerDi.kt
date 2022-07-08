package ru.zavanton.scanner_impl.di

import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.zavanton.db_api.DbApi
import ru.zavanton.db_api.DbApiProvider
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.network_api.NetworkApi
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.scanner_api.ScannerApi
import ru.zavanton.scanner_impl.data.ScannerNetworkService
import ru.zavanton.scanner_impl.ui.ScannerFragment

object ScannerComponentInjector {

    private var scannerComponent: ScannerComponent? = null

    fun getScannerComponent(): ScannerComponent {
        // Get the dependencies from the App
        val application = UtilsComponentInjector.utilsComponent.application()
        val networkApiProvider = application as? NetworkApiProvider
            ?: throw Exception("Must provide NetworkApi")
        val dbApiProvider = application as? DbApiProvider
            ?: throw Exception("Must provide DbApi")

        return DaggerScannerComponent
            .builder()
            .networkApi(networkApiProvider.provideNetworkApi())
            .dbApi(dbApiProvider.provideDbApi())
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
        DbApi::class,
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
