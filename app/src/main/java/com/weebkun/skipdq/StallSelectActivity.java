package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.Item;
import com.weebkun.skipdq.net.Stall;
import com.weebkun.skipdq.util.ItemAdapter;

public class StallSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall_select);

        //get stalls
        HttpClient.get(String.format("/schools/%s/fc/%s/stalls",
                getIntent().getStringExtra("school"),
                getIntent().getStringExtra("fc")), Stall[].class, stalls -> {
            runOnUiThread(() -> {
                ListView listView = findViewById(R.id.stalls);
                listView.setOnItemClickListener((parent, view, pos, id) -> {
                    // get fc and go to menu
                    startActivity(new Intent(this, MenuActivity.class)
                            .putExtra("stall", ((Stall) parent.getItemAtPosition(pos)).id)
                            .putExtra("school", getIntent().getStringExtra("school"))
                            .putExtra("fc", getIntent().getStringExtra("fc")))
                    ;
                });
                listView.setAdapter(new ItemAdapter<>(this, stalls));
            });
        });
    }
}