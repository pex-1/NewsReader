package practice.newsreader.di.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import practice.newsreader.di.details.DetailsModule
import practice.newsreader.di.details.DetailsScope
import practice.newsreader.di.news.NewsDbModule
import practice.newsreader.di.news.NewsModule
import practice.newsreader.di.news.NewsScope
import practice.newsreader.di.viewmodel.ViewModelsModule
import practice.newsreader.di.pager.PagerModule
import practice.newsreader.di.pager.ViewPagerScope
import practice.newsreader.ui.details.DetailsFragment
import practice.newsreader.ui.news.NewsDbFragment
import practice.newsreader.ui.news.NewsFragment
import practice.newsreader.ui.news.PagerFragment

@Module
abstract class MainFragmentBuildersModule {

    @NewsScope
    @ContributesAndroidInjector(modules = [NewsModule::class])
    abstract fun contributeNewsFragment(): NewsFragment

    @NewsScope
    @ContributesAndroidInjector(modules = [NewsDbModule::class])
    abstract fun contributeNewsDbFragment(): NewsDbFragment

    @DetailsScope
    @ContributesAndroidInjector(modules = [DetailsModule::class])
    abstract fun contributeDetailsFragment(): DetailsFragment

    @ViewPagerScope
    @ContributesAndroidInjector(modules = [PagerModule::class])
    abstract fun contributePagerFragment(): PagerFragment
}