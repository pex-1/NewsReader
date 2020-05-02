package practice.newsreader.ui.news

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_news.*
import practice.newsreader.R
import practice.newsreader.data.model.Article
import practice.newsreader.data.model.NetworkResponse
import practice.newsreader.ui.viewmodels.ViewModelProviderFactory
import practice.newsreader.util.*
import javax.inject.Inject


class NewsFragment @Inject constructor() : DaggerFragment(), NewsAdapter.OnArticleClicked, NewsAdapter.OnFooterClicked {

    private var navController: NavController? = null

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: NewsViewModel

    private var swipeLoading = false




    companion object {
        val TAB_NUMBER = "tab_number"

        fun newInstance(tabNumber: Int): NewsFragment {
            val fragment = NewsFragment()
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

        viewModel = ViewModelProvider(this, providerFactory).get(NewsViewModel::class.java)

        subscribeObservers()

        initSwipeRefresh()

        initSearch()

        resetSearch()

        val itemDecoration = VerticalSpacingItemDecoration(15)
        newsRecyclerView.addItemDecoration(itemDecoration)



    }

    private fun resetSearch(){
        resetSearchImageView.setOnClickListener {
            EndpointsUtil.searchQuery[getPosition()] = null
            searchQueryLinearLayout.hide()
            viewModel.refresh()
        }
    }

    fun setSearchQuery(query: String?) {
        if (query != null) {
            EndpointsUtil.searchQuery[getPosition()] = query
            initSearch()
        }
    }

    private fun initSearch(){
        if(EndpointsUtil.searchQuery[getPosition()] != null){
            searchQueryLinearLayout.show()
            searchQueryTextView.text = EndpointsUtil.searchQuery[getPosition()]
        }
    }

    private fun getPosition() = arguments?.getInt(TAB_NUMBER) ?: 0

    private fun hideProgress() {
        newsLoadingProgressBar.hide()
        newsSwipeRefreshLayout.isRefreshing = false
        swipeLoading = false
    }

    private fun initSwipeRefresh() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            newsSwipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.main_color, null))
        }
        newsSwipeRefreshLayout.setOnRefreshListener {
            swipeLoading = true
            viewModel.refresh()
        }
    }

    private fun subscribeObservers() {
        viewModel.getArticleList()
                .removeObservers(viewLifecycleOwner)
        viewModel.getArticleList()
                .observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        setUi(it)
                    }
                })

        viewModel.getResponse()
                .removeObservers(viewLifecycleOwner)
        viewModel.getResponse()
                .observe(viewLifecycleOwner, Observer {
                    if (!viewModel.listIsEmpty()) {
                        newsAdapter.setStatus(it.status ?: NetworkResponse.Status.SUCCESS)
                    }
                    when (it.status) {
                        NetworkResponse.Status.LOADING -> if (!swipeLoading) newsLoadingProgressBar.show()
                        NetworkResponse.Status.SUCCESS -> hideProgress()

                        NetworkResponse.Status.ERROR -> {
                            hideProgress()
                            view?.snackbar("Error: ${it.message.toString()}")
                        }
                    }
                })
    }

    private fun setUi(it: PagedList<Article>) {
        newsRecyclerView.apply {

            newsAdapter.submitList(it)
            viewModel.retry()
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }


    override fun onClick(article: Article?) {
        val direction = PagerFragmentDirections.actionPagerToDetails(article)
        navController?.navigate(direction)
    }

    override fun onClick() {
        viewModel.retry()
    }


}