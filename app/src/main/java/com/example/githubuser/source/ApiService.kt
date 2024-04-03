package com.example.githubuser.source

import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
        @GET("search/users")
        fun getSearchUsers(@Query("q") username: String): Call<UserResponse>

        @GET("users/{username}")
         fun getUserDetails(@Path("username") username: String): Call<DetailUserResponse>

        @GET("users/{username}/followers")
         fun getUserFollowers(@Path("username") username: String): Call<List<ItemsItem>>

        @GET("users/{username}/following")
        fun getUserFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}