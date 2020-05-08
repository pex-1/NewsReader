package practice.newsreader.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import practice.newsreader.data.model.Article

@Database(entities = [Article::class], version = 3, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}