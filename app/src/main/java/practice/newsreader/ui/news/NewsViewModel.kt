package practice.newsreader.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import practice.newsreader.api.ApiService
import practice.newsreader.data.model.Article
import practice.newsreader.data.model.NetworkResponse
import practice.newsreader.ui.news.paging.NewsDataSource
import practice.newsreader.ui.news.paging.NewsDataSourceFactory
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val apiService: ApiService, private val config: PagedList.Config) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var newsDataSourceFactory: NewsDataSourceFactory

    private var articleList: LiveData<PagedList<Article>>

    fun getArticleList() = articleList

    fun getResponse(): LiveData<NetworkResponse> = Transformations.switchMap<NewsDataSource, NetworkResponse>(newsDataSourceFactory.newsDataSourceLiveData, NewsDataSource::getResponse)

    fun refresh() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.invalidate()
    }

    init {
        newsDataSourceFactory = NewsDataSourceFactory(apiService, compositeDisposable)
        articleList = LivePagedListBuilder<Int, Article>(newsDataSourceFactory, config).build()
    }


    fun retry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return articleList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}