package practice.newsreader.di

import androidx.paging.PagedList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import practice.newsreader.BuildConfig
import practice.newsreader.api.ApiService
import practice.newsreader.util.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module class NetworkModule {


    @Singleton
    @Provides
    internal fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    internal fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    internal fun provideRxAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Singleton
    @Provides
    internal fun provideConfig() : PagedList.Config = PagedList.Config.Builder()
        .setPageSize(Constants.PAGE_SIZE)
        .setInitialLoadSizeHint(Constants.INITIAL_PAGE_SIZE)
        .build()

    @Singleton
    @Provides
    internal fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: GsonConverterFactory, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(converterFactory)
                .build()
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->

            val url = chain.request()
                    .url.newBuilder()
                    .addQueryParameter("apiKey", BuildConfig.NewsApiKey)
                    .build()
            val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

            return@Interceptor chain.proceed(request)
        }

        return OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()
    }

}