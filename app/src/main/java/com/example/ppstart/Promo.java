package com.example.ppstart;

public class Promo {
    private String dp_id;
    private String store_id;
    private String item_id;
    private String store_name;
    private byte[] item_image;
    private String dp_desc;
    private String item_name;

    public Promo(String dp_id, String store_id, String item_id, String store_name, byte[] item_image, String dp_desc, String item_name) {
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

    public String getDp_id() {
        return dp_id;
    }

    public void setDp_id(String dp_id) {
        this.dp_id = dp_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
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
