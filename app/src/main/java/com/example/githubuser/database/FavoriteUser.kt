package com.example.githubuser.database

import android.os.Parcelable
import android.support.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var username: String = "",
    var avatarUrl: String? = null,
) : Parcelable