package practice.newsreader.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import practice.newsreader.R
import practice.newsreader.util.DateUtils
import practice.newsreader.util.makeStatusBarTransparent
import javax.inject.Inject

class DetailsActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setToolbar()
        setUi()

        makeStatusBarTransparent()

    }

    private fun setUi() {
        val article = intent.extras?.let { DetailsActivityArgs.fromBundle(it).article }
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
