package com.weebkun.speedqueue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.moshi.Moshi;
import com.weebkun.speedqueue.db.CartDatabase;
import com.weebkun.skipdq_net.HttpClient;
import com.weebkun.skipdq_net.TokenResponse;
import com.weebkun.skipdq_net.WSClient;
import com.weebkun.skipdq_net.util.JWTReader;

import java.io.IOException;

import static com.weebkun.skipdq_net.HttpClient.get;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // clear cart
        new Thread(() -> {
            CartDatabase.getDatabase(this).getDao().clearCart();
            CartDatabase.getDatabase(this).getDao().resetId();
        }).start();
        // check if got token
        try {
            String token = JWTReader.read("token.json", this);
            if(!token.isEmpty()) {
                // token not empty
                HttpClient.authorise(new Moshi.Builder().build().adapter(TokenResponse.class).fromJson(token).access);
                TokenResponse tokenResponse = new Moshi.Builder().build().adapter(TokenResponse.class)
                        .fromJson(token);
                // test token
                SkipDQ.custId = tokenResponse.id;
                get("/", response -> {
                    if(!response.isSuccessful()) {
                        // token expired
                        // refresh token
                        HttpClient.refresh(this, tokenResponse.id, tokenResponse.refresh, () -> startActivity(new Intent(this, FoodCourtSelectActivity.class)));
                    } else {
                        startActivity(new Intent(this, FoodCourtSelectActivity.class));
                    }
                    WSClient.getClient().connect();
                });
            }
        } catch (IOException e) {
            setContentView(R.layout.activity_main);
        }
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void orders(View view) {
        startActivity(new Intent(this, OrdersActivity.class));
    }
}