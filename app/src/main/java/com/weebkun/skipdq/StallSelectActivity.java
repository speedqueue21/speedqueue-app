package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StallSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall_select);

        //set stalls
        String[] stallList = {"stall 1", "stall 2", "stall 3", "stall 4"};
        ListView stalls = findViewById(R.id.stalls);
        stalls.setOnItemClickListener((parent, view, pos, id) -> {
            // get item and go to stall select
            Intent menu = new Intent(this, MenuActivity.class);
            // send fc to menu
            // todo get id of stall
            menu.putExtra("stall", parent.getItemAtPosition(pos).toString());
            startActivity(menu);
        });
        stalls.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stallList));
    }
}