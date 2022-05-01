package com.example.ppstart;

public class item {
    //basic data structure, formatted in xml format file
    private String name;
    private int ownerID;
    private float price;
    private String itemNumber;


    public void setOwnerId(int ownerID) {this.ownerID = ownerID;}
    public void setName(String itemName){
        this.name = itemName;
    }
    public void setPrice(float itemPrice) {this.price = itemPrice;}
    public void setItemNumber(String itemImage) {this.itemNumber = itemImage;}


    public int getOwnerId() {return ownerID;}
    public String getName() { return name;}
    public float getPrice() {return price;}
    public String getItemNumber() {return itemNumber;}
}