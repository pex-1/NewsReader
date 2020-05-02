package practice.newsreader.util

object EndpointsUtil {

    var position = 0
    var searchQuery = arrayListOf<String?>(null, null, null, null)
    val ENDPOINTS = arrayOf("top-headlines?country=us", "everything?domains=wsj.com,nytimes.com", "top-headlines?sources=bbc-news", "top-headlines?country=de&amp;category=business")

}