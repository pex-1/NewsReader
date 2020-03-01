package practice.newsreader.data.repository

import io.reactivex.Flowable
import practice.newsreader.api.ApiService
import practice.newsreader.data.model.NewsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val apiService: ApiService){

    fun getArticles(): Flowable<NewsResponse> {
        return apiService.getArticles()
    }


}