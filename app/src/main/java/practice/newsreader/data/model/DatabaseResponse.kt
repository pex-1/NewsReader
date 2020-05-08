package practice.newsreader.data.model

class DatabaseResponse(val status: Status, val message: String? = null) {

    enum class Status {
        INSERT, DELETE, ERROR
    }

    companion object {
        fun error(msg: String?): DatabaseResponse {
            return DatabaseResponse(Status.ERROR, msg)
        }

        fun insert(msg: String): DatabaseResponse {
            return DatabaseResponse(Status.INSERT, msg)
        }

        fun delete(msg: String): DatabaseResponse{
            return DatabaseResponse(Status.DELETE, msg)
        }

    }

}