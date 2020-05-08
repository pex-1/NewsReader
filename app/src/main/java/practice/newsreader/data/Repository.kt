package practice.newsreader.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import practice.newsreader.data.database.NewsDao
import practice.newsreader.data.model.Article
import javax.inject.Inject

class Repository @Inject constructor(private val newsDao: NewsDao) {

    fun getNews() = newsDao.getNews()

    fun insertArticle(article: Article) =  newsDao.insert(article)

    fun deleteArticle(article: Article) = newsDao.delete(article)

    fun articleExists(key: String): Observable<List<String>> {
        return newsDao.articleExists(key)
    }

}