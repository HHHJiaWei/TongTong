package com.example.a44261.bysj102.IM.model;

import android.content.Context;

import com.example.a44261.bysj102.IM.ImApplication;

public abstract class BaseModel {

    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    public Context getContext(){
        return ImApplication.INSTANCE();
    }
}
