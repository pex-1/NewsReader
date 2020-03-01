package practice.newsreader.di.news

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import practice.newsreader.ui.fragments.news.NewsAdapter
import practice.newsreader.ui.fragments.news.NewsFragment

@Module
class NewsModule {

    @NewsScope
    @Provides
    fun provideAdapter(clickListener: NewsAdapter.OnArticleClicked, picasso: Picasso):NewsAdapter{
        return NewsAdapter(clickListener, picasso)
    }

    @NewsScope
    @Provides
    fun provideOnClickListener(newsFragment: NewsFragment):NewsAdapter.OnArticleClicked{
        return newsFragment
    }

}