package practice.newsreader.di.news

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import practice.newsreader.di.ViewModelKey
import practice.newsreader.ui.fragments.news.NewsViewModel

@Module
abstract class NewsViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel

}