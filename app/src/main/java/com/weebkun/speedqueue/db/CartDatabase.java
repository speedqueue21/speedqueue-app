package com.weebkun.speedqueue.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {OrderItem.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {
    private static CartDatabase db = null;

    public abstract CartDao getDao();

    public synchronized static CartDatabase getDatabase(Context context) {
        synchronized(CartDatabase.class) {
            if(db == null) db = Room.databaseBuilder(context, CartDatabase.class, "cart").build();
            return db;
        }
    }
}
