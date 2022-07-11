package ru.zavanton.network_impl

import android.util.Log
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.zavanton.mylibrary.PerApplication
import ru.zavanton.network_api.NetworkOutApi
import java.lang.ref.WeakReference

object NetworkComponentInjector {

    private var networkComponentWeakRef: WeakReference<NetworkComponent>? = null
    private val networkComponentFactory: () -> NetworkComponent = { DaggerNetworkComponent.create() }

    fun getNetworkOutApi(): NetworkOutApi {
        return networkComponentWeakRef?.get()
            ?: networkComponentFactory().apply {
                networkComponentWeakRef = WeakReference(this)
            }
    }
}

@PerApplication
@Component(
    modules = [
        NetworkModule::class,
    ],
)
interface NetworkComponent : NetworkOutApi {

}

@Module
class NetworkModule {

    @PerApplication
    @Provides
    fun provideInterceptor(): Interceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message: String ->
                Log.d("zavanton", "zavanton - okhttp: $message")
            }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }

    @PerApplication
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @PerApplication
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @PerApplication
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}
