package practice.newsreader.api

import io.reactivex.Single
import practice.newsreader.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET
    fun getArticles(@Url endpoint: String, @Query("q") search: String?, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Single<NewsResponse>

}