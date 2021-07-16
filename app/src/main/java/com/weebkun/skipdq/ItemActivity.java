package com.weebkun.skipdq;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weebkun.skipdq.db.CartDatabase;
import com.weebkun.skipdq.db.OrderItem;
import com.weebkun.skipdq.net.HttpClient;
import com.weebkun.skipdq.net.ItemOptions;

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
                    // todo options validation
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
                // quantity
                EditText quantity = new EditText(this);
                quantity.setId(R.id.quantity);
                quantity.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                quantity.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                layout.addView(quantity);
                // set button
                Button button = new Button(this);
                button.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                button.setOnClickListener(this::add);
                // for now just enable the button
                // i will do the validation next time
                button.setEnabled(true);
                button.setBackgroundColor(Color.BLUE);
                button.setTextColor(Color.WHITE);
                button.setText("Add to cart");
                layout.addView(button);
                setContentView(layout);
            });
        });
    }

    protected void add(View view) {
        // add to cart
        System.out.println(CartDatabase.getDatabase(this).isOpen());
        new Thread(() -> CartDatabase.getDatabase(this).getDao().addItem(new OrderItem(getIntent().getStringExtra("item_id"),
                getIntent().getStringExtra("item_name"),
                Integer.parseInt(((EditText) findViewById(R.id.quantity)).getText().toString()),
                "",
                0
        ))).start();
        finish();
    }
}