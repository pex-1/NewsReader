package practice.newsreader.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import practice.newsreader.data.Repository
import practice.newsreader.data.model.Article
import practice.newsreader.data.model.DatabaseResponse
import javax.inject.Inject

const val INSERT_MSG = "Article stored!"
const val DELETE_MSG = "Article removed!"

class DetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _favorite: MutableLiveData<Boolean> = MutableLiveData()
    val favorite: LiveData<Boolean>
        get() = _favorite

    private val _response: MutableLiveData<DatabaseResponse> = MutableLiveData()
    val response: LiveData<DatabaseResponse>
        get() = _response

    fun insertArticle(article: Article) {
        disposable.add(repository.insertArticle(article)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _response.value = DatabaseResponse(DatabaseResponse.Status.INSERT, INSERT_MSG)
                }, {
                    _response.value = DatabaseResponse(DatabaseResponse.Status.ERROR, it.message)
                }))
    }

    fun articleExists(key: String) {
        disposable.add(repository.articleExists(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _favorite.value = it.isNotEmpty()
                }, {
                    _favorite.value = false
                }))
    }

    fun removeArticle(article: Article){
        disposable.add(repository.deleteArticle(article)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _response.value = DatabaseResponse(DatabaseResponse.Status.DELETE, DELETE_MSG)
            }, {
                _response.value = DatabaseResponse(DatabaseResponse.Status.ERROR, it.message)
            }))
    }


    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}