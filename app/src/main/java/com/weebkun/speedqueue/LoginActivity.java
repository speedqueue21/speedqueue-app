package com.weebkun.speedqueue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.moshi.Moshi;
import com.weebkun.skipdq_net.HttpClient;
import com.weebkun.skipdq_net.TokenResponse;
import com.weebkun.skipdq_net.WSClient;
import com.weebkun.skipdq_net.util.JWTWriter;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        HttpClient.post("/login/customer", String.format("{" +
                "\"email\": \"%s\"," +
                "\"password\": \"%s\"" +
                "}", ((EditText) findViewById(R.id.email)).getText().toString(),
                ((EditText) findViewById(R.id.password)).getText().toString()), response -> {
            if(response.isSuccessful()) {
                // login successful
                try {
                    // get tokens
                    String res = response.body().string();
                    TokenResponse tokenResponse = new Moshi.Builder().build().adapter(TokenResponse.class).fromJson(res);
                    SkipDQ.custId = tokenResponse.id;
                    // write to files
                    JWTWriter.writeTokenToFile(this, res, tokenResponse);
                    // add token to http client
                    HttpClient.authorise(tokenResponse.access);
                    // connect to websocket
                    WSClient.getClient().connect();
                    // go to food court select
                    startActivity(new Intent(this, FoodCourtSelectActivity.class).putExtra("school", "SP"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}