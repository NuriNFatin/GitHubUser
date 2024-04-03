package com.example.githubuser.data.response.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.UserRowBinding
import com.example.githubuser.ui.view.DetailActivity

class ListGitAdapter : ListAdapter<ItemsItem, ListGitAdapter.MyViewHolder>(DIFF_CALLBACK) {

        class MyViewHolder(private val binding: UserRowBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(user: ItemsItem) {
                val bundle = Bundle()
                bundle.putString("username", user.login)

                Glide.with(binding.root)
                    .load(user.avatarUrl)
                    .placeholder(ColorDrawable(Color.LTGRAY))
                    .into(binding.imgItemPhoto)
                binding.tvItemUsername.text = user.login
                binding.root.setOnClickListener { view ->
                    Intent(view.context, DetailActivity::class.java).apply {
                        putExtras(bundle)
                        view.context.startActivity(this)
                    }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val binding = UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val user = getItem(position)
            holder.bind(user)
        }

        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
                override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                    return oldItem == newItem
                }
            }
        }
}