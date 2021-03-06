package practice.newsreader.ui.news.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import practice.newsreader.api.ApiService
import practice.newsreader.data.database.NewsDao
import practice.newsreader.data.model.Article
import practice.newsreader.ui.news.paging.NewsDataSource
import javax.inject.Inject

class NewsDataSourceFactory @Inject constructor(private val apiService: ApiService, private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, Article>() {

    val newsDataSourceLiveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, Article> {
        val newsDataSource = NewsDataSource(apiService, compositeDisposable)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }

}