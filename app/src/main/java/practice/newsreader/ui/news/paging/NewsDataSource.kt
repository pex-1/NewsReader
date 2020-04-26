package practice.newsreader.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import practice.newsreader.api.ApiService
import practice.newsreader.data.model.Article
import practice.newsreader.data.model.NetworkResponse
import practice.newsreader.util.Constants
import practice.newsreader.util.EndpointsUtil
import javax.inject.Inject

class NewsDataSource @Inject constructor(private val apiService: ApiService, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Article>() {

    private val _response: MutableLiveData<NetworkResponse> = MutableLiveData()
    fun getResponse(): LiveData<NetworkResponse> = _response

    private var retryCompletable: Completable? = null

    private var articleSize = 0

    private var lastRequest = Constants.PAGE_SIZE + 1

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Article>) {
        compositeDisposable.add(apiService.getArticles(EndpointsUtil.ENDPOINTS[EndpointsUtil.position], Constants.search, 1, params.requestedLoadSize)
                .doOnSubscribe {
                    updateState(NetworkResponse.start())
                }
                .subscribe({
                    updateState(NetworkResponse.success())
                    callback.onResult(it.articles, null, 2)
                    articleSize = it.articles.size
                }, {
                    updateState(NetworkResponse.error(it.message))
                    setRetry(Action {
                        loadInitial(params, callback)
                    })
                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        if (articleSize < Constants.INITIAL_PAGE_SIZE || lastRequest < Constants.PAGE_SIZE) return
        compositeDisposable.add(apiService.getArticles(EndpointsUtil.ENDPOINTS[EndpointsUtil.position], Constants.search, params.key, params.requestedLoadSize)
                .doOnSubscribe {
                    updateState(NetworkResponse.start())
                }
                .subscribe({
                    updateState(NetworkResponse.success())
                    callback.onResult(it.articles, params.key + 1)
                    lastRequest = it.articles.size
                }, {
                    updateState(NetworkResponse.error(it.message))
                    setRetry(Action { loadAfter(params, callback) })
                }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())
        }

    }

    private fun updateState(response: NetworkResponse) {
        this._response.postValue(response)
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}