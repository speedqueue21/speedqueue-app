package com.weebkun.speedqueue.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weebkun.speedqueue.R;
import com.weebkun.skipdq_net.Order;
import com.weebkun.skipdq_net.OrderItem;

import java.io.IOException;
import java.util.Map;

/**
 * adapter for displaying orders in the order status screen
 */
public class OrderAdapter extends BaseAdapter {
    public Context context;
    public Order[] orders;
    public Map<String, String> stallIdToName;
    public Map<String, OrderItem[]> orderItems;

    public OrderAdapter(Context context, Order[] orders, Map<String, String> stallIdToName, Map<String, OrderItem[]> orderItems) {
        this.context = context;
        this.orders = orders;
        this.stallIdToName = stallIdToName;
        this.orderItems = orderItems;
    }

    @Override
    public int getCount() {
        return orders.length;
    }

    @Override
    public Object getItem(int position) {
        return orders[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // do not use convertView
        // breaks everything
        View view = LayoutInflater.from(context).inflate(R.layout.orders, parent, false);

        Order order = orders[position];
        // populate text views
        ((TextView) view.findViewById(R.id.stall_name)).setText(stallIdToName.get(order.stallId));
        ((TextView) view.findViewById(R.id.order_no)).setText(Integer.toString(order.code));
        ((TextView) view.findViewById(R.id.status)).setText(order.status);
        // populate food items
        LinearLayout ll = view.findViewById(R.id.food_items);
        // passed
        System.out.println(orderItems.get(order.id).length);
        for(OrderItem item : orderItems.get(order.id)) {
            System.out.println(item.id); // here still fine
            RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.order_items, ll, false);
            try {
                ((TextView) layout.findViewById(R.id.food_name)).setText(item.name);
                // parse selections json to comma separated string
                OrderItem.Selections selections = item.parseSelections();
                ((TextView) layout.findViewById(R.id.selections)).setText(selections.selections.length != 0 ? String.join(", ", selections.selections) : "No Selection");
                // calculate total price
                ((TextView) layout.findViewById(R.id.price)).setText(String.format("$%.2f", item.amount + selections.price));
                ll.addView(layout);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}
