package com.example.ppstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Business_Profile_Activity extends AppCompatActivity {
    // layout and fragment view
    private BottomNavigationView bottom_nav_menu;
    private DrawerLayout drawer;
    private NavigationView main_nav_menu;
    private MaterialToolbar toolbar;
    private FragmentManager fragment_manager;

    // small item views and buttons
    private TextView owner_username_view;
    private Button guest_btn;

    // database tools
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor profile_cursor;

    // local variables
    private int owner_Id;
    private int supporter_Id;
    private String supporter_username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup main views
        setContentView(R.layout.activity_business_profile);
        drawer = findViewById(R.id.business_main_page_drawer);
        main_nav_menu = findViewById(R.id.supporter_main_page_navigationView);
        toolbar = findViewById(R.id.supporter_main_page_toolbar);

        // setup small views;
        owner_username_view = findViewById(R.id.owner_username);
        bottom_nav_menu = findViewById(R.id.profile_bottem_nav);

        // setup database
        databaseHelper = new DatabaseHelper(Business_Profile_Activity.this);
        db = databaseHelper.getReadableDatabase();

        // setup button
        guest_btn = findViewById(R.id.buss_pro_guest_btn);

        //receive the bundle passed from the BrowseProfilesAdapter (containing the owner_id of the business profile)
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("profile_bundle");
            if (bundle != null){
                owner_Id = bundle.getInt("owner_id");


                // provide guest and supporter functionality for views
                if (bundle.containsKey("supporter_id") && bundle.containsKey("supporter_username")){
                    supporter_Id = bundle.getInt("supporter_id");
                    if(supporter_Id == -1) {
                        // setup small views in the toolbar
                        supporter_username = "Guest";
                        owner_username_view.setVisibility(View.GONE);
                        guest_btn.setVisibility(View.VISIBLE);
                        guestButtonSetup();
                    }
                    else {
                            // setup small views in the toolbar
                            supporter_username = bundle.getString("supporter_username");
                            owner_username_view.setVisibility(View.VISIBLE);
                            owner_username_view.setText("Hello, " + supporter_username + "!");
                            guest_btn.setVisibility(View.GONE);
                        }
                    SupporterGuestNavMenuSetup();
                }
                else{ // provide owner functionality
                    //create cursor to move query the db and move through the query
                    supporter_Id = -2;
                    supporter_username = "";
                    Cursor owner_cursor = db.rawQuery("SELECT owner_id, first_name, last_name FROM owner_account", null);

                    // build indices for the cursor
                    if(owner_cursor != null) {
                        if (owner_cursor.moveToFirst()) {
                            int owner_id_index = owner_cursor.getColumnIndex("owner_id");
                            int fname_index = owner_cursor.getColumnIndex("first_name");
                            int lname_index = owner_cursor.getColumnIndex("last_name");

                            // parse through the query
                            for(int i = 0; i < owner_cursor.getCount(); i++){
                                if (owner_Id == owner_cursor.getInt(owner_id_index)) break;
                                owner_cursor.moveToNext();
                            }

                            // setup small views int the toolbar
                            owner_username_view.setVisibility(View.VISIBLE);
                            owner_username_view.setText("Hello, " + owner_cursor.getString(fname_index) + " " + owner_cursor.getString(lname_index) + "!");
                            guest_btn.setVisibility(View.GONE);
                        }
                    }
                }
                //profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id,null);

                // fragment and bottom navigation menu setup as well as fragment control
                createAboutFragmentManager();
                BottomNavMenuSetup();
            }
        }
    }


    private void createAboutFragmentManager(){
        // gather information for the bundle
        String name = "Business Name";
        String about = "About Description Not Provided!";
        if (profile_cursor != null){
            if(profile_cursor.moveToFirst()){
                int business_name_index = profile_cursor.getColumnIndex("business_name");
                int about_index = profile_cursor.getColumnIndex("profile_about_desc");
                if (!profile_cursor.isNull(about_index)) {
                    if (!profile_cursor.getString(about_index).trim().equals("")) about = "ABOUT\n\n" + profile_cursor.getString(about_index);
                }
                name = profile_cursor.getString(business_name_index);
            }
        }

        // prepare bundle to send to fragment
        Bundle fragment_info = new Bundle();
        fragment_info.putString("name", name);
        fragment_info.putString("info", about);

        // setup fragment manager and set initial fragment view with required bundle arguements
        fragment_manager = getSupportFragmentManager();
        fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
    }

    //TODO setput navigation menu for owners
    private void OwnerNavMenuSetup(){
    }

    private void BottomNavMenuSetup(){
        // setup for bottom navigation menu
        bottom_nav_menu.setSelectedItemId(R.id.about_button);
        bottom_nav_menu.setOnItemSelectedListener(item -> {

            // setup for database queries
            if (profile_cursor != null){
                if(profile_cursor.moveToFirst()){
                    Bundle fragment_info = new Bundle();
                    int business_name_index = profile_cursor.getColumnIndex("business_name");

                    // switch statement to control the selection of the navigation menu
                    switch (item.getItemId()){
                        case R.id.about_button: // logic control for about button and switching to about screen
                            int about_index = profile_cursor.getColumnIndex("profile_about_desc");
                            String about = "About Description Not Provided!";
                            if (!profile_cursor.isNull(about_index)) {
                                if (!profile_cursor.getString(about_index).trim().equals("")) about = "ABOUT\n\n" + profile_cursor.getString(about_index);
                            }
                            fragment_info.putString("name", profile_cursor.getString(business_name_index));
                            fragment_info.putString("info", about);
                            fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            break;
                        case R.id.inventory_button: // logic control for inventory button and switching to inventory screen
                            fragment_info.putString("name", profile_cursor.getString(business_name_index));
                            fragment_info.putString("info", "This page is coming soon!");
                            fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            break;
                        case R.id.hours_button:  // logic hours for about button and switching to hours screen
                            int hours_index = profile_cursor.getColumnIndex("profile_hours_desc");
                            String hours = "Hours of Operation Not Provided!";
                            if (!profile_cursor.isNull(hours_index)) {
                                if (!profile_cursor.getString(hours_index).trim().equals("")) hours = "HOURS OF OPERATION\n\n" + profile_cursor.getString(hours_index);
                            }
                            fragment_info.putString("name", profile_cursor.getString(business_name_index));
                            fragment_info.putString("info", hours);
                            fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            break;
                        case R.id.layout_button:  // logic layout for about button and switching to layout screen
                            fragment_info.putString("name", profile_cursor.getString(business_name_index));
                            fragment_info.putString("info", "This page is coming soon!");
                            fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            break;
                    }
                }
            }
            return true;
        });
    }


    @Override
    public void onBackPressed() {
        if(supporter_Id == -2){
            // If the user is a owner and the back button is pressed log out.
            Intent logout = new Intent(Business_Profile_Activity.this, MainActivity.class);
            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logout);
        }
        else{
            // If the user is a guest or an support return them to the Supporter main page if the back button is pressed
            Intent explore_view = new Intent(Business_Profile_Activity.this, Supporter_Main_Page_Activity.class);
            explore_view.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            // bundle for Supporter main page
            Bundle supporter_bundle = new Bundle();
            supporter_bundle.putInt("supporter_id", supporter_Id);
            supporter_bundle.putString("supporter_username", supporter_username);
            explore_view.putExtra("supporter_bundle", supporter_bundle);

            startActivity(explore_view);
        }
    }


//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// reimplementation of code from Supporter Main Page Activity with variable changes to match this class.
// The purpose of this is to provide uniformity across the application

    private void guestButtonSetup(){
        // if the guest presses the toolbar button they are redirected to the main menu so they can login/sign up
        guest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(Business_Profile_Activity.this, MainActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logout);
            }
        });
    }

    //TODO setup nav menu for guest and supporter
    private void SupporterGuestNavMenuSetup(){
    }
}