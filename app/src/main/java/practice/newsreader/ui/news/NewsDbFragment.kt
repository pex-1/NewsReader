package practice.newsreader.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_news.*
import practice.newsreader.R
import practice.newsreader.data.model.Article
import practice.newsreader.data.model.NetworkResponse
import practice.newsreader.ui.news.shared.NewsReaderFragment
import practice.newsreader.ui.viewmodels.ViewModelProviderFactory
import practice.newsreader.util.*
import javax.inject.Inject


class NewsDbFragment @Inject constructor() : NewsReaderFragment(), NewsAdapter.OnArticleClicked, NewsAdapter.OnFooterClicked {

    private var navController: NavController? = null

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: NewsDbViewModel

    private var swipeLoading = false


    companion object {
        val TAB_NUMBER = "tab_number"

        fun newInstance(tabNumber: Int): NewsDbFragment {
            val fragment = NewsDbFragment()
            val bundle = Bundle()
            bundle.putInt(TAB_NUMBER, tabNumber)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)

        if (arguments != null) {
            EndpointsUtil.position = arguments!!.getInt(TAB_NUMBER)
        }

        viewModel = ViewModelProvider(this, providerFactory).get(NewsDbViewModel::class.java)

        subscribeObservers()

        initSearch()

        resetSearch()

        val itemDecoration = VerticalSpacingItemDecoration(15)
        newsRecyclerView.addItemDecoration(itemDecoration)


    }

    private fun resetSearch() {
        resetSearchImageView.setOnClickListener {
            EndpointsUtil.searchQuery[getPosition()] = null
            searchQueryLinearLayout.hide()
            viewModel.resetSearch()
        }
    }

    override fun setSearchQuery(query: String?) {
        if (query != null) {
            EndpointsUtil.searchQuery[getPosition()] = query
            initSearch()
        }
    }

    private fun initSearch() {
        if (EndpointsUtil.searchQuery[getPosition()] != null) {
            searchQueryLinearLayout.show()
            searchQueryTextView.text = EndpointsUtil.searchQuery[getPosition()]
            viewModel.searchNews(EndpointsUtil.searchQuery[getPosition()]!!)
        }
        viewModel.getArticles()
    }

    private fun getPosition() = arguments?.getInt(TAB_NUMBER) ?: 0

    private fun subscribeObservers() {
        viewModel.articleList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setUi(it)
            }
        })
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if(it != null){
                when(it.status){
                    NetworkResponse.Status.LOADING -> newsLoadingProgressBar.show()
                    NetworkResponse.Status.SUCCESS -> newsLoadingProgressBar.hide()
                    NetworkResponse.Status.ERROR -> {
                        newsLoadingProgressBar.hide()
                        view?.snackbar(it.message.toString())
                    }
                }
            }
        })
    }

    private fun setUi(it: PagedList<Article>) {
        newsRecyclerView.apply {

            newsAdapter.submitList(it)
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }


    override fun onClick(article: Article?) {
        val direction = PagerFragmentDirections.actionPagerToDetails(article)
        navController?.navigate(direction)
    }

    override fun onClick() {

    }


}