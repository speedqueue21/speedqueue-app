package com.weebkun.skipdq;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.moshi.Moshi;
import com.weebkun.skipdq.net.Customer;
import com.weebkun.skipdq.net.FoodCourt;
import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.School;
import com.weebkun.skipdq.util.ItemAdapter;
import com.weebkun.skipdq.util.JWTPayload;
import com.weebkun.skipdq.util.JWTReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FoodCourtSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_court_select);
        // get user id from payload.json
        try {
            String payload = JWTReader.read("payload.json", this);
            System.out.println(payload);
            JWTPayload body = new Moshi.Builder().build().adapter(JWTPayload.class).fromJson(payload);
            // get school preference by user id
            System.out.println(body.id);
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