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
                    user.avatar = fav.avatar
                    user.username = fav.username
                    user.name = fav.name
                    val intent = Intent(it.context, DetailUser::class.java).putExtra(DetailUser.EXTRA_DATA, user)
                    intent.putExtra(DetailUser.EXTRA_FAV, fav)
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