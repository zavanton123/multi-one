package ru.zavanton.scanner_impl.di

import android.app.Application
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.zavanton.db_api.DbApiProvider
import ru.zavanton.db_api.DbOutApi
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.mylibrary.UtilsComponentInjector
import ru.zavanton.network_api.NetworkApiProvider
import ru.zavanton.network_api.NetworkOutApi
import ru.zavanton.scanner_api.ScannerOutApi
import ru.zavanton.scanner_impl.data.ScannerNetworkService
import ru.zavanton.scanner_impl.ui.ScannerFragment

object ScannerComponentInjector {

    private var scannerComponent: ScannerComponent? = null
    private var scannerInApi: ScannerInApi? = null

    fun getScannerComponent(): ScannerComponent {
        return scannerComponent ?: DaggerScannerComponent
            .factory()
            .create(getScannerInApi())
            .apply { scannerComponent = this }
    }

    fun clear() {
        scannerComponent = null
    }

    private fun getScannerInApi(): ScannerInApi {
        return scannerInApi
            ?: UtilsComponentInjector.utilsComponent.application()
                .let { application ->
                    DaggerScannerInApiComponent.builder()
                        .networkOutApi(getNetworkApi(application))
                        .dbOutApi(getDbApi(application))
                        .build()
                        .apply { scannerInApi = this }
                }
    }

    // Get the dependencies from the App
    private fun getDbApi(application: Application): DbOutApi {
        return (application as? DbApiProvider ?: throw Exception("Must provide DbApi"))
            .provideDbApi()
    }

    // Get the dependencies from the App
    private fun getNetworkApi(application: Application) =
        (application as? NetworkApiProvider ?: throw Exception("Must provide NetworkApi"))
            .provideNetworkApi()
}

@PerFeature
@Component(
    dependencies = [ScannerInApi::class],
    modules = [
        ScannerNetworkServiceModule::class,
    ]
)
interface ScannerComponent : ScannerOutApi {

    @Component.Factory
    interface Factory {

        fun create(scannerInApi: ScannerInApi): ScannerComponent
    }

    fun inject(scannerFragment: ScannerFragment)
}

@PerFeature
@Component(
    dependencies = [
        NetworkOutApi::class,
        DbOutApi::class,
    ]
)
interface ScannerInApiComponent : ScannerInApi

@Module
class ScannerNetworkServiceModule {

    @PerFeature
    @Provides
    fun provideService(retrofit: Retrofit): ScannerNetworkService {
        return retrofit.create(ScannerNetworkService::class.java)
    }
}
