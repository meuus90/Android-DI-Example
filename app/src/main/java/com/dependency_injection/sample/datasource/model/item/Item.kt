package com.dependency_injection.sample.datasource.model.item

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dependency_injection.sample.datasource.model.BaseData
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Items")
data class Item(
        @field:PrimaryKey(autoGenerate = true)
        @field:ColumnInfo(name = "index") val index: Int,

        @field:ColumnInfo(name = "kind") val kind: String,
        @field:ColumnInfo(name = "etag") val etag: String,
        @field:Embedded(prefix = "id") val id: Id,
        @field:Embedded(prefix = "snippet") val snippet: Snippet,
        @field:ColumnInfo(name = "channelTitle") val channelTitle: String
) : BaseData(), Parcelable {

    @Parcelize
    data class Id(
            @field:ColumnInfo(name = "kind") val kind: Int,
            @field:ColumnInfo(name = "videoId") val videoId: Int,
            @field:ColumnInfo(name = "channelId") val channelId: Int,
            @field:ColumnInfo(name = "playlistId") val playlistId: Int
    ) : Parcelable

    @Parcelize
    data class Snippet(
            @field:ColumnInfo(name = "publishedAt") val publishedAt: String,
            @field:ColumnInfo(name = "channelId") val channelId: String,
            @field:ColumnInfo(name = "title") val title: String,
            @field:ColumnInfo(name = "description") val description: String,
            @field:Embedded(prefix = "thumbnails") val thumbnails: Thumbnails
    ) : Parcelable

    @Parcelize
    data class Thumbnails(
            @field:ColumnInfo(name = "url") val url: String,
            @field:ColumnInfo(name = "width") val width: Int,
            @field:ColumnInfo(name = "height") val height: Int
    ) : Parcelable
}