package practice.newsreader.api

import io.reactivex.Flowable
import practice.newsreader.data.model.NewsResponse
import retrofit2.http.GET


interface ApiService {

    @GET("top-headlines?country=us")
    fun getArticles(): Flowable<NewsResponse>

}