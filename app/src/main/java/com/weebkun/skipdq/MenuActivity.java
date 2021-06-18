package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //set menu
        String[] items = {"item 1", "item 2", "item 3"};
        ListView menu = findViewById(R.id.menu_items);
        menu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }
}