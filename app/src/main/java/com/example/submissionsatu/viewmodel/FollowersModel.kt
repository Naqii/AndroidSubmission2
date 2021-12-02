package com.example.submissionsatu.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionsatu.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersModel : ViewModel() {
    val followersModel = MutableLiveData<ArrayList<User>>()

    fun setDataFollowers(username: String) {
        val listItem = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}/followers"
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
                        val jsonArray = JSONArray(result)
                        Log.d("response", jsonArray.toString())
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val user = User()
                            user.username = jsonObject.getString("login")
                            user.avatar = jsonObject.getString("avatar_url")
                            listItem.add(user)
                        }
                        followersModel.postValue(listItem)
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
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d("error", errorMessage)
                }
            })
    }

    fun getData(): LiveData<ArrayList<User>> {
        return followersModel
    }
}