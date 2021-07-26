package com.weebkun.skipdq.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CartDao {
    @Insert
    void addItem(OrderItem item);

    @Query("select * from order_item")
    OrderItem[] getItems();

    @Query("delete from order_item where item_id = :id")
    void removeItem(String id);

    @Update
    void updateItem(OrderItem item);

    @Query("delete from order_item where 1=1")
    void clearCart();

    @Query("delete from sqlite_sequence where name = 'order_item'")
    void resetId();
}
