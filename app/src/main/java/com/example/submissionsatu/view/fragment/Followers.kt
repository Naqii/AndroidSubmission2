package com.example.submissionsatu.view.fragment

import android.annotation.SuppressLint
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
    private var listUser = ArrayList<User>()
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
        adapter = ListUserAdapter(listUser)
        adapter.notifyDataSetChanged()

        binding.tvFollowers.layoutManager = LinearLayoutManager(requireContext())
        val userList = activity?.intent?.getParcelableExtra<User>(DetailUser.EXTRA_DATA) as User
        showFollowers(userList.toString())
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showFollowers(username: String) {
        getFollowersModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersModel::class.java)
        getFollowersModel.setDataFollowers(username)
        getFollowersModel.getData().observe(this, {listUser ->
            if (listUser != null) {
                adapter.setData(listUser)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFollowers.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowers.visibility = View.INVISIBLE
        }
    }

//    private fun showRecyclerList() {
//        binding.tvFollowers.layoutManager = LinearLayoutManager(activity)
//        binding.tvFollowers.adapter = adapter
//    }

//    private fun getUserFollowers(id: String) {
//        binding.progressBarFollowers.visibility = View.VISIBLE
//        val client = AsyncHttpClient()
//        client.addHeader("Authorization", "token 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")
//        client.addHeader("User-Agent", "request")
//        client.get(
//            "https://api.github.com/users/$id/followers",
//            object : AsyncHttpResponseHandler() {
//                override fun onSuccess(
//                    statusCode: Int,
//                    headers: Array<Header>,
//                    responseBody: ByteArray
//                ) {
//                    binding.progressBarFollowers.visibility = View.INVISIBLE
//                    val result = String(responseBody)
//                    Log.d(TAG, result)
//                    try {
//                        val jsonArray = JSONArray(result)
//                        for (i in 0 until jsonArray.length()) {
//                            val jsonObject = jsonArray.getJSONObject(i)
//                            val username: String = jsonObject.getString("login")
//                            getUserDetail(username)
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
//                            .show()
//                        e.printStackTrace()
//                    }
//                }
//
//                override fun onFailure(
//                    statusCode: Int,
//                    headers: Array<Header>,
//                    responseBody: ByteArray,
//                    error: Throwable
//                ) {
//                    binding.progressBarFollowers.visibility = View.INVISIBLE
//                    val errorMessage = when (statusCode) {
//                        401 -> "$statusCode : Bad Request"
//                        403 -> "$statusCode : Forbidden"
//                        404 -> "$statusCode : Not Found"
//                        else -> "$statusCode : ${error.message}"
//                    }
//                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
//    }
//
//    private fun getUserDetail(id: String) {
//        binding.progressBarFollowers.visibility = View.VISIBLE
//        val client = AsyncHttpClient()
//        client.addHeader("Authorization", "token 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")
//        client.addHeader("User-Agent", "request")
//        client.get("https://api.github.com/users/$id", object : AsyncHttpResponseHandler() {
//            override fun onSuccess(
//                statusCode: Int,
//                headers: Array<Header>,
//                responseBody: ByteArray
//            ) {
//                binding.progressBarFollowers.visibility = View.INVISIBLE
//                val result = String(responseBody)
//                Log.d(TAG, result)
//                try {
//                    val jsonObject = JSONObject(result)
//                    val username: String = jsonObject.getString("login").toString()
//                    val name: String = jsonObject.getString("name").toString()
//                    val avatar: String = jsonObject.getString("avatar_url").toString()
//                    val company: String = jsonObject.getString("company").toString()
//                    val location: String = jsonObject.getString("location").toString()
//                    val repository: String = jsonObject.getString("public_repos")
//                    val followers: String = jsonObject.getString("followers")
//                    val following: String = jsonObject.getString("following")
//                    listUser.add(
//                        User(
//                            username,
//                            name,
//                            avatar,
//                            location,
//                            repository,
//                            company,
//                            followers,
//                            following
//                        )
//                    )
//                    showRecyclerList()
//                } catch (e: Exception) {
//                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
//                        .show()
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                headers: Array<Header>,
//                responseBody: ByteArray,
//                error: Throwable
//            ) {
//                binding.progressBarFollowers.visibility = View.INVISIBLE
//                val errorMessage = when (statusCode) {
//                    401 -> "$statusCode : Bad Request"
//                    403 -> "$statusCode : Forbidden"
//                    404 -> "$statusCode : Not Found"
//                    else -> "$statusCode : ${error.message}"
//                }
//                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
//                    .show()
//            }
//        })
//    }
//
//
//    /***private fun showSelectedUser(user: User) {
//    User(
//    user.username,
//    user.name,
//    user.avatar,
//    user.repository,
//    user.company,
//    user.location,
//    user.followers,
//    user.following,
//    )
//    val intent = Intent(activity, DetailUser::class.java)
//    intent.putExtra(DetailUser.EXTRA_DATA, user)
//    startActivity(intent)
//
//    Toast.makeText(activity, "Your Choice " + user.username, Toast.LENGTH_SHORT).show()
//    }*/
}