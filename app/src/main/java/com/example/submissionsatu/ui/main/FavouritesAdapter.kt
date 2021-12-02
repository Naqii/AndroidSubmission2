package com.example.submissionsatu.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionsatu.database.Favourites
import com.example.submissionsatu.databinding.ItemFavouritesBinding
import com.example.submissionsatu.helper.FavDiffCallback
import com.example.submissionsatu.model.User
import com.example.submissionsatu.view.DetailUser

class FavouritesAdapter: RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {


    inner class FavouritesViewHolder(private val binding: ItemFavouritesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fav: Favourites) {
            with(binding) {
                favUsername.text = fav.username
                favName.text = fav.name
                Glide.with(itemView.context)
                    .load(fav.avatar)
                    .into(binding.avatarFav)
                itemView.setOnClickListener {
                    val user = User()
                    user.avatar
                    user.username
                    user.name
                    user.company
                    user.location
                    user.repository
                    user.following
                    user.followers
                    val intent = Intent(it.context, DetailUser::class.java)
                    intent.putExtra(DetailUser.EXTRA_DATA, user)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    private val listFavourites = ArrayList<Favourites>()

    fun setListFavourites(listFavourites: List<Favourites>) {
        val diffCallback = FavDiffCallback(this.listFavourites, listFavourites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavourites.clear()
        this.listFavourites.addAll(listFavourites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val binding = ItemFavouritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.bind(listFavourites[position])
    }

    override fun getItemCount(): Int {
        return listFavourites.size
    }
}