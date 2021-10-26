package com.example.submissionsatu.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionsatu.model.User
import com.example.submissionsatu.view.fragment.Followers
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersModel : ViewModel() {
    val followersModel = MutableLiveData<ArrayList<User>>()

    fun setDataFollowers(id: String) {
        val listItem = ArrayList<User>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(
            "https://api.github.com/users/$id/followers",
            object : AsyncHttpResponseHandler() {
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