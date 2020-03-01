package practice.newsreader.di

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import practice.newsreader.R
import practice.newsreader.api.ApiService
import practice.newsreader.data.model.NewsResponse
import practice.newsreader.data.repository.Repository
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAppDrawable(application: Application?): Drawable? {
        return ContextCompat.getDrawable(application!!, R.drawable.placeholder)
    }

    @Singleton
    @Provides
    fun providePicasso():Picasso{
        return Picasso.get()
    }

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService): Repository {
        return Repository(apiService)
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