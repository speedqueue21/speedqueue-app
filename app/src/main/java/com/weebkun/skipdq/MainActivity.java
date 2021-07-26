package com.weebkun.skipdq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.moshi.Moshi;
import com.weebkun.skipdq.db.CartDatabase;
import com.weebkun.skipdq_net.HttpClient;
import com.weebkun.skipdq_net.TokenResponse;
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
                // test token
                get("/", response -> {
                    if(!response.isSuccessful()) {
                        // token expired
                        // refresh token
                        try {
                            TokenResponse tokenResponse = new Moshi.Builder().build().adapter(TokenResponse.class)
                                    .fromJson(JWTReader.read("token.json", this));
                            SkipDQ.custId = tokenResponse.id;
                            System.out.println(SkipDQ.custId);
                            HttpClient.refresh(this, tokenResponse.id, tokenResponse.refresh, () -> startActivity(new Intent(this, FoodCourtSelectActivity.class)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        startActivity(new Intent(this, FoodCourtSelectActivity.class));
                    }
                });
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