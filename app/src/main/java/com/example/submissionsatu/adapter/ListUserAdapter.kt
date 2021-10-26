package com.example.submissionsatu.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionsatu.databinding.ItemRowUserBinding
import com.example.submissionsatu.model.User

class ListUserAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(item: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(item)
        notifyDataSetChanged()
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataUser = listUser[position]
        Glide.with(holder.itemView.context)
            .load(dataUser.avatar)
            .into(holder.binding.avatar)
        holder.binding.tvUsername.text = dataUser.username
        holder.binding.tvName.text = dataUser.name
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataUser)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}