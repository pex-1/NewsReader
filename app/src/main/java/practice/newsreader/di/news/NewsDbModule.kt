package practice.newsreader.di.news

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import practice.newsreader.api.ApiService
import practice.newsreader.ui.news.NewsAdapter
import practice.newsreader.ui.news.NewsDbFragment
import practice.newsreader.ui.news.paging.NewsDataSource
import practice.newsreader.ui.news.paging.NewsDataSourceFactory
import practice.newsreader.ui.news.NewsFragment

@Module
class NewsDbModule {


    @NewsScope
    @Provides
    fun provideAdapter(footerClickListener: NewsAdapter.OnFooterClicked, clickListener: NewsAdapter.OnArticleClicked, glide: RequestManager): NewsAdapter {
        return NewsAdapter(footerClickListener, clickListener, glide)
    }

    @NewsScope
    @Provides
    fun provideOnFooterClickListener(newsFragment: NewsDbFragment): NewsAdapter.OnFooterClicked {
        return newsFragment
    }

    @NewsScope
    @Provides
    fun provideOnArticleClickListener(newsFragment: NewsDbFragment): NewsAdapter.OnArticleClicked {
        return newsFragment
    }

    @NewsScope
    @Provides
    fun provideGlide(newsFragment: NewsDbFragment): RequestManager {
        return Glide.with(newsFragment)
    }


    @NewsScope
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }


}