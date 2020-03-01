package practice.newsreader.ui.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_details.*
import practice.newsreader.R
import practice.newsreader.util.DateUtils
import javax.inject.Inject

class DetailsFragment : DaggerFragment() {

    @Inject
    lateinit var picasso: Picasso

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setUi()
    }


    private fun setUi() {
        arguments?.let {
            val article = DetailsFragmentArgs.fromBundle(it).article!!
            detailsAuthorTextView.text = article.author
            detailsTitleTextView.text = article.title
            detailsSourceAndTimeTextView.text = "${article.source.name}  \u2022  ${DateUtils.getTimePassed(article.publishedAt)} ago"
            detailsContentTextView.text = article.content

            picasso.load(article.urlToImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .fit()
                .into(detailsImageView)

        }
    }

    private fun setToolbar() {
        with((requireActivity() as AppCompatActivity)) {
            setSupportActionBar(detailsToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}
