package practice.newsreader.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import practice.newsreader.data.database.NewsDao
import practice.newsreader.data.model.Article
import practice.newsreader.data.model.NetworkResponse
import javax.inject.Inject

class NewsDbViewModel @Inject constructor(private val config: PagedList.Config, private val newsDao: NewsDao) : ViewModel() {

    private val _response: MutableLiveData<NetworkResponse> = MutableLiveData()
    val response: LiveData<NetworkResponse>
        get() = _response

    private var compositeDisposable = CompositeDisposable()
    private var newsDataSourceFactory: DataSource.Factory<Int, Article> = newsDao.getNews()
    private var articles : Observable<PagedList<Article>>

    private val _articleList: MutableLiveData<PagedList<Article>> = MutableLiveData()
    val articleList: LiveData<PagedList<Article>>
        get() = _articleList

    init {
        articles = RxPagedListBuilder(newsDataSourceFactory, config).buildObservable()
    }

    fun resetSearch(){
        newsDataSourceFactory = newsDao.getNews()
        articles = RxPagedListBuilder(newsDataSourceFactory, config).buildObservable()
        getArticles()
    }

    fun searchNews(query: String){
        newsDataSourceFactory = newsDao.getNews(query)
        articles = RxPagedListBuilder(newsDataSourceFactory, config).buildObservable()
        getArticles()
    }

    fun getArticles(){
        compositeDisposable.add(articles.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _response.value = NetworkResponse.start() }
                .subscribe({
                    _response.value = NetworkResponse.success()
                    _articleList.value = it
                }, {
                    _response.value = NetworkResponse.error(it.message)
                }))
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}