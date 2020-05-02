package practice.newsreader.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import practice.newsreader.di.details.DetailsActivityModule
import practice.newsreader.di.details.DetailsScope
import practice.newsreader.di.main.MainFragmentBuildersModule
import practice.newsreader.ui.MainActivity
import practice.newsreader.ui.details.DetailsActivity

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @DetailsScope
    @ContributesAndroidInjector(modules = [DetailsActivityModule::class])
    abstract fun provideDetailsActivity(): DetailsActivity

}
