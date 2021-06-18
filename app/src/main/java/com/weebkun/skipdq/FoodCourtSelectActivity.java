package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FoodCourtSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_court_select);
        // hard code fcs
        String[] fcs = {"fc 1", "fc 2", "fc 3", "fc 4", "fc 5", "fc 6"};
        // set food court list
        ListView fc = findViewById(R.id.food_courts);
        fc.setOnItemClickListener((parent, view, pos, id) -> {
            // get item and go to stall select
            Intent stallSelect = new Intent(this, StallSelectActivity.class);
            // send fc to stall select
            // todo get id of fc
            stallSelect.putExtra("fc", parent.getItemAtPosition(pos).toString());
            startActivity(stallSelect);
        });
        fc.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fcs));
    }
}