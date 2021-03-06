package practice.newsreader.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import practice.newsreader.BaseApplication
import practice.newsreader.di.database.NewsDbModule
import practice.newsreader.di.viewmodel.ViewModelFactoryModule
import practice.newsreader.di.viewmodel.ViewModelsModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        NetworkModule::class,
        AppModule::class,
        ViewModelFactoryModule::class,
        ActivityBuildersModule::class,
        NewsDbModule::class,
        ViewModelsModule::class
    ]
)

interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}