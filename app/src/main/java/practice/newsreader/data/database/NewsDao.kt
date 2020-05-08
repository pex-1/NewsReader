package practice.newsreader.data.database

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import practice.newsreader.data.model.Article

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table ORDER BY publishedAt ASC")
    fun getNews(): DataSource.Factory<Int, Article>

    @Query("SELECT * FROM news_table WHERE content LIKE '%' || :searchQuery  || '%' OR title LIKE '%' || :searchQuery  || '%'")
    fun getNews(searchQuery: String): DataSource.Factory<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article) : Completable

    @Delete
    fun delete(article: Article) : Completable

    @Query("SELECT publishedAt FROM news_table WHERE publishedAt LIKE :primaryKey")
    fun articleExists(primaryKey: String) : Observable<List<String>>


}