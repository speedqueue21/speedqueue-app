package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.weebkun.skipdq.db.CartDatabase;

public class SkipDqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(() -> {
            CartDatabase.getDatabase(this).getDao().clearCart();
            CartDatabase.getDatabase(this).getDao().resetId();
        }).start();
    }
}