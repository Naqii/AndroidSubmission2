package com.example.submissionsatu.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionsatu.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setDetailUser(id: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$id"
        val detailUser = ArrayList<User>()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
                ) {
                    try {
                        val result = String(responseBody)
                        val jsonObject = JSONObject(result)
                        val user = User()
                        user.avatar = jsonObject.getString("avatar_url")
                        user.username = jsonObject.getString("login")
                        user.name = jsonObject.getString("name")
                        user.location = jsonObject.getString("location")
                        user.company = jsonObject.getString("company")
                        user.repository = jsonObject.getString("public_repos")
                        user.followers = jsonObject.getString("followers")
                        user.following = jsonObject.getString("following")
                        detailUser.add(user)
                        listUser.postValue(detailUser)
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
                ) {
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message + " DETAIL "}"
                    }
                    Log.d("error", errorMessage)
                }
            })
    }

    fun getDetailUser(): LiveData<ArrayList<User>> {
        return listUser
    }
}