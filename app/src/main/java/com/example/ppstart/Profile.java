//CREATED BY BLAKE

package com.example.ppstart;


//this is the Java class for a business profile
public class Profile {

    //only include what is necessary for the browse recycler view for now
    private int profile_id;
    private int owner_id;
    private String business_name;
    private String profile_avatar_image;


    public Profile(int profile_id, int owner_id, String business_name, String profile_avatar_image) {
        this.profile_id = profile_id;
        this.owner_id = owner_id;
        this.business_name = business_name;
        this.profile_avatar_image = profile_avatar_image;
    }


    //empty constructor
    public Profile(){

    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getProfile_avatar_image() {
        return profile_avatar_image;
    }

    public void setProfile_avatar_image(String profile_avatar_image) {
        this.profile_avatar_image = profile_avatar_image;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profile_id=" + profile_id +
                ", owner_id=" + owner_id +
                ", business_name='" + business_name + '\'' +
                ", profile_avatar_image='" + profile_avatar_image + '\'' +
                '}';
    }
}
