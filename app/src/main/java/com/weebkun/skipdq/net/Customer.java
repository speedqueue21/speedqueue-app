package com.weebkun.skipdq.net;

import java.io.Serializable;

public class Customer extends Item implements Serializable {
    public String email;
    public String password;
    public String phone;
    public String school;

    public Customer(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
