package com.weebkun.skipdq.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weebkun.skipdq.R;
import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.MenuItem;

public class ImageAdapter extends BaseAdapter {

    public Context context;
    public MenuItem[] items;

    public ImageAdapter(Context context, MenuItem[] items) {
        this.context = context;
        this.items = items;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);

        MenuItem item = (MenuItem) getItem(position);
        if(item != null) {
            Picasso.get().load(String.format("%s/item_img/%s.jpg", HttpClient.getRoot(), item.image))
                    .into((ImageView) convertView.findViewById(R.id.image));
            ((TextView) convertView.findViewById(R.id.item)).setText(item.name);
        }
        return convertView;
    }
}
