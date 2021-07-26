package com.weebkun.skipdq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.moshi.Moshi;
import com.weebkun.skipdq_net.Customer;
import com.weebkun.skipdq_net.HttpClient;

public class RegisterSchoolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);

        // set dropdown
        String[] schools = {"SP", "NYP"};
        Spinner dropdown = findViewById(R.id.school);
        ArrayAdapter<String> adapter =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
    }

    public void submit(View view) {
        // get user from previous step
        Customer cust = (Customer) getIntent().getSerializableExtra("user");
        // set school preference
        cust.school = ((Spinner) findViewById(R.id.school)).getSelectedItem().toString();
        // create new customer
        HttpClient.post("/register", new Moshi.Builder().build().adapter(Customer.class).toJson(cust));
        // go to fc select
        startActivity(new Intent(this, FoodCourtSelectActivity.class));
    }
}