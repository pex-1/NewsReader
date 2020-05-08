package practice.newsreader.data.model


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
    @ColumnInfo(name = "source_id")
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String
):Parcelable