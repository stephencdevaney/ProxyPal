package com.example.ppstart;

//Naming Scheme: dp = discount/promo

public class Promo {
    private int dp_id;
    private int store_id;
    private int item_id;
    private String store_name;
    private byte[] item_image;
    private String dp_desc;
    private String item_name;

    public Promo(int dp_id, int store_id, int item_id, String store_name, byte[] item_image, String dp_desc, String item_name) {
        this.dp_id = dp_id;
        this.store_id = store_id;
        this.item_id = item_id;
        this.store_name = store_name;
        this.item_image = item_image;
        this.dp_desc = dp_desc;
        this.item_name = item_name;
    }

    public Promo(){
        //Empty constructor
    }

    public int getDp_id() {
        return dp_id;
    }

    public void setDp_id(int dp_id) {
        this.dp_id = dp_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public byte[] getItem_image() {
        return item_image;
    }

    public void setItem_image(byte[] item_image) {
        this.item_image = item_image;
    }

    public String getDp_desc() {
        return dp_desc;
    }

    public void setDp_desc(String dp_desc) {
        this.dp_desc = dp_desc;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
