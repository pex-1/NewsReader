package practice.newsreader.di.viewmodel

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import practice.newsreader.di.viewmodel.ViewModelKey
import practice.newsreader.ui.details.DetailsViewModel
import practice.newsreader.ui.news.NewsDbViewModel
import practice.newsreader.ui.news.NewsViewModel

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsDbViewModel::class)
    abstract fun bindNewsDbViewModel(viewModel: NewsDbViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(viewModel: DetailsViewModel): ViewModel

}