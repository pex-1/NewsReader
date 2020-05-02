package practice.newsreader.di.news

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import practice.newsreader.api.ApiService
import practice.newsreader.ui.news.NewsAdapter
import practice.newsreader.ui.news.paging.NewsDataSource
import practice.newsreader.ui.news.paging.NewsDataSourceFactory
import practice.newsreader.ui.news.NewsFragment

@Module
class NewsModule {


    @NewsScope
    @Provides
    fun provideAdapter(footerClickListener: NewsAdapter.OnFooterClicked, clickListener: NewsAdapter.OnArticleClicked, glide: RequestManager): NewsAdapter {
        return NewsAdapter(footerClickListener, clickListener, glide)
    }

    @NewsScope
    @Provides
    fun provideOnFooterClickListener(newsFragment: NewsFragment): NewsAdapter.OnFooterClicked {
        return newsFragment
    }

    @NewsScope
    @Provides
    fun provideGlide(newsFragment: NewsFragment): RequestManager {
        return Glide.with(newsFragment)
    }

    @NewsScope
    @Provides
    fun provideOnArticleClickListener(newsFragment: NewsFragment): NewsAdapter.OnArticleClicked {
        return newsFragment
    }


    @Provides
    fun provideNewsDataSource(apiService: ApiService, compositeDisposable: CompositeDisposable): NewsDataSource {
        return NewsDataSource(
            apiService,
            compositeDisposable
        )
    }


    @Provides
    fun provideNewsDataSourceFactory(apiService: ApiService, compositeDisposable: CompositeDisposable): NewsDataSourceFactory {
        return NewsDataSourceFactory(
            apiService,
            compositeDisposable
        )
    }

    @NewsScope
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @NewsScope
    @Provides
    fun provideCompletable(): Completable? {
        return Completable.complete()
    }


}