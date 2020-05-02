package practice.newsreader.ui.news

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import practice.newsreader.ui.MainActivity
import javax.inject.Inject

class PagerAdapter @Inject constructor(fragmentActivity: MainActivity) : FragmentStateAdapter(fragmentActivity) {

    private var fragments = HashMap<Int, NewsFragment>()

    override fun createFragment(position: Int): Fragment {
        val fragment = NewsFragment.newInstance(position)
        fragments[position] = fragment
        return fragment
    }

    fun getItem(id: Int): NewsFragment? {
        return fragments[id]
    }

    override fun getItemCount(): Int = 4

}
