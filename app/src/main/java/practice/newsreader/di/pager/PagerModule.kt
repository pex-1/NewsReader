package practice.newsreader.di.pager

import dagger.Module
import dagger.Provides
import practice.newsreader.ui.MainActivity
import practice.newsreader.ui.news.PagerAdapter

@Module
class PagerModule {

    @ViewPagerScope
    @Provides
    fun providePagerAdapter(fragmentActivity: MainActivity): PagerAdapter {
        return PagerAdapter(fragmentActivity)
    }

}