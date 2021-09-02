package com.weebkun.speedqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.weebkun.speedqueue.db.CartDatabase;
import com.weebkun.speedqueue.db.OrderItem;
import com.weebkun.speedqueue.db.Selections;
import com.weebkun.speedqueue.util.ItemAdapter;
import com.weebkun.skipdq_net.HttpClient;

import java.io.IOException;

public class CheckoutActivity extends AppCompatActivity {

    OrderItem[] items;
    String stall_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        // set dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Wallet", "Paynow"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.payment_method)).setAdapter(adapter);
        new Thread(() -> {
            OrderItem[] orderItems = CartDatabase.getDatabase(this).getDao().getItems();
            items = orderItems;
            runOnUiThread(() -> {
                // set order items
                ListView items = findViewById(R.id.items);
                items.setAdapter(new ItemAdapter<>(this, orderItems));
                double total = 0;
                for(OrderItem item : orderItems) {
                    if(stall_id == null) stall_id = item.stall_id;
                    total += item.amount;
                    // get selections
                    try {
                        total += SkipDQ.getMoshi().adapter(Selections.class).fromJson(item.selections).price * item.quantity;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ((TextView) findViewById(R.id.total)).setText(String.format("Total: $%.2f", total));
            });
        }).start();
    }

    public void checkout(View view) {
        // create new order
        HttpClient.post("/order", String.format("{" +
                "\"cust_id\":\"%s\"," +
                "\"stall_id\":\"%s\"," +
                "\"payment\":\"%s\"," +
                "\"items\": %s"+
                "}", SkipDQ.custId,
                stall_id,
                ((Spinner) findViewById(R.id.payment_method)).getSelectedItem().toString(),
                SkipDQ.getMoshi().adapter(OrderItem[].class).toJson(items)));
        startActivity(new Intent(this, OrdersActivity.class));
    }
}