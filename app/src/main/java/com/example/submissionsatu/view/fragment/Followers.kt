package com.example.submissionsatu.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionsatu.adapter.ListUserAdapter
import com.example.submissionsatu.databinding.FragmentFollowersBinding
import com.example.submissionsatu.model.User
import com.example.submissionsatu.view.DetailUser
import com.example.submissionsatu.viewmodel.FollowersModel

class Followers : Fragment() {
    private lateinit var getFollowersModel: FollowersModel
    private lateinit var binding: FragmentFollowersBinding
    private lateinit var adapter: ListUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        val userList = activity?.intent?.getParcelableExtra(DetailUser.EXTRA_DATA) as? User

        getFollowersModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersModel::class.java)
        getFollowersModel.getData().observe(viewLifecycleOwner, { listUser ->
            if (listUser != null) {
                adapter.setData(listUser)
                showLoading(false)
            }
        })
        if (userList != null) {
            getFollowersModel.setDataFollowers(userList.username)
        }

        binding.tvFollowers.layoutManager = LinearLayoutManager(requireContext())
        binding.tvFollowers.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(activity, DetailUser::class.java)
        intent.putExtra(DetailUser.EXTRA_DATA, user)
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFollowers.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowers.visibility = View.INVISIBLE
        }
    }
}