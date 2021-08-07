package com.weebkun.skipdq;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.weebkun.skipdq_net.HttpClient;
import com.weebkun.skipdq_net.Order;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // get orders
        HttpClient.get(String.format("/user/%s/orders", SkipDQ.custId), Order[].class, orders -> {
            runOnUiThread(() -> {
                // todo display orders
            });
        });
    }
}