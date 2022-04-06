package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Business_Profile_Activity extends AppCompatActivity {

    private TextView owner_username_view;
    private Button guest_btn;
    private BottomNavigationView bottom_nav_menu;
    private FragmentManager fragment_manager;
    private int owner_Id;
    private int supporter_Id;
    private DatabaseHelper databaseHelper;
    private Cursor profile_cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);
        owner_username_view = findViewById(R.id.owner_username);
        guest_btn = findViewById(R.id.buss_pro_guest_btn);
        bottom_nav_menu = findViewById(R.id.profile_bottem_nav);

        databaseHelper = new DatabaseHelper(Business_Profile_Activity.this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        //receive the bundle passed from the BrowseProfilesAdapter (containing the owner_id of the business profile)
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("profile_bundle");
            if (bundle != null){
                owner_Id = bundle.getInt("owner_id");


                // provide supporter functionality
                if (bundle.containsKey("supporter_id") && bundle.containsKey("supporter_username")){
                    supporter_Id = bundle.getInt("supporter_id");
                    if(supporter_Id == -1) {
                        owner_username_view.setVisibility(View.GONE);
                        guest_btn.setVisibility(View.VISIBLE);
                        guestButtonSetup();
                    }
                    else {
                            owner_username_view.setVisibility(View.VISIBLE);
                            owner_username_view.setText("Hello, " + bundle.getString("supporter_username") + "!");
                            guest_btn.setVisibility(View.GONE);
                        }
                }
                else{ // provide owner functionality
                    //create cursor to move query the db and move through the query
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
                            owner_username_view.setVisibility(View.VISIBLE);
                            owner_username_view.setText("Hello, " + owner_cursor.getString(fname_index) + " " + owner_cursor.getString(lname_index) + "!");
                            guest_btn.setVisibility(View.GONE);
                        }
                    }
                }
                profile_cursor = db.rawQuery("SELECT * FROM profile WHERE owner_id=" + owner_Id,null);
                createAboutFragmentManager();
                BottomNavMenuSetup();
            }
        }
    }

    private void createAboutFragmentManager(){
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

        Bundle fragment_info = new Bundle();
        fragment_info.putString("name", name);
        fragment_info.putString("info", about);

        fragment_manager = getSupportFragmentManager();
        fragment_manager.beginTransaction().replace(R.id.business_fragment_view, business_profile_text.class, fragment_info).setReorderingAllowed(true).addToBackStack("name").commit();
    }

    private void guestButtonSetup(){
        //toolbar button
        guest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(Business_Profile_Activity.this, MainActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logout);
            }
        });
    }

    private void BottomNavMenuSetup(){
        bottom_nav_menu.setSelectedItemId(R.id.about_button);
        bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.about_button:
                    break;
                case R.id.inventory_button:
                    break;
                case R.id.hours_button:
                    break;
                case R.id.layout_button:
                    break;
                case R.id.faq_button:
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
    //    super.onBackPressed();
    }
}