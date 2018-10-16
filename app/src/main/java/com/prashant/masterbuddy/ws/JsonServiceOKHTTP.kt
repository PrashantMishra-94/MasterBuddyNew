package com.prashant.masterbuddy.ws

import android.content.Context
import android.util.Log

import com.prashant.masterbuddy.utils.Utils
import com.prashant.masterbuddy.ws.model.RegisterRequest
import com.prashant.masterbuddy.ws.model.RegisterResult
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.lang.reflect.Type

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class JsonServiceOKHTTP(private val mContext: Context, private val volleyCallback: VolleyCallback) {
    private val client = OkHttpClient()

    private fun <T> executeAsync(func: IFunction<T>) {
        object : android.os.AsyncTask<Void, Void, OperationResult<T>>() {
            override fun onPreExecute() {
                if (Utils.isConnectingToInternet(mContext, true)) {
                    volleyCallback.starting()
                } else {
                    cancel(true)
                }
            }

            override fun doInBackground(vararg params: Void): OperationResult<T> {
                val result = OperationResult<T>()
                try {
                    result.Result = func.func()
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                    result.Exception = ex
                    Log.e(TAG, ex.toString())
                }

                return result
            }

            override fun onPostExecute(result: OperationResult<T>?) {
                Log.d("TEST_LOG", if (result == null) "NULL Object" else " : " + Gson().toJson(result))
                if (result != null && result.Result != null) {
                    volleyCallback.onSuccess(result.Result)
                } else {
                    volleyCallback.onFailure()
                }
            }
        }.execute()
    }

    private fun <T> executeRequest(url: String, json: String?, returnType: Type): T? {
        Log.d("Test!!!!!!", "json:$json")
        var result: T? = null
        val maxLogSize = 10000
        var responseCode = 0
        var responseMessage = ""
        try {
            var body: RequestBody? = null
            if (json != null) {
                body = RequestBody.create(JSON, json)
            }
            val request = Request.Builder().url(url).post(body).build()
            val response = client.newCall(request).execute()
            val jsonResponse = response.body()!!.string()
            responseCode = response.code()
            responseMessage = response.message()
            Log.d("TestJSON", jsonResponse)
            val builder = GsonBuilder()
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss")
            builder.serializeNulls()
            val gson = builder.create()

            result = gson.fromJson<T>(jsonResponse, returnType)

            if (result == null) {
                throw NullPointerException("Response null exception")
            }
        } catch (e: Exception) {
            val message = ("executeRequest Exception: " + e.toString() + " ResponseCode: " + responseCode + " Response Message: "
                    + responseMessage + " URL: " + url + " JSON: " + json)
            Log.d(TAG, message)
        }

        return result
    }

    fun registerUserAsync(name: String, emailId: String, password: String, currentDate: String) {
        executeAsync<RegisterResult>(object : IFunction<RegisterResult> {
            override fun func(): RegisterResult {
                return registerUser(name, emailId, password, currentDate)!!
            }
        })
    }

    private fun registerUser(name: String, emailId: String, Password: String, currentDate: String): RegisterResult? {
        val registerRequest = RegisterRequest(name, emailId, Password, currentDate)
        val gson = Gson()
        val json = gson.toJson(registerRequest)
        return executeRequest<RegisterResult>("http://api.masterbuddy.com/User/Register", json, RegisterResult::class.java)
    }

    companion object {

        private val TAG = "JsonServiceOKHTTP"
        private val JSON = MediaType
                .parse("application/json; charset=utf-8")
    }

}

internal class OperationResult<T> {
    var Exception: java.lang.Exception? = null
    var Result: T? = null
    var Tag: Any? = null
}

internal interface IFunction<Res> {
    @Throws(java.lang.Exception::class)
    fun func(): Res
}
