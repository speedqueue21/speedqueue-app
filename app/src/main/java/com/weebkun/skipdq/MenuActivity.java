package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.MenuItem;
import com.weebkun.skipdq.util.ImageAdapter;
import com.weebkun.skipdq.util.ItemAdapter;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //get menu
        HttpClient.get(String.format("/schools/%s/fc/%s/stalls/%s/menu",
                getIntent().getStringExtra("school"),
                getIntent().getStringExtra("fc"),
                getIntent().getStringExtra("stall")), MenuItem[].class,
                menuItems -> runOnUiThread(() -> {
                    ListView menu = findViewById(R.id.menu_items);
                    menu.setOnItemClickListener((parent, view, position, id) -> {

                    });
                    menu.setAdapter(new ImageAdapter(this, menuItems));
                }));
    }
}