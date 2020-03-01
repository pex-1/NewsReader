package practice.newsreader.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import practice.newsreader.di.main.MainFragmentBuildersModule
import practice.newsreader.ui.MainActivity

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class])  //this fragment can only be used within the scope of the main activity subcomponent
    abstract fun contributeMainActivity(): MainActivity?

}
