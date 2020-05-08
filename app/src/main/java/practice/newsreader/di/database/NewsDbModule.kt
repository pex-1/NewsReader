package practice.newsreader.di.database

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import practice.newsreader.data.database.NewsDao
import practice.newsreader.data.database.NewsDatabase
import javax.inject.Singleton

@Module
class NewsDbModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(application: Application): NewsDatabase {
        return Room.databaseBuilder(application, NewsDatabase::class.java, "news_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(database: NewsDatabase): NewsDao = database.newsDao()
}