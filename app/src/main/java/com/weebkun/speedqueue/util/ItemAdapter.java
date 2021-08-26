package com.weebkun.speedqueue.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.weebkun.speedqueue.R;
import com.weebkun.skipdq_net.Item;

public class ItemAdapter<T extends Item> extends BaseAdapter {

    private Context context;
    private T[] items;

    public ItemAdapter(@NonNull Context context, @NonNull T[] objects) {
        this.context = context;
        this.items = objects;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // inflate view
        if(convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);

        // get item and set text views
        Item item = (Item) getItem(position);
        if(item != null) {
            ((TextView) convertView.findViewById(R.id.text)).setText(item.name);
            ((TextView) convertView.findViewById(R.id.id)).setText(item.id);
        }
        return convertView;
    }
}
