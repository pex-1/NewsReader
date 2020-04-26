package practice.newsreader.data.model

class NetworkResponse(val status: Status, val message: String?) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun error(msg: String?): NetworkResponse {
            return NetworkResponse(Status.ERROR, msg)
        }

        fun success(): NetworkResponse {
            return NetworkResponse(Status.SUCCESS, null)
        }

        fun start(): NetworkResponse {
            return NetworkResponse(Status.LOADING, null)
        }
    }

}