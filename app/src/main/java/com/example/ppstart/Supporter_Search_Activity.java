//CREATED BY BLAKE

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

    //Declare the UI elements -Blake
    private MaterialToolbar search_toolbar;
    private EditText search_box;
    private ImageView search_btn;
    private Spinner search_spinner;
    private BottomNavigationView supporter_bottom_nav_menu;

    //for database useage -Blake
    private DatabaseHelper databaseHelper;


    //The usual variables for storing the supporter id and username -Blake
    private int supporter_id;
    private String supporter_username;

    //Declare a recycler view for business profile avatars (this recycler view functions the same as the one in the explore page) -Blake
    private RecyclerView search_rec_view;

    //This is the same adapter as the one used for the explore page recycler view -Blake
    private BrowseProfilesAdapter searchAdapter;

    //this is used for partial searches -Blake
    boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_search);

        //receive the id/username from the bundle passed to this activity -Blake
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("supporter_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
            }
        }

        //method that initializes views, to keep the code here neat -Blake
        initViews();

        //this is the same method for creating the bottom navigation menu
        //as the one used in the Supporter_Main_Page_Fragment -Blake
        initBottomNavigationView();

        //The search box and icon are set in a toolbar -Blake
        setSupportActionBar(search_toolbar);

        //initialize the recycler view adapter - again, this is the same as the recycler view
        //in the browse page -Blake
        searchAdapter = new BrowseProfilesAdapter(this);
        search_rec_view = findViewById(R.id.search_recycler_view);
        search_rec_view.setAdapter(searchAdapter);
        search_rec_view.setLayoutManager(new GridLayoutManager(this, 2));


        //set the on-click listener for the search icon -Blake
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This method defines the UI elements for the search and executes it -Blake
                Search();
            }
        });

        //this listens for changes in the text entered into the search box -Blake
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //this method is called when the text inside the search box changes -Blake
                Search();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //allows the user to select whether they want to search for items or businesses -Blake
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

    //functionally identical to the nan menu for the other pages/activities that use this bottom nav menu -Blake
    private void initBottomNavigationView() {
        //sets the search page as the default page -Blake
        supporter_bottom_nav_menu.setSelectedItemId(R.id.search);

        //Set the listener for the bottom navigation menu buttons
        //(note: use this since the onNavigationItemSelected is deprecated) -Blake
        supporter_bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //Do nothing when "search" is tapped in the bottom nav menu, since we're already in the search activity! -Blake
                case R.id.search:
                    break;
                //switch to the Supporter_Main_Page_Activity activity when the "explore" button at the bottom is tapped -Blake
                case R.id.explore:
                        Intent to_explore = new Intent(this, Supporter_Main_Page_Activity.class);
                        //pass the supporter account id and supporter account username to the Favorites_Activity activity -Blake
                        Bundle supporter_bundle_search = createNavMenuBundle();
                        to_explore.putExtra("supporter_bundle", supporter_bundle_search);
                        //switch to the Favorites_Activity activity -Blake
                        startActivity(to_explore);
                    break;
                //switch to the Discounts_Promos_Activity activity when the "Discounts & Promos" button at the bottom is tapped -Blake
                case R.id.discounts_and_promos:
                    if(supporter_id != -1){
                        Intent to_dp = new Intent(this, Discounts_Promos_Activity.class);
                        //pass the supporter account id and supporter account username to the Favorites_Activity activity -Blake
                        Bundle supporter_bundle_dp = createNavMenuBundle();
                        to_dp.putExtra("supporter_bundle", supporter_bundle_dp);
                        //switch to the Favorites_Activity activity -Blake
                        startActivity(to_dp);
                    }else{
                        //Guests cannot access discounts/promos -Blake
                        Toast.makeText(this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                //switch to the Shopping_List_Activity activity when the "Shopping List" button at the bottom is tapped -Blake
                case R.id.shopping:
                    if(supporter_id != -1){
                        Intent to_shopping_list = new Intent(this, Shopping_List_Activity.class);
                        //pass the supporter account id and supporter account username to the Favorites_Activity activity -Blake
                        Bundle supporter_bundle_shopping = createNavMenuBundle();
                        to_shopping_list.putExtra("supporter_bundle", supporter_bundle_shopping);
                        //switch to the Favorites_Activity activity -Blake
                        startActivity(to_shopping_list);
                    }else{
                        //Guests cannot access shopping list
                        Toast.makeText(this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
            }

            return true;
        });

    }


    private Bundle createNavMenuBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("supporter_id", supporter_id);
        bundle.putString("supporter_username", supporter_username);
        return bundle;
    }

    //initialize UI elements
    private void initViews(){
        search_toolbar = findViewById(R.id.search_toolbar);
        search_box = findViewById(R.id.search_box);
        search_btn = findViewById(R.id.search_btn);
        supporter_bottom_nav_menu = findViewById(R.id.supporter_bottom_nav_menu);
        search_spinner = findViewById(R.id.search_spinner);
    }

    //this method allows the user to search for businesses or items (with rudimentary partial search allowed) -Blake
    private void Search() {
        //empty Profile ArrayList used for clearing the screen -Blake
        ArrayList<Profile> empty_list = new ArrayList<>();

        //for querying the database
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        //The search does not execute if the search box has no text entered into it -Blake
        if (!search_box.getText().toString().equals("")) {
            //store what is entered in the search box into this String variable -Blake
            String search_box_entry = search_box.getText().toString();

            //if the user selects the business option in the spinner (meaning the search will be for businesses) -Blake
            if (search_spinner.getSelectedItem().toString().equals("Business")) {
                //if the user selected the items option for searching and then switches to the business option for searching,
                //the recycler view adapter is set using the empty array list so that the screen clears -Blake
                searchAdapter.setBrowsable_profiles(empty_list);

                try {
                    //retrieve all business profiles in the database using this query
                    //this query functions the same as the one in the browse page -Blake
                    Cursor cursor = db.rawQuery("SELECT * FROM profile", null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            ArrayList<Profile> all_browsable_profiles = new ArrayList<>(); //business profiles taken from the database will be added to this ArrayList -Blake

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

                            //create a new ArrayList to store profiles that are searched -Blake
                            if (all_browsable_profiles != null) {
                                ArrayList<Profile> browsable_profiles_search = new ArrayList<>();
                                for (Profile profile : all_browsable_profiles) {
                                    //.equalsIgnoreCase ensures the search is case-insensitive -Blake
                                    if (profile.getBusiness_name().equalsIgnoreCase(search_box_entry)) {
                                        //if the name of a business queried from the database exactly matches what is entered into the
                                        //search box, then it is stored into the array list of items successfully searched -Blake
                                        browsable_profiles_search.add(profile);
                                    }

                                    //split the profile being iterated through by the for-each loop by spaces and
                                    //store the result in a string array -Blake
                                    String[] partial_search = profile.getBusiness_name().split(" ");
                                    for (int i = 0; i < partial_search.length; i++) {

                                        //if the user enters a partial search and it matches a string stored
                                        //in the partial search array, then check if the profile was already added to the
                                        //array list of successful searches -Blake
                                        if (search_box_entry.equalsIgnoreCase(partial_search[i])) {
                                            for (Profile profile_search : browsable_profiles_search) {
                                                //if the profile being iterated through by the first for-each loop
                                                //was already added to the list of successful searches, then indicate
                                                //this by setting the exists boolean value to true -Blake
                                                if (profile_search.getProfile_id() == profile.getProfile_id()) {
                                                    exists = true;
                                                }
                                            }

                                            //if the profile was not already added to the list of successful searches,
                                            //then add it to this list, meaning that it was found by partial search and not
                                            //exact match -Blake
                                            if (!exists) {
                                                browsable_profiles_search.add(profile);
                                            }
                                        }
                                    }


                                }

                                //set the adapter for the recycler view using the array list containing all of the profiles
                                //that were successfully searched for -Blake
                                if (browsable_profiles_search != null) {
                                    searchAdapter.setBrowsable_profiles(browsable_profiles_search);
                                }
                            }


                        } else {
                            //Always close the cursor and database where necessary! -Blake
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

            //if the user selected the item option in the spinner -Blake
            if (search_spinner.getSelectedItem().toString().equals("Item")) {
                if (!search_box.getText().toString().equals("")) {
                    searchAdapter.setBrowsable_profiles(empty_list);
                    Toast.makeText(this, "No Items", Toast.LENGTH_SHORT).show();
                    //once the inventory system has been progressed, this code will be used to do a general search for items
                    //in a way that is functionally identical to the searching done for businesses -Blake
                }

            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        //this prevents the bottom navigation menu from floating on top of the keyboard when the user is searching -Blake
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }
}