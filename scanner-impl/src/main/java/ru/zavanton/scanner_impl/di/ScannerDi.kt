package ru.zavanton.scanner_impl.di

import android.app.Application
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
    private var scannerDependencies: ScannerDependencies? = null

    fun getScannerComponent(): ScannerComponent {
        return scannerComponent ?: DaggerScannerComponent
            .factory()
            .create(getScannerDependencies())
            .apply { scannerComponent = this }
    }

    fun clear() {
        scannerComponent = null
    }

    private fun getScannerDependencies(): ScannerDependencies {
        return scannerDependencies
            ?: UtilsComponentInjector.utilsComponent.application()
                .let { application ->
                    DaggerScannerDependenciesComponent.builder()
                        .networkApi(getNetworkApi(application))
                        .dbApi(getDbApi(application))
                        .build()
                        .apply { scannerDependencies = this }
                }
    }

    // Get the dependencies from the App
    private fun getDbApi(application: Application): DbApi {
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
    modules = [
        ScannerNetworkServiceModule::class,
    ],
    dependencies = [
        ScannerDependencies::class,
    ]
)
interface ScannerComponent : ScannerApi {

    @Component.Factory
    interface Factory {

        fun create(scannerDependencies: ScannerDependencies): ScannerComponent
    }

    fun inject(scannerFragment: ScannerFragment)
}

@PerFeature
@Component(
    dependencies = [
        NetworkApi::class,
        DbApi::class,
    ]
)
interface ScannerDependenciesComponent : ScannerDependencies

@Module
class ScannerNetworkServiceModule {

    @PerFeature
    @Provides
    fun provideService(retrofit: Retrofit): ScannerNetworkService {
        return retrofit.create(ScannerNetworkService::class.java)
    }
}
