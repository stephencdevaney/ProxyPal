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
import android.view.Menu;
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
    private View header_view;
    private ImageView logo;
    private TextView account_username;
    private ActionBarDrawerToggle toggle;

    // database tools
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor profile_cursor;

    // local variables
    private int owner_Id;
    private String owner_username;
    private int supporter_Id;
    private String supporter_username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup database
        databaseHelper = new DatabaseHelper(Business_Profile_Activity.this);
        db = databaseHelper.getReadableDatabase();

        // database query setup on profile table for profile used in createAboutFragmentManager and BottemNavMenuSetup functions
        // profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id,null);

        // setup main views
        setContentView(R.layout.activity_business_profile);
        drawer = findViewById(R.id.business_main_page_drawer);
        main_nav_menu = findViewById(R.id.owner_navigationView);
        toolbar = findViewById(R.id.owner_main_page_toolbar);

        // setup small views;
        owner_username_view = findViewById(R.id.owner_username);
        bottom_nav_menu = findViewById(R.id.profile_bottem_nav);

        // setup button
        guest_btn = findViewById(R.id.buss_pro_guest_btn);

        //receive the bundle passed from the BrowseProfilesAdapter (containing the owner_id of the business profile)
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("profile_bundle");
            if (bundle != null){
                owner_Id = bundle.getInt("owner_id");

                // database query setup on profile table for profile used in createAboutFragmentManager and BottemNavMenuSetup functions
                profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id,null);

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
                    Cursor owner_cursor = db.rawQuery("SELECT * FROM owner_account WHERE owner_id=" + owner_Id, null);

                    // build indices for the cursor
                    if(owner_cursor != null) {
                        if (owner_cursor.moveToFirst()) {
                            int owner_username_index = owner_cursor.getColumnIndex("owner_username");
                            int fname_index = owner_cursor.getColumnIndex("first_name");
                            int lname_index = owner_cursor.getColumnIndex("last_name");

                            owner_username = owner_cursor.getString(owner_username_index);

                            // setup small views int the toolbar
                            owner_username_view.setVisibility(View.VISIBLE);
                            owner_username_view.setText("Hello, " + owner_cursor.getString(fname_index) + " " + owner_cursor.getString(lname_index) + "!");
                            guest_btn.setVisibility(View.GONE);
                            OwnerNavMenuSetup();
                        }
                    }
                }

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


    private void OwnerNavMenuSetup(){
        //create the toggleable drawer inside the toolbar at the top of this page
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, (R.string.supporter_drawer_open), (R.string.supporter_drawer_close));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //this allows the TextView/ImageView in the top of the drawer header to be manipulated
        header_view = main_nav_menu.getHeaderView(0);
        logo = (ImageView) header_view.findViewById(R.id.drawer_header_image);
        account_username = (TextView) header_view.findViewById(R.id.drawer_header_username);

        //set the TextView inside the drawer header for this activity to the supporter account's username
        account_username.setText(owner_username);

        //set the icon in the drawer header to the ProxyPal logo
        if (profile_cursor != null){
            if(profile_cursor.moveToFirst()){
                int image_index = profile_cursor.getColumnIndex("profile_avatar_image");
                Glide.with(this).asBitmap()
                        .load(profile_cursor.getString(image_index))
                        .into(logo);
                }
            }

        // remove guest menu items
        Menu menu = main_nav_menu.getMenu();
        menu.removeItem(R.id.drawer_account);
        menu.removeItem(R.id.drawer_favorites);
        menu.removeItem(R.id.drawer_direct_messages);

        //set the listener for when options in the drawer menu are tapped
        main_nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.drawer_logout:
                        Intent logout = new Intent(Business_Profile_Activity.this, MainActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logout);
                        break;
                }
                return false;
            }
        });
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
                    String businessName = "Business Name";
                    if (!profile_cursor.isNull(business_name_index)) {
                        if (!profile_cursor.getString(business_name_index).trim().equals("")) businessName = profile_cursor.getString(business_name_index);
                    }

                    // switch statement to control the selection of the navigation menu
                    switch (item.getItemId()){
                        case R.id.about_button: // logic control for about button and switching to about screen
                            int about_index = profile_cursor.getColumnIndex("profile_about_desc");
                            String about = "About Description Not Provided!";
                            if (!profile_cursor.isNull(about_index)) {
                                if (!profile_cursor.getString(about_index).trim().equals("")) about = "ABOUT\n\n" + profile_cursor.getString(about_index);
                            }
                            fragment_info.putString("name", businessName);
                            fragment_info.putString("info", about);
                            fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            break;
                        case R.id.inventory_button: // logic control for inventory button and switching to inventory screen
                            fragment_info.putString("name", businessName);
                            fragment_info.putString("info", "This page is coming soon!");
                            fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            break;
                        case R.id.hours_button:  // logic hours for about button and switching to hours screen
                            int hours_index = profile_cursor.getColumnIndex("profile_hours_desc");
                            String hours = "Hours of Operation Not Provided!";
                            if (!profile_cursor.isNull(hours_index)) {
                                if (!profile_cursor.getString(hours_index).trim().equals("")) hours = "HOURS OF OPERATION\n\n" + profile_cursor.getString(hours_index);
                            }
                            fragment_info.putString("name", businessName);
                            fragment_info.putString("info", hours);
                            fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            break;
                        case R.id.layout_button:  // logic layout for about button and switching to layout screen
                            int layout_index = profile_cursor.getColumnIndex("profile_map_image");
                            String layout = "NULL";
                            if (!profile_cursor.isNull(layout_index)) {
                                if (!profile_cursor.getString(layout_index).trim().equals("")) layout = profile_cursor.getString(layout_index);
                            }
                            fragment_info.putString("name", businessName);
                            fragment_info.putString("info", layout);
                            fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_image.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
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


    private void SupporterGuestNavMenuSetup(){
        //create the toggleable drawer inside the toolbar at the top of this page
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, (R.string.supporter_drawer_open), (R.string.supporter_drawer_close));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //this allows the TextView/ImageView in the top of the drawer header to be manipulated
        header_view = main_nav_menu.getHeaderView(0);
        logo = (ImageView) header_view.findViewById(R.id.drawer_header_image);
        account_username = (TextView) header_view.findViewById(R.id.drawer_header_username);



        //set the TextView inside the drawer header for this activity to the supporter account's username
        if(supporter_Id != -1){
            account_username.setText(supporter_username);
        }else{
            account_username.setText("Guest");
        }


        //set the icon in the drawer header to the ProxyPal logo
        Glide.with(this).asBitmap()
                .load("https://icons.iconarchive.com/icons/hydrattz/multipurpose-alphabet/256/Letter-P-violet-icon.png")
                .into(logo);


        //set the listener for when options in the drawer menu are tapped
        main_nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.drawer_account:
                        if(supporter_Id != -1){
                            Intent to_options = new Intent(Business_Profile_Activity.this, Supporter_Acc_Options_Activity.class);
                            //pass the supporter account id and supporter account username to the Favorites_Activity activity
                            Bundle supporter_bundle = new Bundle();
                            supporter_bundle.putInt("supporter_id", supporter_Id);
                            supporter_bundle.putString("supporter_username", supporter_username);
                            to_options.putExtra("supporter_bundle", supporter_bundle);
                            //switch to the Favorites_Activity activity
                            startActivity(to_options);
                        }else{
                            Toast.makeText(Business_Profile_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case  R.id.drawer_favorites:
                        if(supporter_Id != -1){
                            Intent to_favorites = new Intent(Business_Profile_Activity.this, Favorites_Activity.class);
                            //pass the supporter account id and supporter account username to the Favorites_Activity activity
                            Bundle supporter_bundle = new Bundle();
                            supporter_bundle.putInt("supporter_id", supporter_Id);
                            supporter_bundle.putString("supporter_username", supporter_username);
                            to_favorites.putExtra("supporter_bundle", supporter_bundle);
                            //switch to the Favorites_Activity activity
                            startActivity(to_favorites);
                        }else{
                            Toast.makeText(Business_Profile_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.drawer_direct_messages:
                        if(supporter_Id != -1){
                            Toast.makeText(Business_Profile_Activity.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Business_Profile_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.drawer_logout:
                        Intent logout = new Intent(Business_Profile_Activity.this, MainActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logout);

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}