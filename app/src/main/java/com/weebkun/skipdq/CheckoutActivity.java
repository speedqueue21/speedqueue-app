package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.weebkun.skipdq.db.CartDatabase;
import com.weebkun.skipdq.db.OrderItem;
import com.weebkun.skipdq.util.ItemAdapter;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        new Thread(() -> {
            OrderItem[] orderItems = CartDatabase.getDatabase(this).getDao().getItems();
            runOnUiThread(() -> {
                // set order items
                ListView items = findViewById(R.id.items);
                items.setAdapter(new ItemAdapter<>(this, orderItems));
                double total = 0;
                for(OrderItem item : orderItems) {
                    total += item.amount;
                }
                ((TextView) findViewById(R.id.total)).setText(String.format("%.2f", total));
            });
        }).start();
    }

    public void checkout(View view) {

    }
}