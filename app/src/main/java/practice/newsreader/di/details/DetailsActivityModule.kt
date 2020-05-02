package practice.newsreader.di.details

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import practice.newsreader.ui.details.DetailsActivity
import practice.newsreader.ui.details.DetailsFragment

@Module
class DetailsActivityModule {

    @DetailsScope
    @Provides
    fun provideGlide(detailsActivity: DetailsActivity): RequestManager {
        return Glide.with(detailsActivity)
    }

}