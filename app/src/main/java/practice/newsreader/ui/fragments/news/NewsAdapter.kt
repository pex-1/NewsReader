package practice.newsreader.ui.fragments.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*
import practice.newsreader.R
import practice.newsreader.data.model.Article
import practice.newsreader.util.DateUtils
import javax.inject.Inject

class NewsAdapter @Inject constructor(private val clickListener: OnArticleClicked, private val picasso: Picasso) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var news = listOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_news,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = news[position]

        with(holder.itemView) {
            picasso.load(article.urlToImage)
                    .resizeDimen(R.dimen.image_size, R.dimen.image_size)
                    .onlyScaleDown()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(articleImageView)

            articleSourceTextView.text = article.source.name
            articleSubtitleTextView.text = article.description
            articleAuthorTextView.text = article.author
            articleTimeTextView.text = DateUtils.getTimePassed(article.publishedAt)

            rootView.setOnClickListener {
                clickListener.onClick(article)
            }
        }
    }


    fun setData(articles: List<Article>) {
        val oldList = news
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
                NewsItemDiffCallback(
                        oldList,
                        articles)
        )
        news = articles
        diffResult.dispatchUpdatesTo(this)
        //notifyDataSetChanged()
    }

    class NewsItemDiffCallback(private var oldNewsList: List<Article>, private var newNewsList: List<Article>) :
            DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldNewsList[oldItemPosition].publishedAt == newNewsList[newItemPosition].publishedAt)
        }

        override fun getOldListSize(): Int = oldNewsList.size

        override fun getNewListSize(): Int = newNewsList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldNewsList[oldItemPosition] == newNewsList[newItemPosition]
        }

    }

    interface OnArticleClicked {
        fun onClick(article: Article)
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}