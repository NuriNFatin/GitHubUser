package com.example.githubuser.source

import android.content.Context
import com.example.githubuser.database.FavoriteRoomDatabase
import com.example.githubuser.repository.FavoriteRepository

object Injection {
    fun provideFavoriteRepository(context: Context): FavoriteRepository {
        val database = FavoriteRoomDatabase.getInstance(context)
        val dao = database.FavoriteDao()
        return FavoriteRepository.getInstance(dao)
    }
}