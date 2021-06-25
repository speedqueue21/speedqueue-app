package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.squareup.moshi.Moshi;
import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.TokenResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;

import at.favre.lib.crypto.bcrypt.BCrypt;

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
                    try(OutputStreamWriter writer = new OutputStreamWriter(openFileOutput("token.json", Context.MODE_PRIVATE))) {
                        writer.write(response.body().string());
                    }
                    System.out.println("success login");
                    //TokenResponse token = new Moshi.Builder().build().adapter(TokenResponse.class).fromJson(response.body().source());
                    // go to food court select
                    startActivity(new Intent(this, FoodCourtSelectActivity.class).putExtra("school", "SP"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}