package com.dependency_injection.sample.datasource.model.item

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dependency_injection.sample.datasource.model.BaseData
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Items")
data class Item(
        @field:PrimaryKey(autoGenerate = true)
        @field:ColumnInfo(name = "index") val index: Int = 0,

        @field:ColumnInfo(name = "id") val id: Int,
        @field:ColumnInfo(name = "name") val name: String,
        @field:ColumnInfo(name = "createdAt") val createdAt: Long,
        @field:ColumnInfo(name = "updatedAt") val updatedAt: Long
) : BaseData(), Parcelable