package com.weebkun.skipdq;

import android.app.Application;

import com.neovisionaries.ws.client.WebSocketFactory;
import com.squareup.moshi.Moshi;
import com.weebkun.skipdq_net.WSClient;

public class SkipDQ extends Application {
    private static final Moshi moshi = new Moshi.Builder().build();
    public static String custId;

    public static Moshi getMoshi() {
        return moshi;
    }
}
