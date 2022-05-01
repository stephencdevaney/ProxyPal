package com.example.ppstart;

public class item {
    //basic data structure, formatted in xml format file
    private String name;
    private int ownerID;
    private String price;
    private int itemNumber;

    public item() {
        //empty constructor
    }

    public item(String name, int ownerID, String price, int itemNumber) {
        this.name = name;
        this.ownerID = ownerID;
        this.price = price;
        this.itemNumber = itemNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }
}