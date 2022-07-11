package ru.zavanton.scanner_impl.di

import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.zavanton.db_api.DatabaseOutApi
import ru.zavanton.db_api.IAppDatabaseDao
import ru.zavanton.mylibrary.PerFeature
import ru.zavanton.network_api.NetworkOutApi
import ru.zavanton.scanner_api.ScannerOutApi
import ru.zavanton.scanner_impl.data.ScannerNetworkService
import ru.zavanton.scanner_impl.ui.ScannerFragment
import java.lang.ref.WeakReference

object ScannerComponentHolder {

    private var scannerComponentWeakRef: WeakReference<ScannerComponent>? = null
    // initialize in App
    lateinit var scannerInApiFactory: () -> ScannerInApi
    private val scannerComponentFactory: (ScannerInApi) -> ScannerComponent = { scannerInApi ->
        DaggerScannerComponent
            .factory()
            .create(scannerInApi)
    }

    fun getScannerOutApi(): ScannerOutApi {
        return getScannerComponent()
    }

    fun getScannerComponent(): ScannerComponent {
        return scannerComponentWeakRef?.get()
            ?: scannerComponentFactory(scannerInApiFactory()).apply {
                scannerComponentWeakRef = WeakReference(this)
            }
    }
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
        DatabaseOutApi::class,
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

interface ScannerInApi {

    fun retrofit(): Retrofit

    fun appDatabaseDao(): IAppDatabaseDao
}