package practice.newsreader.ui.news

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_footer.view.*
import kotlinx.android.synthetic.main.item_news.view.*
import practice.newsreader.R
import practice.newsreader.data.model.Article
import practice.newsreader.data.model.NetworkResponse
import practice.newsreader.util.DateUtils
import javax.inject.Inject

private const val DATA_VIEW_TYPE = 1
private const val FOOTER_VIEW_TYPE = 2
class NewsAdapter @Inject constructor(private val footerClickListener: OnFooterClicked, private val clickListener: OnArticleClicked, private val glide: RequestManager) : PagedListAdapter<Article, RecyclerView.ViewHolder>(NewsItemDiffCallback) {

    private var status = NetworkResponse.Status.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) NewsViewHolder(parent).create(parent) else ErrorViewHolder(parent).create(parent)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) (holder as NewsViewHolder).bind(getItem(position))
        else (holder as ErrorViewHolder).bind(status)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (status == NetworkResponse.Status.ERROR)
    }

    interface OnArticleClicked {
        fun onClick(article: Article?)
    }

    interface OnFooterClicked {
        fun onClick()
    }

    fun setStatus(status: NetworkResponse.Status) {
        this.status = status
        notifyItemChanged(super.getItemCount())
    }

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(article: Article?) {
            if (article != null) {
                with(itemView) {

                    glide.load(article.urlToImage)
                            .centerCrop()
                            .error(R.drawable.placeholder)
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
        }

        fun create(parent: ViewGroup): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_news, parent, false)
            return NewsViewHolder(view)
        }


    }

    inner class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(status: NetworkResponse.Status?) {
            itemView.footerErrorProgressBar.visibility = if (status == NetworkResponse.Status.LOADING) View.VISIBLE else View.INVISIBLE
            itemView.footerErrorTextView.visibility = if (status == NetworkResponse.Status.ERROR) View.VISIBLE else View.INVISIBLE
            itemView.rootView.setOnClickListener {
                footerClickListener.onClick()
            }
        }


        fun create(parent: ViewGroup): ErrorViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
            return ErrorViewHolder(view)
        }

    }

    companion object {
        val NewsItemDiffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url && oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }


}