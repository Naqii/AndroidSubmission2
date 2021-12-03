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
import com.example.submissionsatu.databinding.FragmentFollowingBinding
import com.example.submissionsatu.model.User
import com.example.submissionsatu.view.DetailUser
import com.example.submissionsatu.viewmodel.FollowingModel

class Following : Fragment() {

//    companion object {
//        private val TAG = Following::class.java.simpleName
//    }

//    private var listUser: ArrayList<User> = ArrayList()
    private lateinit var getFollowingModel: FollowingModel
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: ListUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.tvFollowing.layoutManager = LinearLayoutManager(requireContext())

        val userList = activity?.intent?.getParcelableExtra(DetailUser.EXTRA_DATA) as? User

        getFollowingModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingModel::class.java)
        getFollowingModel.getData().observe(viewLifecycleOwner, { listUser ->
            if (listUser != null) {
                adapter.setData(listUser)
                showLoading(false)
            }
        })
        if (userList != null) {
            getFollowingModel.setDataFollowing(userList.username)
        }

        binding.tvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.tvFollowing.adapter = adapter
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
            binding.progressBarFollowing.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowing.visibility = View.INVISIBLE
        }

    }
}