package practice.newsreader.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import practice.newsreader.di.main.MainFragmentBuildersModule
import practice.newsreader.ui.MainActivity

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

}
