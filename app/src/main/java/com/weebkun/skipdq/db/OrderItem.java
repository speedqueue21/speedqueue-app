package com.weebkun.skipdq.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.weebkun.skipdq_net.Item;

@Entity(tableName = "order_item")
public class OrderItem extends Item {
    @PrimaryKey(autoGenerate = true)
    public int item;
    public String item_id;
    public String stall_id;
    public int quantity;
    public String selections;
    public double amount;

    public OrderItem(){}

    public OrderItem(String item_id, String stall_id, String name, int quantity, String selections, double amount) {
        this.item_id = item_id;
        this.stall_id = stall_id;
        this.name = name;
        this.quantity = quantity;
        this.selections = selections;
        this.amount = amount;
    }
}
