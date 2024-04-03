package com.example.githubuser.ui.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.source.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun <T> Call<T>.enqueue(callback: Callback<List<ItemsItem>>) {

}

class FollowersViewModel : ViewModel() {

    private val _listfollowers = MutableLiveData<List<ItemsItem>>()
    val listfollowers: LiveData<List<ItemsItem>> = _listfollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _noFollowersAnyoneText = MutableLiveData<String>()
    val noFollowersAnyoneText: LiveData<String> = _noFollowersAnyoneText

    fun getUserFollowersByUsername(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(user)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listfollowers.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })


        fun getFirstFollower(): ItemsItem? {
            return _listfollowers.value?.get(0)
        }

        fun setNoFollowersAnyoneText(text: String) {
            _noFollowersAnyoneText.value = text
        }
    }
}
