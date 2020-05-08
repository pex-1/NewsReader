package practice.newsreader.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import practice.newsreader.ui.viewmodels.ViewModelProviderFactory

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory?): ViewModelProvider.Factory?
}