package com.example.ppstart;

public class item {
    //basic data structure, formatted in xml format file
    private String name;
    private String price;
    private String image;


    public void setName(String itemName){
        this.name = itemName
    }
    public void setPrice(String itemPrice) {this.price = itemPrice;}
    public void setImage(String itemImage) {this.image = itemImage;}


    public String getName() { return name;}
    public String getPrice() {return price;}
    public String getImage() {return image;}
}