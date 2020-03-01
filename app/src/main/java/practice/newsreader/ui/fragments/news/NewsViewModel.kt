package practice.newsreader.ui.fragments.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import practice.newsreader.data.model.Article
import practice.newsreader.data.repository.Repository
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    private lateinit var disposable: Disposable


    private val _news: MutableLiveData<List<Article>> = MutableLiveData()

    val news: LiveData<List<Article>>
    get() = _news

    init {
        getArticles()
    }

    private fun getArticles(){
        disposable = repository.getArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                {
                    _news.value = it.articles
                    onRetrievePostListSuccess()
                },
                {
                    onRetrievePostListError()
                }
            )
    }


    private fun onRetrievePostListFinish() {
        Log.e("RX JAVA", "finished")
    }

    private fun onRetrievePostListError() {
        Log.e("RX JAVA", "error")
    }

    private fun onRetrievePostListSuccess() {
        Log.e("RX JAVA", "loaded")
    }

    private fun onRetrievePostListStart() {
        Log.e("RX JAVA", "started")

    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}