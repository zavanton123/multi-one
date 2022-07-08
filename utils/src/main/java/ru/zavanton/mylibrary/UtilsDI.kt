package ru.zavanton.mylibrary

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Qualifier


@Qualifier
@Retention
annotation class AppContext

interface ApplicationApi {

    fun application(): Application

    @AppContext
    fun appContext(): Context
}

object UtilsComponentInjector {

    lateinit var utilsComponent: UtilsComponent

    fun init(application: Application) {
        utilsComponent = DaggerUtilsComponent
            .factory()
            .create(
                application = application,
                appContext = application
            )
    }
}

@PerApplication
@Component
interface UtilsComponent : ApplicationApi {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
            @BindsInstance @AppContext appContext: Context,
        ): UtilsComponent
    }
}
