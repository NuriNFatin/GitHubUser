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

class FollowingViewModel : ViewModel() {

    private val _listfollowing = MutableLiveData<List<ItemsItem>>()
    val listfollowing: LiveData<List<ItemsItem>> = _listfollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _noFollowingAnyoneText = MutableLiveData<String>()
    val noFollowingAnyoneText: LiveData<String> = _noFollowingAnyoneText

    fun getUserFollowingByUsername(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(user)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>,response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listfollowing.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    fun getFirstFollower(): ItemsItem? {
        return _listfollowing.value?.get(0)
    }

    fun setNoFollowersAnyoneText(text: String) {
        _noFollowingAnyoneText.value = text
    }
}
