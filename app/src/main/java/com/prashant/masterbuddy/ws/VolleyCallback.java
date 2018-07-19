package com.prashant.masterbuddy.ws;

/**
 * Created by tanmay.agnihotri on 1/5/18.
 */

public interface VolleyCallback {

    public void starting();
    public void onSuccess(Object objectFromJson);
    public void onSuccess(boolean isSuccess);
    public void onFailure();

}
