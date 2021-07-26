package com.weebkun.skipdq;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.weebkun.skipdq.db.CartDatabase;
import com.weebkun.skipdq.db.OrderItem;
import com.weebkun.skipdq.db.Selections;
import com.weebkun.skipdq_net.HttpClient;
import com.weebkun.skipdq_net.ItemOptions;
import com.weebkun.skipdq_net.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    private MenuItem item = null;
    private final List<String> selections = new ArrayList<>();
    private double total = 0;
    private RadioButton prevButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // gay bug
        // if this is not called, findViewById will return null
        setContentView(R.layout.activity_item);
        //get item
        HttpClient.get(String.format("/item/%s",
                //get item id from intent
                getIntent().getStringExtra("item_id")), MenuItem.class, it -> item = it);
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
                            LinearLayout optionParent = (LinearLayout) getLayoutInflater().inflate(R.layout.item_option, null);
                            ((TextView)optionParent.findViewById(R.id.option_name)).setText(option.name);
                            ((TextView)optionParent.findViewById(R.id.option_price)).setText(String.format("$%.2f", option.price));
                            ((TextView)optionParent.findViewById(R.id.option_price_real)).setText(Double.toString(option.price));
                            CheckBox cb = new CheckBox(this);
                            cb.setOnCheckedChangeListener((button, checked) -> {
                                if(checked) {
                                    selections.add(((TextView)((ViewGroup) button.getParent()).findViewById(R.id.option_name)).getText().toString());
                                    total += Double.parseDouble(((TextView)((ViewGroup) button.getParent()).findViewById(R.id.option_price_real)).getText().toString());
                                }
                                else {
                                    selections.remove(((TextView)((ViewGroup) button.getParent()).findViewById(R.id.option_name)).getText().toString());
                                    total -= Double.parseDouble(((TextView)((ViewGroup) button.getParent()).findViewById(R.id.option_price_real)).getText().toString());
                                }
                            });
                            optionParent.addView(cb, 0);
                            layout.addView(optionParent);
                        }
                    } else {
                        // display radio button for single select for each option
                        int i = 1;
                        // get options
                        for(ItemOptions.Option option : itemOption.parseOptions()) {
                            // create parent layout
                            LinearLayout optionParent = (LinearLayout) getLayoutInflater().inflate(R.layout.item_option, null);
                            ((TextView) optionParent.findViewById(R.id.option_name)).setText(option.name);
                            ((TextView) optionParent.findViewById(R.id.option_price)).setText(String.format("$%.2f", option.price));
                            ((TextView)optionParent.findViewById(R.id.option_price_real)).setText(Double.toString(option.price));
                            RadioButton rb = new RadioButton(this);
                            rb.setOnCheckedChangeListener((view, checked) -> {
                                if(!checked) {
                                    selections.remove(((TextView)((ViewGroup) view.getParent()).findViewById(R.id.option_name)).getText().toString());
                                    total -= Double.parseDouble(((TextView)((ViewGroup) view.getParent()).findViewById(R.id.option_price_real)).getText().toString());
                                    return;
                                }
                                if(prevButton != null) {
                                    prevButton.setChecked(false);
                                }
                                selections.add(((TextView)((ViewGroup) view.getParent()).findViewById(R.id.option_name)).getText().toString());
                                total += Double.parseDouble(((TextView)((ViewGroup) view.getParent()).findViewById(R.id.option_price_real)).getText().toString());
                                prevButton = (RadioButton) view;
                            });
                            rb.setId(i);
                            optionParent.addView(rb, 0);
                            layout.addView(optionParent);
                            i++;
                        }
                    }
                }
                // quantity
                EditText quantity = new EditText(this);
                quantity.setId(R.id.quantity);
                quantity.setHint("Quantity");
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
                item.stall_id,
                getIntent().getStringExtra("item_name"),
                Integer.parseInt(((EditText) findViewById(R.id.quantity)).getText().toString()),
                SkipDQ.getMoshi().adapter(Selections.class).toJson(new Selections(selections.toArray(new String[]{}), total)),
                item.price * Integer.parseInt(((EditText) findViewById(R.id.quantity)).getText().toString())
        ))).start();
        finish();
    }
}