package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Favorites_Activity extends AppCompatActivity {
    private RecyclerView business_favorites_rec_view, item_favorites_rec_view;
    private BrowseProfilesAdapter profilesAdapter;
    private TextView no_business_favorites_txt, no_item_favorites_txt;

    //include the items recycler view adapter when the inventory system is created

    private DatabaseHelper databaseHelper;

    private String supporter_username;
    private int supporter_id;

    private ArrayList<Profile> all_browsable_profiles, favorited_browsable_profiles;
    private ArrayList<ProfileFavorites> profile_favorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //initialize recycler view elements
        business_favorites_rec_view = findViewById(R.id.business_favorites_rec_view);
        profilesAdapter = new BrowseProfilesAdapter(this);
        business_favorites_rec_view.setAdapter(profilesAdapter);
        business_favorites_rec_view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        //initialize texts that will be displayed if the user hasn't favorited any businesses or items
        no_business_favorites_txt = findViewById(R.id.no_business_favorites_txt);
        no_item_favorites_txt = findViewById(R.id.no_item_favorites_txt);

        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("supporter_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
            }
        }

        System.out.println("INSIDE FAVORITES: " + supporter_username + " " + supporter_id);

       GetFavorites(supporter_username, supporter_id);


    }



    //(note: this function might be too slow - might need to use threads for when the database is large)
    private void GetFavorites(String supporter_username, int supporter_id){

        supporter_id = 1098; //for testing

        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        //query the profile_favorites table and store the result in the profile_favorites arraylist
        try{
            Cursor profile_favorites_cursor = db.rawQuery("SELECT * FROM profile_favorites WHERE supporter_id =?", new String[]{String.valueOf(supporter_id)});
            Cursor profile_cursor = db.rawQuery("SELECT * FROM profile", null);

            if(profile_favorites_cursor != null){
                if(profile_favorites_cursor.moveToFirst()){
                   profile_favorites = new ArrayList<>();

                    int supporter_id_index_fav = profile_favorites_cursor.getColumnIndex("supporter_id");
                    int owner_id_index_fav = profile_favorites_cursor.getColumnIndex("owner_id");
                    int profile_id_index_fav = profile_favorites_cursor.getColumnIndex("profile_id");

                    for(int i = 0; i < profile_favorites_cursor.getCount(); i++){
                        ProfileFavorites pf  = new ProfileFavorites();
                        pf.setProf_fav_supporter_id(profile_favorites_cursor.getInt(supporter_id_index_fav));
                        pf.setProf_fav_owner_id(profile_favorites_cursor.getInt(owner_id_index_fav));
                        pf.setProf_fav_profile_id(profile_favorites_cursor.getInt(profile_id_index_fav));

                        profile_favorites.add(pf);
                        profile_favorites_cursor.moveToNext();
                    }
                    profile_favorites_cursor.close();




                    for (int i = 0; i < profile_favorites.size();i++)
                    {
                        System.out.println("FAVORITES PROFILE ARRAYLIST");
                        System.out.println(profile_favorites.get(i));
                    }


                }else{
                    no_business_favorites_txt.setVisibility(View.VISIBLE);
                    profile_favorites_cursor.close();
                }
            }else{
                no_business_favorites_txt.setVisibility(View.VISIBLE);
            }

            if(profile_cursor != null){
                if(profile_cursor.moveToFirst()){
                    all_browsable_profiles = new ArrayList<>();

                    int profile_id_index = profile_cursor.getColumnIndex("profile_id");
                    int owner_id_index = profile_cursor.getColumnIndex("owner_id");
                    int business_name_index = profile_cursor.getColumnIndex("business_name");
                    int profile_avatar_image_index = profile_cursor.getColumnIndex("profile_avatar_image");

                    for(int i = 0; i < profile_cursor.getCount(); i++){
                        Profile p  = new Profile();
                        p.setProfile_id(profile_cursor.getInt(profile_id_index));
                        p.setOwner_id(profile_cursor.getInt(owner_id_index));
                        p.setBusiness_name(profile_cursor.getString(business_name_index));
                        p.setProfile_avatar_image(profile_cursor.getString(profile_avatar_image_index));

                        all_browsable_profiles.add(p);
                        profile_cursor.moveToNext();
                    }
                    profile_cursor.close();
                    //db.close();
                    for (int i = 0; i < all_browsable_profiles.size();i++)
                    {
                        System.out.println("ALL BROWSABLE PROFILES");
                        System.out.println(all_browsable_profiles.get(i));
                    }
                }else{
                    profile_cursor.close();
                }


            }



            //can probably do this in a much more effective manner
            favorited_browsable_profiles = new ArrayList<>();
            for(Profile profile : all_browsable_profiles){
                if(profile_favorites != null){
                    for(ProfileFavorites profileFavorites: profile_favorites){
                        if(profile.getOwner_id() == profileFavorites.getProf_fav_owner_id() && profile.getProfile_id() == profileFavorites.getProf_fav_profile_id()){
                            favorited_browsable_profiles.add(profile);
                        }
                    }
                }else{
                }
            }




            System.out.println("FAVORITES TO ADD TO ADAPTER");
            for (int i = 0; i < favorited_browsable_profiles.size();i++)
            {
                System.out.println(favorited_browsable_profiles.get(i));
            }


            //profilesAdapter.setBrowsable_profiles(favorited_browsable_profiles);




        }catch(SQLiteException e){
            e.printStackTrace();

        }




        //retrieve the favorited business profiles and add them to the business favorites recycler view (when the inventory is created)
        no_item_favorites_txt.setVisibility(View.VISIBLE); //for now, just display that there are no items favorited


    }




}