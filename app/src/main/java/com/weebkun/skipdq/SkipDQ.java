package com.weebkun.skipdq;

import android.app.Application;

import com.squareup.moshi.Moshi;

public class SkipDQ extends Application {
    private static final Moshi moshi = new Moshi.Builder().build();
}
