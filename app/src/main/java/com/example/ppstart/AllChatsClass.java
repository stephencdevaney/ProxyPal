//CREATED BY BLAKE

package com.example.ppstart;

public class AllChatsClass {
   private int owner_id;
   private int supporter_id;
   private String business_pic;
   private String supporter_username;
   private String owner_username;
   private String business_name;


    public AllChatsClass() {
    }

    public AllChatsClass(int owner_id, int supporter_id, String owner_pic, String supporter_username, String owner_username, String business_name) {
        this.owner_id = owner_id;
        this.supporter_id = supporter_id;
        this.business_pic = owner_pic;
        this.supporter_username = supporter_username;
        this.owner_username = owner_username;
        this.business_name = business_name;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getSupporter_id() {
        return supporter_id;
    }

    public void setSupporter_id(int supporter_id) {
        this.supporter_id = supporter_id;
    }

    public String getBusiness_pic() {
        return business_pic;
    }

    public void setBusiness_pic(String owner_pic) {
        this.business_pic = owner_pic;
    }

    public String getSupporter_username() {
        return supporter_username;
    }

    public void setSupporter_username(String supporter_username) {
        this.supporter_username = supporter_username;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }
}
