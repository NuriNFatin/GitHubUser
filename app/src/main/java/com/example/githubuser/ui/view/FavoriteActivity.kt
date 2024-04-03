package com.example.githubuser.ui.view

import FavoriteViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.response.adapter.ListGitAdapter
import com.example.githubuser.database.FavoriteDao
import com.example.githubuser.database.FavoriteRoomDatabase
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.repository.FavoriteRepository
import com.example.githubuser.ui.viewModel.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var userFavoriteBinding: ActivityFavoriteBinding
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var listGitAdapter: ListGitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(userFavoriteBinding.root)

        favoriteDao = FavoriteRoomDatabase.getInstance(application).FavoriteDao()
        val favoriteRepository = FavoriteRepository.getInstance(favoriteDao)
        val favoriteViewModelFactory = FavoriteViewModelFactory.getInstance(favoriteRepository)
        favoriteViewModel = ViewModelProvider(this, favoriteViewModelFactory).get(FavoriteViewModel::class.java)

        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listGitAdapter = ListGitAdapter()

        setupRecyclerView()

        favoriteViewModel.getAllUserFavorite().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map { user ->
                val item = ItemsItem(login = user.username, avatarUrl = user.avatarUrl)
                items.add(item)
            }
            val adapter = ListGitAdapter()
            adapter.submitList(items)
            userFavoriteBinding.UserFavorite.adapter = adapter
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        userFavoriteBinding.UserFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        userFavoriteBinding.UserFavorite.addItemDecoration(itemDecoration)
        userFavoriteBinding.UserFavorite.adapter = listGitAdapter
    }
}