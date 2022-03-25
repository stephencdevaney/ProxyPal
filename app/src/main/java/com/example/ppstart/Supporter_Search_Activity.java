package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Supporter_Search_Activity extends AppCompatActivity {

    private MaterialToolbar search_toolbar;
    private EditText search_box;
    private ImageView search_btn;
    private BottomNavigationView supporter_main_page_bottom_menu;

    //for database useage
    private DatabaseHelper databaseHelper;


    //Declare a recycler view for business profile avatars
    private RecyclerView search_rec_view;

    //Declare a recycler view adapter for business profile avatars
    private BrowseProfilesAdapter searchProfilesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_search);

        initViews();

        initBottomNavigationView();

        setSupportActionBar(search_toolbar);

        //initialize the recycler view adapter
        searchProfilesAdapter = new BrowseProfilesAdapter(this);
        search_rec_view = findViewById(R.id.search_recycler_view);
        search_rec_view.setAdapter(searchProfilesAdapter);
        search_rec_view.setLayoutManager(new GridLayoutManager(this, 2));


        //set the on-click listener for the search icon
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSearch();

            }
        });

        //this listens for changes in the text entered into the search box
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //this method is called when the text inside the search box changes
                initSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void initBottomNavigationView() {
        //selects home as the default view - make this browse for supporter
        supporter_main_page_bottom_menu.setSelectedItemId(R.id.search);
        //use this since the onNavigationItemSelected is deprecated
        supporter_main_page_bottom_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    break;
                case R.id.explore:
                    Intent to_explore = new Intent(Supporter_Search_Activity.this, Supporter_Main_Page_Activity.class);
                    to_explore.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //clears back-stack - might use elsewhere too
                    startActivity(to_explore);
                    break;
                case R.id.discounts_and_promos:
                    break;
                case R.id.shopping:
                    break;
                default:
                    break;
            }

            return true;
        });

    }

    private void initViews(){
        search_toolbar = findViewById(R.id.search_toolbar);
        search_box = findViewById(R.id.search_box);
        search_btn = findViewById(R.id.search_btn);
        supporter_main_page_bottom_menu = findViewById(R.id.supporter_main_page_bottom_menu);
    }

    private void initSearch() {
        if (!search_box.getText().toString().equals("")) {
            String search_box_entry = search_box.getText().toString();

            databaseHelper = new DatabaseHelper(this);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();


            try {
                //retrieve the business profiles in the database using this query
                Cursor cursor = db.rawQuery("SELECT * FROM profile", null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        ArrayList<Profile> all_browsable_profiles = new ArrayList<>(); //business profiles taken from the database will be added to this ArrayList

                        int profile_id_index = cursor.getColumnIndex("profile_id");
                        int owner_id_index = cursor.getColumnIndex("owner_id");
                        int business_name_index = cursor.getColumnIndex("business_name");
                        int profile_avatar_image_index = cursor.getColumnIndex("profile_avatar_image");

                        for (int i = 0; i < cursor.getCount(); i++) {
                            Profile p = new Profile();
                            p.setProfile_id(cursor.getInt(profile_id_index));
                            p.setOwner_id(cursor.getInt(owner_id_index));
                            p.setBusiness_name(cursor.getString(business_name_index));
                            p.setProfile_avatar_image(cursor.getString(profile_avatar_image_index));

                            all_browsable_profiles.add(p);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        db.close();

                        //create a new ArrayList to store profiles that are searched
                        if(all_browsable_profiles != null){
                            ArrayList<Profile> browsable_profiles_search = new ArrayList<>();
                            for(Profile profile: all_browsable_profiles){
                                if(profile.getBusiness_name().equalsIgnoreCase(search_box_entry)){
                                    browsable_profiles_search.add(profile);
                                }
                                //split the search entry by spaces and use the result for matching partial searches
                                String[] partial_search = profile.getBusiness_name().split(" ");
                                for(int i = 0; i < partial_search.length; i++){
                                    if(search_box_entry.equalsIgnoreCase(partial_search[i])){
                                        boolean exists = false;

                                        for(Profile profile_search: browsable_profiles_search){
                                            if(profile_search.getProfile_id() == profile.getProfile_id()){
                                                exists = true;
                                            }
                                        }

                                        if(!exists){
                                            browsable_profiles_search.add(profile);
                                        }
                                    }
                                }



                            }

                            if(browsable_profiles_search != null){
                                searchProfilesAdapter.setBrowsable_profiles(browsable_profiles_search);
                            }
                        }


                    } else {
                        cursor.close();
                        db.close();
                    }
                } else {
                    db.close();
                }

            } catch (SQLiteException e) {
                e.printStackTrace();

            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this prevents the bottom navigation menu from floating on top of the keyboard when the user is searching
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }
}