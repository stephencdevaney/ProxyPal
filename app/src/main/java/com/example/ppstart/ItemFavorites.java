//CREATED BY BLAKE

package com.example.ppstart;

public class ItemFavorites {
    private int item_id;
    private int owner_id;
    private int supporter_id;
    private int profile_id;

    public ItemFavorites() {
    }

    public ItemFavorites(int item_id, int owner_id, int supporter_id, int profile_id) {
        this.item_id = item_id;
        this.owner_id = owner_id;
        this.supporter_id = supporter_id;
        this.profile_id = profile_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
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

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }
}
