package com.example.submissionsatu.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favourites")
data class Favourites (
    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "avatar")
    var avatar: String = "",

): Parcelable