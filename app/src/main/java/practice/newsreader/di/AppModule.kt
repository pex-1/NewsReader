package practice.newsreader.di

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import practice.newsreader.R
import practice.newsreader.data.Repository
import practice.newsreader.data.database.NewsDao
import practice.newsreader.data.model.NewsResponse
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRepository(newsDao: NewsDao): Repository = Repository(newsDao)

    @Singleton
    @Provides
    fun provideAppDrawable(application: Application): Drawable? {
        return ContextCompat.getDrawable(application, R.drawable.placeholder)
    }

    @Singleton
    @Provides
    @Named("article_placeholder")
    fun providePlaceholder(application: Application): Drawable? {
        return ContextCompat.getDrawable(application, R.drawable.placeholder)
    }

    @Singleton
    @Provides
    fun provideNewsResponse(): NewsResponse{
        return NewsResponse()
    }

}