package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.ItemOptions;
import com.weebkun.skipdq.net.MenuItem;

import org.w3c.dom.Text;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // gay bug
        // if this is not called, findViewById will return null
        setContentView(R.layout.activity_item);
        //get item id from intent
        //get item options from api
        HttpClient.get(String.format("/item/%s/options",
                getIntent().getStringExtra("item_id")), ItemOptions[].class, options -> {
            // do ui
            runOnUiThread(() -> {
                // get layout
                LinearLayout layout = findViewById(R.id.item_activity);
                TextView title = new TextView(this);
                title.setText(getIntent().getStringExtra("item_name"));
                layout.addView(title);
                for(ItemOptions itemOption : options) {
                    // set option title
                    TextView optionTitle = new TextView(this);
                    optionTitle.setText(itemOption.description);
                    layout.addView(optionTitle);
                    if(itemOption.type.equals("multi")) {
                        // display checkbox for multiple select for each option
                        // get options
                        for(ItemOptions.Option option : itemOption.parseOptions()) {
                            CheckBox cb = new CheckBox(this);
                            cb.setText(String.format("%s $%.2f", option.name, option.price));
                            layout.addView(cb);
                        }
                    } else {
                        // display radio button for single select for each option
                        // create radio group
                        RadioGroup group = new RadioGroup(this);
                        group.setOrientation(RadioGroup.VERTICAL);
                        // get options
                        for(ItemOptions.Option option : itemOption.parseOptions()) {
                            RadioButton rb = new RadioButton(this);
                            rb.setText(String.format("%s $%.2f", option.name, option.price));
                            group.addView(rb);
                        }
                        layout.addView(group);
                    }
                }
                setContentView(layout);
            });
        });
    }
}