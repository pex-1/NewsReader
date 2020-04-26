package practice.newsreader.ui.fragments.news

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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


class NewsFragment : DaggerFragment(), NewsAdapter.OnArticleClicked, NewsAdapter.OnFooterClicked {

    private var navController: NavController? = null

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: NewsViewModel

    private var swipeLoading = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: context!!
        view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.fragmentBackground))

        setToolbar()

        navController = Navigation.findNavController(view)

        viewModel = ViewModelProvider(this, providerFactory).get(NewsViewModel::class.java)

        subscribeObservers()

        initSwipeRefresh()

        setSearchLayout()

        val itemDecoration = VerticalSpacingItemDecoration(15)
        newsRecyclerView.addItemDecoration(itemDecoration)

        resetSearchImageView.setOnClickListener {
            Constants.search = null
            searchQueryLinearLayout.hide()
            viewModel.refresh()
        }
    }

    private fun setSearchLayout() {
        if (Constants.search != null) {
            searchQueryLinearLayout.show()
            searchQueryTextView.text = Constants.search
        }
    }

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
            //newsSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun subscribeObservers() {
        viewModel.getArticleList()
                .observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        //Log.e("OBSERVER", "new list: ${it[0]}")
                        setUi(it)
                    }
                })

        viewModel.getResponse()
                .observe(viewLifecycleOwner, Observer {
                    if (!viewModel.listIsEmpty()) {
                        newsAdapter.setStatus(it.status ?: NetworkResponse.Status.SUCCESS)
                    }
                    when (it.status) {
                        NetworkResponse.Status.LOADING -> Log.e("LOADING", "swipe $swipeLoading")
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.news_menu, menu)


        val manager = activity?.getSystemService(SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))

        searchView.isFocusable = true
        searchView.isIconified = true
        searchView.requestFocusFromTouch()
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                Constants.search = query
                viewModel.refresh()
                context?.toast("looking for $query")
                setSearchLayout()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setToolbar() {
        with((requireActivity() as AppCompatActivity)) {
            setSupportActionBar(newsToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }


    override fun onClick(article: Article?) {
        val direction = NewsFragmentDirections.actionDetailsScreen(article)
        navController?.navigate(direction)
    }

    override fun onClick() {
        viewModel.retry()
    }




}