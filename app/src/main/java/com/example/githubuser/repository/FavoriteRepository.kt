package com.example.githubuser.repository

import androidx.lifecycle.LiveData
import com.example.githubuser.database.FavoriteDao
import com.example.githubuser.database.FavoriteUser

class FavoriteRepository
private constructor(
    private val userFavoriteDao: FavoriteDao,
) {

    fun getListFavoriteUser(): LiveData<List<FavoriteUser>> {
        return userFavoriteDao.getListFavoriteUser()
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return userFavoriteDao.getFavoriteUserByUsername(username)
    }

    suspend fun deleteFavoriteUser(userFavorite: FavoriteUser) {
        return userFavoriteDao.delete(userFavorite)
    }

    suspend fun insertFavoriteUser(userFavorite: FavoriteUser) {
        return userFavoriteDao.insert(userFavorite)
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            userFavoriteDao: FavoriteDao,
        ): FavoriteRepository = instance ?: synchronized(this) {
            instance ?: FavoriteRepository(userFavoriteDao).also {
                instance = it
            }
        }
    }
}
