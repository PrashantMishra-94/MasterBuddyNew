package com.prashant.masterbuddy.ws;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prashant.masterbuddy.Constants;
import com.prashant.masterbuddy.ws.model.UserResult;
import com.prashant.masterbuddy.ws.model.RegisterRequest;
import com.prashant.masterbuddy.ws.model.RegisterResult;
import com.prashant.masterbuddy.ws.model.SelectVideoResponse;
import com.prashant.masterbuddy.ws.model.SelectVideosRequest;
import com.prashant.masterbuddy.ws.model.DataPart;
import com.prashant.masterbuddy.ws.model.UserRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class JsonServices {

    private final VolleyCallback volleyCallback;
    private final Context mContext;
    private String strResponse;
    private GsonBuilder builder = new GsonBuilder();

    public JsonServices(Context context, VolleyCallback volleyCallback) {
        super();
        this.mContext = context;
        this.volleyCallback = volleyCallback;
    }


    public <T> T createObjectFromJson(String response, Type returnType) {

        Gson gson = builder.create();
        T result = gson.fromJson(response, returnType);

        return result;

    }

    public void executeRequest(String url, String json, final Type returnType) {
        Log.d("Test!!!!!!", "json:" + json);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("JSON", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, jsonObject,

                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        Log.d("Response from volley ", "" + response);
                        strResponse = String.valueOf(response);
                        volleyCallback.onSuccess(createObjectFromJson(strResponse, returnType));
                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JsonService", "Error Status code: " + error.networkResponse.statusCode);
                error.printStackTrace();
                volleyCallback.onFailure();

            }

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjectRequest);
        volleyCallback.starting();

    }

    public void executeVideoRequest(final Uri selectedPath, final String title, final String description, final int userId) {
        VolleyMultipartRequest jsonObjectRequest = new VolleyMultipartRequest(com.android.volley.Request.Method.POST, "http://masterbuddy.info/video/upload",

                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            //Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            volleyCallback.onSuccess(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        volleyCallback.onFailure();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("video", new DataPart(imagename + ".mp4", convertFileToByteArray(selectedPath)));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("description", description);
                params.put("userId", "25");
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                2000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Access the RequestQueue through your singleton class.
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
        volleyCallback.starting();
    }

    private byte[] convertFileToByteArray(Uri selectedPath) {
        byte[] buf = new byte[0];
        try {
            FileInputStream in = (FileInputStream) mContext.getContentResolver().openInputStream(selectedPath);
            assert in != null;
            buf = IOUtils.toByteArray(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }

    public void doLogin(String username, String password) {
        UserRequest userRequest = new UserRequest(username, password);
        Gson gson = new Gson();
        String json = gson.toJson(userRequest);
        executeRequest("http://masterbuddy.info/User/Login", json, UserResult.class);
    }

    public void registerUser(String name, String emailId, String Password, String currentDate) {
        RegisterRequest registerRequest = new RegisterRequest(name, emailId, Password, currentDate);
        Gson gson = new Gson();
        String json = gson.toJson(registerRequest);
        executeRequest("http://masterbuddy.info/User/Register", json, RegisterResult.class);
    }

    public void getVideoList(int startIndex, int type) {
        SelectVideosRequest selectVideosRequest = new SelectVideosRequest();
        selectVideosRequest.setStartIndex(startIndex);
        selectVideosRequest.setLastIndex(startIndex + 20);
        Gson gson = new Gson();
        String json = gson.toJson(selectVideosRequest);
        String homeURL = "http://masterbuddy.info/File/SelectVideos";
        String trendingURL = "http://masterbuddy.info/File/SelectTrendingVideos";
        executeRequest(type == Constants.LIST_TYPE_TRENDING ? trendingURL : homeURL, json, SelectVideoResponse.class);
    }
}