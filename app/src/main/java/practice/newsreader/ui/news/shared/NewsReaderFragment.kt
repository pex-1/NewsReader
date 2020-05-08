package practice.newsreader.ui.news.shared

import dagger.android.support.DaggerFragment

abstract class NewsReaderFragment : DaggerFragment() {

    abstract fun setSearchQuery(query: String?)
}