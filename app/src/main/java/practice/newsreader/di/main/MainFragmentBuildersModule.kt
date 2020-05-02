package practice.newsreader.di.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import practice.newsreader.di.details.DetailsActivityModule
import practice.newsreader.di.details.DetailsModule
import practice.newsreader.di.details.DetailsScope
import practice.newsreader.di.news.NewsModule
import practice.newsreader.di.news.NewsScope
import practice.newsreader.di.news.NewsViewModelsModule
import practice.newsreader.di.pager.PagerModule
import practice.newsreader.di.pager.ViewPagerScope
import practice.newsreader.ui.details.DetailsActivity
import practice.newsreader.ui.details.DetailsFragment
import practice.newsreader.ui.news.NewsFragment
import practice.newsreader.ui.news.PagerFragment

@Module
abstract class MainFragmentBuildersModule {

    @NewsScope
    @ContributesAndroidInjector(modules = [NewsViewModelsModule::class, NewsModule::class])
    abstract fun contributeNewsFragment(): NewsFragment

    @DetailsScope
    @ContributesAndroidInjector(modules = [DetailsModule::class])
    abstract fun contributeDetailsFragment(): DetailsFragment



    @ViewPagerScope
    @ContributesAndroidInjector(modules = [PagerModule::class])
    abstract fun contributePagerFragment(): PagerFragment
}