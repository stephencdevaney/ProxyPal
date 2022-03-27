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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Supporter_Search_Activity extends AppCompatActivity {

    private MaterialToolbar search_toolbar;
    private EditText search_box;
    private ImageView search_btn;
    private Spinner search_spinner;
    private BottomNavigationView supporter_bottom_nav_menu;

    //for database useage
    private DatabaseHelper databaseHelper;


    //Declare a recycler view for business profile avatars
    private RecyclerView search_rec_view;

    //Declare a recycler view adapter for business profile avatars
    private BrowseProfilesAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_search);

        initViews();

        initBottomNavigationView();

        setSupportActionBar(search_toolbar);

        //initialize the recycler view adapter
        searchAdapter = new BrowseProfilesAdapter(this);
        search_rec_view = findViewById(R.id.search_recycler_view);
        search_rec_view.setAdapter(searchAdapter);
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

        //allows the user to select whether they want to search for items or businesses
        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (search_spinner.getSelectedItem().toString().equals("Business")) {
                    Toast.makeText(Supporter_Search_Activity.this, "Business selected", Toast.LENGTH_SHORT).show();
                }
                if (search_spinner.getSelectedItem().toString().equals("Item")) {
                    Toast.makeText(Supporter_Search_Activity.this, "Item selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void initBottomNavigationView() {
        //sets the explore page as the default page
        supporter_bottom_nav_menu.setSelectedItemId(R.id.search);

        //Set the listener for the bottom navigation menu buttons
        //(note: use this since the onNavigationItemSelected is deprecated)
        supporter_bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //switch to the Supporter_Search_Activity activity when the "search" button at the bottom is tapped
                case R.id.search:
                    break;
                //switch to the Supporter_Main_Page_Activity activity when the "explore" button at the bottom is tapped
                case R.id.explore:
                    Intent to_explore = new Intent(Supporter_Search_Activity.this, Supporter_Main_Page_Activity.class);
                    startActivity(to_explore);
                    break;
                //switch to the Discounts_Promos_Activity activity when the "Discounts & Promos" button at the bottom is tapped
                case R.id.discounts_and_promos:
                    Intent to_dp = new Intent(Supporter_Search_Activity.this, Discounts_Promos_Activity.class);
                    startActivity(to_dp);
                    break;
                //switch to the Shopping_List_Activity activity when the "Shopping List" button at the bottom is tapped
                case R.id.shopping:
                    Intent to_shopping_list = new Intent(Supporter_Search_Activity.this, Shopping_List_Activity.class);
                    startActivity(to_shopping_list);
                    break;
                default:
                    break;
            }

            return true;
        });

    }

    //initialize UI elements
    private void initViews(){
        search_toolbar = findViewById(R.id.search_toolbar);
        search_box = findViewById(R.id.search_box);
        search_btn = findViewById(R.id.search_btn);
        supporter_bottom_nav_menu = findViewById(R.id.supporter_bottom_nav_menu);
        search_spinner = findViewById(R.id.search_spinner);
    }

    //this method allows the user to search for businesses or items (with rudimentary partial search allowed)
    private void initSearch() {
        //empty Profile ArrayList used for clearing the screen
        ArrayList<Profile> empty_list = new ArrayList<>();

        //for querying the database
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        if (!search_box.getText().toString().equals("")) {
            String search_box_entry = search_box.getText().toString();

            //if the user selected the business option in the spinner
            if (search_spinner.getSelectedItem().toString().equals("Business")) {
                searchAdapter.setBrowsable_profiles(empty_list);


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
                            if (all_browsable_profiles != null) {
                                ArrayList<Profile> browsable_profiles_search = new ArrayList<>();
                                for (Profile profile : all_browsable_profiles) {
                                    if (profile.getBusiness_name().equalsIgnoreCase(search_box_entry)) {
                                        browsable_profiles_search.add(profile);
                                    }
                                    //split the search entry by spaces and use the result for matching partial searches
                                    String[] partial_search = profile.getBusiness_name().split(" ");
                                    for (int i = 0; i < partial_search.length; i++) {
                                        if (search_box_entry.equalsIgnoreCase(partial_search[i])) {
                                            boolean exists = false;

                                            for (Profile profile_search : browsable_profiles_search) {
                                                if (profile_search.getProfile_id() == profile.getProfile_id()) {
                                                    exists = true;
                                                }
                                            }

                                            if (!exists) {
                                                browsable_profiles_search.add(profile);
                                            }
                                        }
                                    }


                                }

                                if (browsable_profiles_search != null) {
                                    searchAdapter.setBrowsable_profiles(browsable_profiles_search);
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
            //if the user selected the item option in the spinner
            if (search_spinner.getSelectedItem().toString().equals("Item")) {
                if (!search_box.getText().toString().equals("")) {
                    searchAdapter.setBrowsable_profiles(empty_list);
                    Toast.makeText(this, "No Items", Toast.LENGTH_SHORT).show();
                    //once the inventory system has been progressed, this code will be used to do a general search for items
                }

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