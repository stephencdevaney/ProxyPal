//CREATED BY BLAKE

package com.example.ppstart;

//This class is used to store the "link" between a supporter account and a business it favorited using the id's of the respective accounts
public class ProfileFavorites {
    private int prof_fav_supporter_id, prof_fav_owner_id, prof_fav_profile_id;

    public ProfileFavorites(int prof_fav_supporter_id, int prof_fav_owner_id, int prof_fav_profile_id) {
        this.prof_fav_supporter_id = prof_fav_supporter_id;
        this.prof_fav_owner_id = prof_fav_owner_id;
        this.prof_fav_profile_id = prof_fav_profile_id;
    }

    //empty constructor
    public ProfileFavorites() {
    }

    public int getProf_fav_supporter_id() {
        return prof_fav_supporter_id;
    }

    public void setProf_fav_supporter_id(int prof_fav_supporter_id) {
        this.prof_fav_supporter_id = prof_fav_supporter_id;
    }

    public int getProf_fav_owner_id() {
        return prof_fav_owner_id;
    }

    public void setProf_fav_owner_id(int prof_fav_owner_id) {
        this.prof_fav_owner_id = prof_fav_owner_id;
    }

    public int getProf_fav_profile_id() {
        return prof_fav_profile_id;
    }

    public void setProf_fav_profile_id(int prof_fav_profile_id) {
        this.prof_fav_profile_id = prof_fav_profile_id;
    }

    @Override
    public String toString() {
        return "ProfileFavorites{" +
                "prof_fav_supporter_id=" + prof_fav_supporter_id +
                ", prof_fav_owner_id=" + prof_fav_owner_id +
                ", prof_fav_profile_id=" + prof_fav_profile_id +
                '}';
    }
}
