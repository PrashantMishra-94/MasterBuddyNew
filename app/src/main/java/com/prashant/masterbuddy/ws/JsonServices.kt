package com.prashant.masterbuddy.ws

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast

import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.prashant.masterbuddy.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.prashant.masterbuddy.ws.model.*

import org.apache.commons.io.IOUtils
import org.json.JSONException
import org.json.JSONObject

import java.io.FileInputStream
import java.io.IOException
import java.lang.reflect.Type
import java.util.HashMap


class JsonServices(private val mContext: Context, private val volleyCallback: VolleyCallback) {

    private val TAG = "JsonServices TEST!!"
    private var strResponse: String? = null
    private val builder = GsonBuilder()

    fun <T> createObjectFromJson(response: String?, returnType: Type): T? {

        val gson = builder.create()

        return gson.fromJson<T>(response, returnType)

    }

    fun executeRequest(url: String, json: String, returnType: Type) {
        Log.d(TAG, "json:$json")

        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
        }

        val jsonObjectRequest = JsonObjectRequest(com.android.volley.Request.Method.POST, url, jsonObject,

                Response.Listener { response ->
                    Log.d(TAG, "Response from volley " + response)
                    strResponse = response.toString()
                    volleyCallback.onSuccess(createObjectFromJson<Any>(strResponse, returnType))
                }, Response.ErrorListener { error ->
            Log.e(TAG, "Error Status code: " + error?.networkResponse?.statusCode)
            error.printStackTrace()
            volleyCallback.onFailure()
        })

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(mContext)
        requestQueue.add(jsonObjectRequest)
        volleyCallback.starting()

    }

    fun executeVideoRequest(selectedPath: Uri, title: String, description: String, userId: Int) {
        val jsonObjectRequest = object : VolleyMultipartRequest(com.android.volley.Request.Method.POST, "http://masterbuddy.info/video/upload",

                Response.Listener { response ->
                    try {
                        val obj = JSONObject(String(response.data))
                        //Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        volleyCallback.onSuccess(true)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
                    error.printStackTrace()
                    volleyCallback.onFailure()
                }) {
            protected override val byteData: Map<String, DataPart>?
                @Throws(AuthFailureError::class)
                get() {
                    val params = HashMap<String, DataPart>()
                    val imagename = System.currentTimeMillis()
                    params["video"] = DataPart(imagename.toString() + ".mp4", convertFileToByteArray(selectedPath))
                    return params
                }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["title"] = title
                params["description"] = description
                params["userId"] = "25"
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                2000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        // Access the RequestQueue through your singleton class.
        Volley.newRequestQueue(mContext).add(jsonObjectRequest)
        volleyCallback.starting()
    }

    private fun convertFileToByteArray(selectedPath: Uri): ByteArray {
        var buf = ByteArray(0)
        try {
            val `in` = (mContext.contentResolver.openInputStream(selectedPath) as? FileInputStream)!!
            buf = IOUtils.toByteArray(`in`)
            `in`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return buf
    }

    fun doLogin(username: String, password: String) {
        val userRequest = UserRequest(username, password)
        val gson = Gson()
        val json = gson.toJson(userRequest)
        executeRequest("http://api.masterbuddy.com/User/Login", json, UserResult::class.java)
    }

    fun registerUser(name: String, emailId: String, Password: String, currentDate: String) {
        val registerRequest = RegisterRequest(name, emailId, Password, currentDate)
        val gson = Gson()
        val json = gson.toJson(registerRequest)
        executeRequest("http://api.masterbuddy.com/User/Register", json, RegisterResult::class.java)
    }

    fun getVideoList(startIndex: Int, type: Int) {
        val selectVideosRequest = SelectVideosRequest()
        selectVideosRequest.setStartIndex(startIndex)
        selectVideosRequest.setLastIndex(startIndex + 20)
        val gson = Gson()
        val json = gson.toJson(selectVideosRequest)
        val homeURL = "http://masterbuddy.info/File/SelectVideos"
        val trendingURL = "http://masterbuddy.info/File/SelectTrendingVideos"
        executeRequest(if (type == Constants.LIST_TYPE_TRENDING) trendingURL else homeURL, json, SelectVideoResponse::class.java)
    }

    fun getAllFiles(channel: Int? = null, media: Int? = null, startIndex: Int = 0) {
        val json = Gson().toJson(GetAllFilesRequest(channel, media, startIndex))
        executeRequest("http://api.masterbuddy.com/File/GetAllFiles", json, GetAllFileResponse::class.java)
    }
}