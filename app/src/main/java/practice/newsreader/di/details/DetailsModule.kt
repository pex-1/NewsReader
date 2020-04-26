package practice.newsreader.di.details

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import practice.newsreader.ui.fragments.details.DetailsFragment

@Module
class DetailsModule {

    @DetailsScope
    @Provides
    fun provideGlide(detailsFragment: DetailsFragment): RequestManager {
        return Glide.with(detailsFragment)
    }


}