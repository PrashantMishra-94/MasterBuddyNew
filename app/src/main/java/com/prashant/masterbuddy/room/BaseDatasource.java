package com.prashant.masterbuddy.room;

import android.content.Context;

public abstract class BaseDatasource {

    protected AppDatabase database;
    protected Context context;

    public BaseDatasource(AppDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }
}
