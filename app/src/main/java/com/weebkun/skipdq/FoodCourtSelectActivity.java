package com.weebkun.skipdq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.weebkun.skipdq.net.FoodCourt;
import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.School;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FoodCourtSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_court_select);
        // get fcs depending on school
        HttpClient.get("/schools?name=" + getIntent().getStringExtra("school"), School.class, school ->
                HttpClient.get(String.format("/schools/%s/fc", school.id), FoodCourt[].class, foodCourts -> {
                    List<String> fcs = Arrays.stream(foodCourts).map(foodCourt -> foodCourt.name).collect(Collectors.toList());
                    runOnUiThread(() -> {
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
                    });
                }));
    }
}