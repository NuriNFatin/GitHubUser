package com.example.githubuser.ui.view;

import FavoriteViewModel
import SectionsPagerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.database.FavoriteDao
import com.example.githubuser.database.FavoriteRoomDatabase
import com.example.githubuser.database.FavoriteUser
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.repository.FavoriteRepository
import com.example.githubuser.ui.viewModel.DetailViewModel
import com.example.githubuser.ui.viewModel.FavoriteViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private var userDetailResponse = DetailUserResponse()
    private lateinit var viewPager: ViewPager2
    private lateinit var progressBar: ProgressBar
    private var isFavorite: Boolean = false
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var factory: FavoriteViewModelFactory
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        viewPager = activityDetailBinding.viewPager
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val username = intent.extras?.getString("username")
        val favoriteButton: FloatingActionButton = findViewById(R.id.fabFavorite)

        val roomDatabase = FavoriteRoomDatabase.getInstance(this)
        favoriteDao = roomDatabase.FavoriteDao()
        favoriteRepository = FavoriteRepository.getInstance(favoriteDao)
        factory = FavoriteViewModelFactory(favoriteRepository)
        favoriteViewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)

        favoriteViewModel.getFavoriteUserByUsername(username ?: "").observe(this) { favoriteUser ->
            isFavorite = favoriteUser != null
            updateFavoriteIcon()
        }

        setupSectionPager(username)
        setupTabLayout()

        with(detailViewModel) {
            if (detailUser.value == null) {
                getUserDetail(username ?: "a")
            }

            detailUser.observe(this@DetailActivity) { detailUserResponse ->
                setUserDetail(detailUserResponse)
                userDetailResponse = detailUserResponse
                progressBar.visibility = View.GONE
            }

            activityDetailBinding.detail.setOnClickListener {
                val githubProfileUrl = "https://github.com/${userDetailResponse.login}".toUri()
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = githubProfileUrl
                    startActivity(this)
                }
            }

        }
        activityDetailBinding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                deleteFromFavorite()
            } else {
                insertToFavorite()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun insertToFavorite() {
        val favoriteUser = FavoriteUser(
            username = userDetailResponse.login ?: "",
            avatarUrl = userDetailResponse.avatarUrl
        )
        favoriteViewModel.insertFavoriteUser(favoriteUser)
        isFavorite = true
        updateFavoriteIcon()
        showToast("Added to favorites")
    }

    private fun deleteFromFavorite() {
        val favoriteUser = FavoriteUser(
            username = userDetailResponse.login ?: ""
        )
        favoriteViewModel.deleteFavoriteUser(favoriteUser)
        isFavorite = false
        updateFavoriteIcon()
        showToast("delete to favorites")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateFavoriteIcon() {
        if (isFavorite) {
            activityDetailBinding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            activityDetailBinding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun setupSectionPager(username: String?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username ?: "a")
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
    }

    private fun setupTabLayout() {
        val tab: TabLayout = findViewById(R.id.tab_layout)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        TabLayoutMediator(tab, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setUserDetail(user: DetailUserResponse) {
        Glide.with(this@DetailActivity)
            .load(user.avatarUrl)
            .into(activityDetailBinding.photo)

        val detailUsername = "@${user.login}"
        val detailRepository = "${user.publicRepos} repository"
        with(activityDetailBinding) {
            username.text = detailUsername
            twitter.text = user.twitterUsername?.toString()
            fullname.text = user.name?.toString() ?: "-"
            bio.text = user.bio?.toString() ?: "There is no bio."
            gitsUrl.text = "https://github.com/${user.login}"
            location.text = user.location?.toString() ?: "-"
            company.text = user.company?.toString()
            blog.text = user.blog?.toString()
            detailUserFollow.text = resources.getString(
                R.string.followers_and_following,
                user.followers,
                user.following
            ).toString()
        }
        progressBar.visibility = View.GONE
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
