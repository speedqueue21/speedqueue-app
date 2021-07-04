package com.weebkun.skipdq.net;

import com.squareup.moshi.Moshi;

import java.io.IOException;

public class ItemOptions {
    public int id;
    public String description;
    public String type;
    public boolean optional;
    public int min;
    public int max;
    public String options;
    public String item_id;

    public Option[] parseOptions() {
        try {
            return new Moshi.Builder().build().adapter(Option[].class).fromJson(options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class Option {
        public String name;
        public double price;
    }
}
