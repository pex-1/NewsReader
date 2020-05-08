package practice.newsreader.ui.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import practice.newsreader.api.ApiService
import practice.newsreader.data.Repository
import practice.newsreader.data.database.NewsDao
import practice.newsreader.data.model.Article
import practice.newsreader.data.model.NetworkResponse
import practice.newsreader.ui.news.paging.NewsDataSource
import practice.newsreader.ui.news.paging.NewsDataSourceFactory
import practice.newsreader.util.EndpointsUtil
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val apiService: ApiService, private val config: PagedList.Config, private val newsDao: NewsDao) : ViewModel() {


    private var compositeDisposable = CompositeDisposable()
    private var newsDataSourceFactory: NewsDataSourceFactory

    private var articleList: LiveData<PagedList<Article>>

    fun getArticleList() = articleList

    fun getResponse(): LiveData<NetworkResponse> = Transformations.switchMap(newsDataSourceFactory.newsDataSourceLiveData, NewsDataSource::getResponse)

    fun refresh() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.invalidate()
    }

    init {
        newsDataSourceFactory = NewsDataSourceFactory(apiService, compositeDisposable)
        articleList = LivePagedListBuilder(newsDataSourceFactory, config).build()
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