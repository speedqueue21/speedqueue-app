package com.weebkun.speedqueue;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.weebkun.speedqueue.util.OrderAdapter;
import com.weebkun.skipdq_net.HttpClient;
import com.weebkun.skipdq_net.Order;
import com.weebkun.skipdq_net.OrderItem;
import com.weebkun.skipdq_net.Stall;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // get orders
        HttpClient.get(String.format("/user/%s/orders", SkipDQ.custId), Order[].class, orders -> {
            // get stall name from stall id
            Map<String, String> stallIdToName = new HashMap<>();
            Map<String, OrderItem[]> orderItems = new HashMap<>();
            for (Order order : orders) {
                AtomicBoolean stallDone = new AtomicBoolean(false);
                AtomicBoolean itemsDone = new AtomicBoolean(false);
                System.out.println(order.id);
                HttpClient.get(String.format("/stall/%s", order.stallId), Stall.class, stall -> {
                    stallIdToName.put(stall.id, stall.name);
                    stallDone.set(true);
                });
                HttpClient.get(String.format("/order/%s/items", order.id), OrderItem[].class, items -> {
                    orderItems.put(order.id, items);
                    itemsDone.set(true);
                });
                // wait until both calls done
                while(!(stallDone.get() && itemsDone.get())) {
                    continue;
                }
            }
            runOnUiThread(() -> {
                // todo display orders
                // adapter
                System.out.println("orders");
                ((ListView) findViewById(R.id.orders_list)).setAdapter(new OrderAdapter(this, orders, stallIdToName, orderItems));
            });
        });
    }
}