package com.weebkun.speedqueue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.squareup.moshi.Moshi;
import com.weebkun.skipdq_net.Customer;
import com.weebkun.skipdq_net.FoodCourt;
import com.weebkun.skipdq_net.HttpClient;
import com.weebkun.skipdq_net.School;
import com.weebkun.speedqueue.util.ItemAdapter;
import com.weebkun.skipdq_net.util.JWTPayload;
import com.weebkun.skipdq_net.util.JWTReader;

import java.io.IOException;

public class FoodCourtSelectActivity extends SpeedQueueActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_court_select);
        // get user id from payload.json
        try {
            String payload = JWTReader.read("payload.json", this);
            JWTPayload body = new Moshi.Builder().build().adapter(JWTPayload.class).fromJson(payload);
            // get school preference by user id
            HttpClient.get("/user/" + body.id, Customer.class, customer -> {
                // get fcs depending on school
                HttpClient.get("/schools?name=" + customer.school, School.class, school ->
                        HttpClient.get(String.format("/schools/%s/fc", school.id), FoodCourt[].class, foodCourts -> {
                            runOnUiThread(() -> {
                                // set food court list
                                ListView fc = findViewById(R.id.food_courts);
                                fc.setOnItemClickListener((parent, view, pos, id) -> {
                                    // send fc to stall select with fc and school id
                                    startActivity(new Intent(this, StallSelectActivity.class)
                                            .putExtra("fc", ((FoodCourt) parent.getItemAtPosition(pos)).id)
                                            .putExtra("school", school.id));
                                });
                                fc.setAdapter(new ItemAdapter<>(this, foodCourts));
                            });
                        }));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}