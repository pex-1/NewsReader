package practice.newsreader.ui.details

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import practice.newsreader.R
import practice.newsreader.data.model.Article
import practice.newsreader.ui.viewmodels.ViewModelProviderFactory
import practice.newsreader.util.*
import javax.inject.Inject

class DetailsActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: DetailsViewModel

    private var article: Article? = null

    private var favorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        makeStatusBarTransparent()

        viewModel = ViewModelProvider(this, providerFactory).get(DetailsViewModel::class.java)

        subscribeObservers()
        setToolbar()
        setUi()

        favoriteImageView.setOnClickListener {
            if(favorite){
                article?.let { it1 -> viewModel.removeArticle(it1) }
            }else{
                article?.let { it1 -> viewModel.insertArticle(it1) }
            }
        }

    }

    private fun subscribeObservers() {
        viewModel.favorite.observe(this, Observer {
            favoritesIconToggle(it)
            favorite = it
        })

        viewModel.response.observe(this, Observer {
            it.message?.let { it1 -> detailsView.snackbar(it1) }
        })
    }

    private fun favoritesIconToggle(stored: Boolean){
        if (stored) {
            favoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star))
        }
        else favoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border))
    }

    private fun setUi() {
        article = intent.extras?.let { DetailsActivityArgs.fromBundle(it).article }
        viewModel.articleExists(article!!.publishedAt)
        detailsAuthorTextView.text = article?.author
        detailsTitleTextView.text = article?.title
        detailsSourceAndTimeTextView.text = "${article?.source?.name}  \u2022  ${DateUtils.getTimePassed(article?.publishedAt)} ago"
        detailsContentTextView.text = article?.content

        glide.load(article?.urlToImage)
                .centerCrop()
                .fitCenter()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(detailsImageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setToolbar() {
        setSupportActionBar(detailsToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
