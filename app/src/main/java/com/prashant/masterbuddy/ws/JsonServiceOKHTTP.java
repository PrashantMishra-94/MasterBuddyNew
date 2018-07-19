package com.prashant.masterbuddy.ws;

import android.content.Context;
import android.util.Log;

import com.prashant.masterbuddy.utils.Utils;
import com.prashant.masterbuddy.ws.model.RegisterRequest;
import com.prashant.masterbuddy.ws.model.RegisterResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JsonServiceOKHTTP {

    private static final String TAG = "JsonServiceOKHTTP";

    private final VolleyCallback volleyCallback;
    private final Context mContext;
    private GsonBuilder builder = new GsonBuilder();
    private static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    public JsonServiceOKHTTP(Context mContext, VolleyCallback volleyCallback) {
        this.volleyCallback = volleyCallback;
        this.mContext = mContext;
    }

    private <T> void executeAsync(final IFunction<T> func) {
        new android.os.AsyncTask<Void, Void, OperationResult<T>>() {
            @Override
            protected void onPreExecute() {
                if (Utils.isConnectingToInternet(mContext, true)) {
                    volleyCallback.starting();
                } else {
                    cancel(true);
                }
            }

            @Override
            protected OperationResult<T> doInBackground(Void... params) {
                OperationResult<T> result = new OperationResult<T>();
                try {
                    result.Result = func.Func();
                } catch (java.lang.Exception ex) {
                    ex.printStackTrace();
                    result.Exception = ex;
                    Log.e(TAG, ex.toString());
                }
                return result;
            }

            @Override
            protected void onPostExecute(OperationResult<T> result) {
                Log.d("TEST_LOG", result == null ? "NULL Object" : " : " + new Gson().toJson(result));
                if (result != null && result.Result != null) {
                    volleyCallback.onSuccess(result.Result);
                } else {
                    volleyCallback.onFailure();
                }
            }
        }.execute();
    }

    private <T> T executeRequest(String url, String json, Type returnType) {
        Log.d("Test!!!!!!", "json:" + json);
        T result = null;
        int maxLogSize = 10000;
        int responseCode = 0;
        String responseMessage = "";
        try {
            RequestBody body = null;
            if (json != null) {
                body = RequestBody.create(JSON, json);
            }
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            responseCode = response.code();
            responseMessage = response.message();
            Log.d("TestJSON", jsonResponse);
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
            builder.serializeNulls();
            Gson gson = builder.create();

            result = gson.fromJson(jsonResponse, returnType);

            if (result == null) {
                throw new NullPointerException("Response null exception");
            }
        } catch (Exception e) {
            String message = "executeRequest Exception: "+ e.toString() + " ResponseCode: " +responseCode + " Response Message: "
                    + responseMessage + " URL: "+ url + " JSON: " + json;
            Log.d(TAG, message);
        }
        return result;
    }

    public void registerUserAsync(String name, String emailId, String password, String currentDate) {
        executeAsync(() -> registerUser(name, emailId, password, currentDate));
    }

    private RegisterResult registerUser(String name, String emailId, String Password, String currentDate) {
        RegisterRequest registerRequest = new RegisterRequest(name, emailId, Password, currentDate);
        Gson gson = new Gson();
        String json = gson.toJson(registerRequest);
        return executeRequest("http://masterbuddy.info/User/Register", json, RegisterResult.class);
    }

}

class OperationResult<T> {
    public java.lang.Exception Exception;
    public T Result;
    public java.lang.Object Tag;
}

interface IFunction<Res> {
    Res Func() throws java.lang.Exception;
}
