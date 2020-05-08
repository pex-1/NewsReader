package practice.newsreader.ui.news

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import practice.newsreader.ui.MainActivity
import practice.newsreader.ui.news.shared.NewsReaderFragment
import javax.inject.Inject

class PagerAdapter @Inject constructor(fragmentActivity: MainActivity) : FragmentStateAdapter(fragmentActivity) {

    private var fragments = HashMap<Int, NewsReaderFragment>()

    override fun createFragment(position: Int): Fragment {
        if(position == 3){
            return setFragment(NewsDbFragment.newInstance(position), position)
        }
        return setFragment(NewsFragment.newInstance(position), position)
    }

    fun getItem(id: Int): NewsReaderFragment? {
        return fragments[id]
    }

    override fun getItemCount(): Int = 4

    private fun setFragment(fragment: NewsReaderFragment, position: Int) : NewsReaderFragment{
        fragments[position] = fragment
        return fragment
    }

}
