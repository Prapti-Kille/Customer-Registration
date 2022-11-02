package com.example.customerregistration;

public class Customer {
    int id;
    String name, platform, joiningDate;
    int contact;

    public Customer(int id, String name, String platform, String joiningDate, int contact) {
        this.id = id;
        this.name = name;
        this.platform = platform;
        this.joiningDate = joiningDate;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlatform() {
        return platform;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public int getContact() {
        return contact;
    }
}
