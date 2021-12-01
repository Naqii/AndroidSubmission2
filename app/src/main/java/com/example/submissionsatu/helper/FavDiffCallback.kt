package com.example.submissionsatu.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.submissionsatu.database.Favourites

class FavDiffCallback(private val mOldFavList: List<Favourites>, private val mNewFavList: List<Favourites>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].username == mNewFavList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmploye = mOldFavList[oldItemPosition]
        val newEmploye = mNewFavList[newItemPosition]
        return oldEmploye.name == newEmploye.name
    }
}