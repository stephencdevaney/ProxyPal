package com.example.ppstart;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // buttons added by Blake for the supporter to message/favorite a business
    private Button add_to_fav_btn;
    private Button direct_message_btn;

    // database tools
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor profile_cursor;

    // local variables
    private int owner_Id;
    private String owner_username;
    private int supporter_Id;
    private String supporter_username;
    private boolean edit_view_flag;
    private String businessName;


    //for loading screen
    ProgressDialog progressDialog;

    //ArrayList to store favorited businesses
    private ArrayList<ProfileFavorites> profile_favorites;

    //ArrayList to store all chats for the supporter
    private ArrayList<AllChatsClass> all_chats_arr_list;

    //Variable for storing instance of the firestore database
    private FirebaseFirestore firebaseFirestore;

    //For use with the direct messages button
    String business_name;
    String owner_username_msg;

    private Boolean business_favorited = false;
    private Boolean chat_exists = false;


    // Method to run code when an instance of the business profile is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set loading screen for supporter view
        progressDialog = new ProgressDialog(this);


        //flag setup
        edit_view_flag = false;

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

        //Blake's buttons
        add_to_fav_btn = findViewById(R.id.add_to_fav_btn);
        direct_message_btn = findViewById(R.id.direct_message_btn);

        //Initializing array list of favorited profiles
        profile_favorites = new ArrayList<>();

        //Initializing array list of all chats
        all_chats_arr_list = new ArrayList<>();


        // setup button
        guest_btn = findViewById(R.id.buss_pro_guest_btn);

        //receive the bundle passed from the BrowseProfilesAdapter (containing the owner_id of the business profile)
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("profile_bundle");
            if (bundle != null) {
                owner_Id = bundle.getInt("owner_id");

                // database query setup on profile table for profile used in createAboutFragmentManager and BottemNavMenuSetup functions
                profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id, null);

                // provide guest and supporter functionality for views
                if (bundle.containsKey("supporter_id") && bundle.containsKey("supporter_username")) {
                    supporter_Id = bundle.getInt("supporter_id");
                    if (supporter_Id == -1) {
                        // setup small views in the toolbar
                        supporter_username = "Guest";
                        owner_username_view.setVisibility(View.GONE);

                        add_to_fav_btn.setVisibility(View.GONE);
                        direct_message_btn.setVisibility(View.GONE);

                        guest_btn.setVisibility(View.VISIBLE);
                        guestButtonSetup();
                    } else {

                        // setup small views in the toolbar
                        supporter_username = bundle.getString("supporter_username");
                        owner_username_view.setVisibility(View.GONE);
                        //owner_username_view.setText("Hello, " + supporter_username + "!");

                        guest_btn.setVisibility(View.GONE);

                        //Handle the favorites button
                        HandleFavoritesButton();
                        //Handle the messages button
                        HandleMessagesButton();


                        //set the on-click listener for the messages button

                    }
                    SupporterGuestNavMenuSetup();
                } else { // provide owner functionality
                    //create cursor to move query the db and move through the query
                    if (bundle.containsKey("edit_view_flag")) {
                        edit_view_flag = bundle.getBoolean("edit_view_flag");
                    }
                    supporter_Id = -2;
                    supporter_username = "";
                    Cursor owner_cursor = db.rawQuery("SELECT * FROM owner_account WHERE owner_id=" + owner_Id, null);

                    // build indices for the cursor
                    if (owner_cursor != null) {
                        if (owner_cursor.moveToFirst()) {
                            int owner_username_index = owner_cursor.getColumnIndex("owner_username");
                            int fname_index = owner_cursor.getColumnIndex("first_name");
                            int lname_index = owner_cursor.getColumnIndex("last_name");

                            owner_username = owner_cursor.getString(owner_username_index);

                            // setup small views int the toolbar

                            add_to_fav_btn.setVisibility(View.GONE);
                            direct_message_btn.setVisibility(View.GONE);

                            owner_username_view.setVisibility(View.VISIBLE);
                            owner_username_view.setText("Hello, " + owner_cursor.getString(fname_index) + " " + owner_cursor.getString(lname_index) + "!");
                            guest_btn.setVisibility(View.GONE);
                            owner_cursor.close();
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


    // Method to create the initial fragment when business profiles is loaded - Stephen
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
                profile_cursor.close();
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


    // Method for setting up the owners navigation menu - Stephen
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

        //set the icon in the drawer header to the business logo
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
       // menu.removeItem(R.id.drawer_account);
        menu.removeItem(R.id.drawer_favorites);


        //commented out so that Owners can access their DM's -Blake
        //menu.removeItem(R.id.drawer_direct_messages);

        //set the listener for when options in the drawer menu are tapped
        main_nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle fragment_info = new Bundle();
                switch (item.getItemId()) {
                    case R.id.supporter_view:
                        bottom_nav_menu.setSelectedItemId(R.id.about_button);
                        // setup for database queries
                        profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id, null);
                        if (profile_cursor != null){
                            if (profile_cursor.moveToFirst()) {
                                int business_name_index = profile_cursor.getColumnIndex("business_name");
                                businessName = "Business Name";
                                if (!profile_cursor.isNull(business_name_index)) {
                                    if (!profile_cursor.getString(business_name_index).trim().equals(""))
                                        businessName = profile_cursor.getString(business_name_index);
                                }
                                int about_index = profile_cursor.getColumnIndex("profile_about_desc");
                                String about = "About Description Not Provided!";
                                if (!profile_cursor.isNull(about_index)) {
                                    if (!profile_cursor.getString(about_index).trim().equals(""))
                                        about = "ABOUT\n\n" + profile_cursor.getString(about_index);
                                }
                                fragment_info.putString("name", businessName);
                                fragment_info.putString("info", about);
                                fragment_info.putInt("owner_id", owner_Id);
                                edit_view_flag = false;
                                fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                                profile_cursor.close();
                            }
                        }
                        break;
                    case R.id.edit_supporter_view:
                        // setup for database queries
                        bottom_nav_menu.setSelectedItemId(R.id.about_button);
                        profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id, null);
                        if (profile_cursor != null){
                            if (profile_cursor.moveToFirst()) {
                                int business_name_index = profile_cursor.getColumnIndex("business_name");
                                businessName = "Business Name";
                                if (!profile_cursor.isNull(business_name_index)) {
                                    if (!profile_cursor.getString(business_name_index).trim().equals(""))
                                        businessName = profile_cursor.getString(business_name_index);
                                }
                                int about_index = profile_cursor.getColumnIndex("profile_about_desc");
                                String about = "About Description Not Provided!";
                                if (!profile_cursor.isNull(about_index)) {
                                    if (!profile_cursor.getString(about_index).trim().equals(""))
                                        about = "ABOUT\n\n" + profile_cursor.getString(about_index);
                                }
                                fragment_info.putString("name", businessName);
                                fragment_info.putString("info", about);
                                fragment_info.putInt("owner_id", owner_Id);
                                edit_view_flag = true;
                                fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_textEditor.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                                profile_cursor.close();
                            }
                        }
                        break;
                    case R.id.account_management:
                        break;
                    case R.id.drawer_direct_messages:
                        Intent to_dm = new Intent(Business_Profile_Activity.this, All_Chats_Activity.class);
                        Bundle dm_bundle = new Bundle();
                        dm_bundle.putInt("owner_id", owner_Id);
                        dm_bundle.putString("owner_username", owner_username);
                        to_dm.putExtra("dm_bundle", dm_bundle);
                        startActivity(to_dm);
                        break;
                    case R.id.owner_promotions:
                        Intent to_promo = new Intent(getApplicationContext(), Discounts_Promos_Activity.class);
                        Bundle dp_bundle = new Bundle();
                        dp_bundle.putInt("owner_id", owner_Id);
                        dp_bundle.putString("owner_username", owner_username);
                        to_promo.putExtra("promo_bundle", dp_bundle);
                        startActivity(to_promo);
                        break;
                    case R.id.drawer_logout:
                        profile_cursor.close();
                        db.close();
                        Intent logout = new Intent(Business_Profile_Activity.this, MainActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logout);
                        break;
                }
                drawer.close();
                return false;
            }
        });
    }


    private void BottomNavMenuSetup(){  // Setup bottom navigation menu for owner, supporter, and guest functionality - Stephen
        // setup for bottom navigation menu
        bottom_nav_menu.setSelectedItemId(R.id.about_button);
        bottom_nav_menu.setOnItemSelectedListener(item -> {
            Bundle fragment_info = new Bundle();

            // switch statement to control the selection of the navigation menu
            switch (item.getItemId()){
                case R.id.about_button: // logic control for about button and switching to about screen
                    // setup for database queries
                    profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id, null);
                    if (profile_cursor != null){
                        if(profile_cursor.moveToFirst()){
                            int business_name_index = profile_cursor.getColumnIndex("business_name");
                            businessName = "Business Name";
                            if (!profile_cursor.isNull(business_name_index)) {
                                if (!profile_cursor.getString(business_name_index).trim().equals("")) businessName = profile_cursor.getString(business_name_index);
                            }
                            int about_index = profile_cursor.getColumnIndex("profile_about_desc");
                            String about = "About Description Not Provided!";
                            if (!profile_cursor.isNull(about_index)) {
                                if (!profile_cursor.getString(about_index).trim().equals("")) about = "ABOUT\n\n" + profile_cursor.getString(about_index);
                            }
                            fragment_info.putString("name", businessName);
                            fragment_info.putString("info", about);
                            if(edit_view_flag) {
                                fragment_info.putInt("owner_id", owner_Id);
                                fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_textEditor.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            }
                            else fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            profile_cursor.close();
                        }
                    }
                    break;
                case R.id.inventory_button: // logic control for inventory button and switching to inventory screen
                    // setup for database queries
                    profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id, null);
                    if (profile_cursor != null){
                        if(profile_cursor.moveToFirst()){
                            int business_name_index = profile_cursor.getColumnIndex("business_name");
                            businessName = "Business Name";
                            if (!profile_cursor.isNull(business_name_index)) {
                                if (!profile_cursor.getString(business_name_index).trim().equals("")) businessName = profile_cursor.getString(business_name_index);
                            }
                            fragment_info.putString("name", businessName);
                            if (edit_view_flag) {
                                fragment_info.putInt("owner_id", owner_Id);
                                fragment_info.putBoolean("edit_flag", true);
                                fragment_manager.beginTransaction().replace(R.id.business_fragment_view, inventory_list.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            }
                            else {
                                fragment_info.putBoolean("edit_flag", false);
                                fragment_info.putInt("owner_id", owner_Id);
                                fragment_manager.beginTransaction().replace(R.id.business_fragment_view, inventory_list.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            }
                            profile_cursor.close();
                        }
                    }
                    break;

                case R.id.hours_button:  // logic hours for about button and switching to hours screen
                    // setup for database queries
                    profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id, null);
                    if (profile_cursor != null){
                        if(profile_cursor.moveToFirst()){
                            int business_name_index = profile_cursor.getColumnIndex("business_name");
                            businessName = "Business Name";
                            if (!profile_cursor.isNull(business_name_index)) {
                                if (!profile_cursor.getString(business_name_index).trim().equals("")) businessName = profile_cursor.getString(business_name_index);
                            }
                            int hours_index = profile_cursor.getColumnIndex("profile_hours_desc");
                            String hours = "Hours of Operation Not Provided!";
                            if (!profile_cursor.isNull(hours_index)) {
                                if (!profile_cursor.getString(hours_index).trim().equals("")) hours = profile_cursor.getString(hours_index);
                            }
                            fragment_info.putString("name", businessName);
                            if (edit_view_flag){
                                fragment_info.putInt("owner_id", owner_Id);
                                fragment_info.putString("info", hours);
                                fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_hours_editor.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            }
                            else {
                                hours = "HOURS OF OPERATION\n\n" + hours;
                                fragment_info.putString("info", hours);
                                fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            }
                            profile_cursor.close();
                        }
                    }
                    break;

                case R.id.layout_button:  // logic layout for about button and switching to layout screen
                    // setup for database queries
                    profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id, null);
                    if (profile_cursor != null){
                        if(profile_cursor.moveToFirst()){
                            int business_name_index = profile_cursor.getColumnIndex("business_name");
                            businessName = "Business Name";
                            if (!profile_cursor.isNull(business_name_index)) {
                                if (!profile_cursor.getString(business_name_index).trim().equals("")) businessName = profile_cursor.getString(business_name_index);
                            }
                            int layout_index = profile_cursor.getColumnIndex("profile_map_image");
                            String layout = "NULL";
                            if (!profile_cursor.isNull(layout_index)) {
                                if (!profile_cursor.getString(layout_index).trim().equals("")) layout = profile_cursor.getString(layout_index);
                            }
                            fragment_info.putString("name", businessName);
                            fragment_info.putString("info", layout);
                            if (edit_view_flag){
                                fragment_info.putInt("owner_id", owner_Id);
                                fragment_info.putString("info", "This page is coming soon!");
                                fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            }
                            else fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_image.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
                            profile_cursor.close();
                        }
                    }
                    break;
            }
            return true;
        });
    }

    //Method for handling the push of the back button on an android phone - Stephen
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


    //Method for handling the favorites button (to keep the code neat) -Blake
    private void HandleFavoritesButton(){

        add_to_fav_btn.setVisibility(View.VISIBLE);

        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        //Query the Favorited_Profiles collection in Firestore to see if this business is already
        //favorited by this supporter account  -Blake
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Favorited_Profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ProfileFavorites pf = new ProfileFavorites();
                        pf.setProf_fav_supporter_id(Integer.parseInt(doc.get("supporter_id").toString()));
                        pf.setProf_fav_owner_id(Integer.parseInt(doc.get("owner_id").toString()));
                        pf.setProf_fav_profile_id(Integer.parseInt(doc.get("profile_id").toString()));
                        System.out.println(Integer.parseInt(doc.get("supporter_id").toString()));

                        profile_favorites.add(pf);

                    }

                    //for testing -Blake
                    System.out.println("FAVORITES ARRAY LIST");
                    for (int i = 0; i < profile_favorites.size(); i++) {
                        System.out.println(profile_favorites.get(i));
                        System.out.println(supporter_Id);
                        System.out.println(owner_Id);
                    }
                    //check if this business profile has already been favorited by the supporter viewing it  -Blake
                    for(ProfileFavorites pf : profile_favorites){
                        if(pf.getProf_fav_owner_id() == owner_Id && pf.getProf_fav_supporter_id() == supporter_Id){
                            business_favorited = true;
                        }
                    }
                    if(business_favorited){
                        add_to_fav_btn.setText("Unfavorite");
                    }else{
                        add_to_fav_btn.setText("Favorite");
                    }
                    progressDialog.dismiss();


                }
            }

        });


        //set on-click listener for favorites button  -Blake
        add_to_fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                if(business_favorited){
                    CollectionReference DeleteBusinessFavorite = firebaseFirestore.collection("Favorited_Profiles");
                    Query query = DeleteBusinessFavorite.whereEqualTo("owner_id", String.valueOf(owner_Id));
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //DelRecModel drm = new DelRecModel();
                                    //drm.setId(document.get("id").toString());
                                    //String test = document.get("id").toString();
                                    //System.out.println("AAWEDRAWODNAOWIDN" + test);
                                    profile_favorites.removeIf(del -> String.valueOf(del.getProf_fav_owner_id()).equals(document.get("owner_id")));
                                    firebaseFirestore.collection("Favorited_Profiles").document(document.getId()).delete();

                                    //this refreshes the recyclerview after deletion; probably a weak workaround, callback interface might be better
                                    //recreate();

                                }
                            }
                        }

                    });
                    add_to_fav_btn.setText("Favorite");
                    business_favorited = false;
                    progressDialog.dismiss();
                    Toast.makeText(Business_Profile_Activity.this, "Business removed from favorites", Toast.LENGTH_SHORT).show();

                }else{
                    //add business to the Favorited_Profiles collection of Firestore database  -Blake
                    Map<String, Object> data = new HashMap<>();
                    data.put("owner_id", String.valueOf(owner_Id));
                    data.put("supporter_id", String.valueOf(supporter_Id));
                    //profile_id not defined yet
                    data.put("profile_id", String.valueOf(-999));
                    firebaseFirestore.collection("Favorited_Profiles").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                //ProfileFavorites pf = new ProfileFavorites();
                                //pf.setProf_fav_profile_id(owner_Id);
                                //pf.setProf_fav_profile_id(-999);
                                //pf.setProf_fav_supporter_id(supporter_Id);
                                add_to_fav_btn.setText("Unfavorite");
                                business_favorited = true;
                                progressDialog.dismiss();
                                Toast.makeText(Business_Profile_Activity.this, "Business Favorited", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }


    //Method for handling the messages button (to keep the code neat) - by Blake
    private void HandleMessagesButton(){

        direct_message_btn.setVisibility(View.VISIBLE);

        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        AllChatsClass ac = new AllChatsClass();
                        ac.setOwner_id(Integer.parseInt(doc.get("owner_id").toString()));
                        ac.setOwner_username(doc.get("owner_username").toString());
                        ac.setSupporter_id(Integer.parseInt(doc.get("supporter_id").toString()));
                        ac.setSupporter_username(doc.get("supporter_username").toString());
                        ac.setBusiness_name(doc.get("business_name").toString());
                        all_chats_arr_list.add(ac);
                    }


                    /*  -for testing
                    System.out.println("ALL CHATS ARRAY LIST");
                    for (int i = 0; i < all_chats_arr_list.size(); i++) {
                        System.out.println(all_chats_arr_list.get(i));
                        System.out.println(supporter_Id);
                        System.out.println(owner_Id);
                    */

                    //check if this there is already a chat between this supporter and business  - by Blake
                    for(AllChatsClass ac : all_chats_arr_list){
                        if(ac.getOwner_id() == owner_Id && ac.getSupporter_id() == supporter_Id){
                            chat_exists = true;
                        }
                    }
                    if(chat_exists){
                        direct_message_btn.setText("Message");
                    }else{
                        direct_message_btn.setText("Start Chat");
                    }

                    progressDialog.dismiss();


                }
            }

        });




        //set on-click listener for messages button  - by Blake
        direct_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                //get business username and name
                try{
                    Cursor business_name_cursor = db.rawQuery("SELECT business_name FROM profile WHERE owner_id = ?", new String[] {String.valueOf(owner_Id)});
                    Cursor owner_username_cursor = db.rawQuery("SELECT owner_username FROM owner_account WHERE owner_id = ?", new String[] {String.valueOf(owner_Id)});
                    if(business_name_cursor != null) {
                        if (business_name_cursor.moveToFirst()) {
                            int business_name_index = business_name_cursor.getColumnIndex("business_name");
                            business_name = (business_name_cursor.getString(business_name_index));
                            business_name_cursor.close();
                            //db.close();
                        }else{
                            business_name_cursor.close();
                            //db.close();
                        }

                    }else{
                        //db.close();
                    }

                    if(owner_username_cursor != null){
                        if(owner_username_cursor.moveToFirst()){
                            int owner_username_index = owner_username_cursor.getColumnIndex("owner_username");
                            owner_username_msg = (owner_username_cursor.getString(owner_username_index));
                        }else{
                            owner_username_cursor.close();
                            db.close();
                        }
                    }else{
                        db.close();
                    }

                }catch(SQLiteException e){
                    e.printStackTrace();
                }


                if(chat_exists){

                    /*
                            chats_bundle.putInt("supporter_id", supporter_id);
                            chats_bundle.putInt("owner_id", model.getOwner_id());
                            chats_bundle.putString("owner_username", model.getOwner_username());
                            chats_bundle.putString("supporter_username", supporter_username);
                            chats_bundle.putString("viewer_username", supporter_username);
                            chats_bundle.putString("viewed_username", owner_username);
                            to_individual_chat.putExtra("chats_bundle", chats_bundle);
                            startActivity(to_individual_chat);



                     */

                    Intent to_individual_chat = new Intent(Business_Profile_Activity.this, Individual_Chats_Activity.class);
                    Bundle chats_bundle = new Bundle();
                    chats_bundle.putInt("supporter_id", supporter_Id);
                    chats_bundle.putInt("owner_id", owner_Id);
                    chats_bundle.putString("owner_username", owner_username_msg);
                    chats_bundle.putString("supporter_username", supporter_username);
                    chats_bundle.putString("business_name", business_name);
                    chats_bundle.putString("viewer_username", supporter_username);
                    chats_bundle.putString("viewed_username", owner_username_msg);
                    to_individual_chat.putExtra("chats_bundle", chats_bundle);

                    progressDialog.dismiss();

                    startActivity(to_individual_chat);
                    finish();



                }else{
                    //add a chat to the Chats collection of Firestore database  - by Blake
                    Map<String, Object> data = new HashMap<>();
                    data.put("owner_id", owner_Id);
                    data.put("supporter_id", supporter_Id);
                    data.put("supporter_username", supporter_username);
                    data.put("owner_username", owner_username_msg);
                    data.put("business_name", business_name);
                    firebaseFirestore.collection("Chats").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Business_Profile_Activity.this, "Chat Started!", Toast.LENGTH_SHORT).show();
                                direct_message_btn.setText("Message");
                                chat_exists = true;
                                progressDialog.dismiss();
                            }
                        }
                    });
                }

            }
        });






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

        //this gets rid of the title of the app in the toolbar since there's no space after the new button were added
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, (R.string.supporter_drawer_open), (R.string.supporter_drawer_close));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //this allows the TextView/ImageView in the top of the drawer header to be manipulated
        header_view = main_nav_menu.getHeaderView(0);
        logo = (ImageView) header_view.findViewById(R.id.drawer_header_image);
        account_username = (TextView) header_view.findViewById(R.id.drawer_header_username);

        // remove owner menu items - added by Stephen
        Menu menu = main_nav_menu.getMenu();
        menu.removeItem(R.id.supporter_view);
        menu.removeItem(R.id.edit_supporter_view);
        menu.removeItem(R.id.account_management);
        menu.removeItem(R.id.owner_promotions);



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

                    /*
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

                     */
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
                            Intent to_dm = new Intent(Business_Profile_Activity.this, All_Chats_Activity.class);
                            Bundle dm_bundle = new Bundle();
                            dm_bundle.putInt("supporter_id", supporter_Id);
                            dm_bundle.putString("supporter_username", supporter_username);
                            to_dm.putExtra("dm_bundle", dm_bundle);
                            startActivity(to_dm);
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

