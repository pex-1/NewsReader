package practice.newsreader.ui.fragments.news

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_news.*
import practice.newsreader.R
import practice.newsreader.data.model.Article
import practice.newsreader.ui.viewmodels.ViewModelProviderFactory
import practice.newsreader.util.VerticalSpacingItemDecoration
import practice.newsreader.util.isConnectedToNetwork
import javax.inject.Inject


class NewsFragment : DaggerFragment(), NewsAdapter.OnArticleClicked{

    private var navController: NavController? = null

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: NewsViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()

        navController = Navigation.findNavController(view)
        if(context?.isConnectedToNetwork()!!){
            newsLoadingProgressBar.visible()
            viewModel = ViewModelProvider(this, providerFactory).get(NewsViewModel::class.java)

            subscribeObservers()
        }else{
            val snack = Snackbar.make(view,"No network connection!", Snackbar.LENGTH_LONG)
            snack.show()
        }

    }

    private fun subscribeObservers(){
        viewModel.news.observe(viewLifecycleOwner, Observer {
            if(it != null){
                setUi(it)
                newsLoadingProgressBar?.gone()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                Toast.makeText(activity, "looking for $query", Toast.LENGTH_LONG).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }


    private fun setToolbar(){
        with((requireActivity() as AppCompatActivity)) {
            setSupportActionBar(newsToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun setUi(it: List<Article>) {
        newsRecyclerView.apply {
            val itemDecoration = VerticalSpacingItemDecoration(15)
            addItemDecoration(itemDecoration)

            newsAdapter.setData(it)
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

    }

    override fun onClick(article: Article) {
        val direction = NewsFragmentDirections.actionDetailsScreen(article)
        navController?.navigate(direction)
    }

    private fun View.visible(){
        this.visibility = View.VISIBLE
    }

    private fun View.gone(){
        this.visibility = View.GONE
    }

}