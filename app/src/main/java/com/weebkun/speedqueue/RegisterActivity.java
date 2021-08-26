package com.weebkun.speedqueue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.weebkun.skipdq_net.Customer;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void next(View view) {
        EditText name = findViewById(R.id.name);
        name.getText();
        // go to school select
        startActivity(new Intent(this, RegisterSchoolActivity.class).putExtra("user", new Customer(
                ((EditText) findViewById(R.id.name)).getText().toString(),
                ((EditText) findViewById(R.id.email)).getText().toString(),
                BCrypt.withDefaults().hashToString(10, ((EditText) findViewById(R.id.password)).getText().toString().toCharArray()),
                ((EditText) findViewById(R.id.phone)).getText().toString()
        )));
    }
}