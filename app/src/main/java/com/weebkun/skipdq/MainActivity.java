package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.squareup.moshi.Moshi;
import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.TokenResponse;
import com.weebkun.skipdq.util.JWTReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if got token
        try {
            String token = JWTReader.read("token.json", this);
            if(!token.isEmpty()) {
                // token not empty
                HttpClient.authorise(this, new Moshi.Builder().build().adapter(TokenResponse.class).fromJson(token).access);
                startActivity(new Intent(this, FoodCourtSelectActivity.class));
            }
        } catch (IOException e) {
            // assume don't have
            setContentView(R.layout.activity_main);
        }
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}