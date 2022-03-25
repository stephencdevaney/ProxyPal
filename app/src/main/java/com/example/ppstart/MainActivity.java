package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {


    //Declare the interactive UI elements displayed in the initial page
    private ImageView initial_page_logo;
    private Button initial_login_btn, initial_create_btn, initial_guest_btn;

    //for testing
    private TextView main_test_txt;
    private DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the ImageView that contains the logo
        initial_page_logo = findViewById(R.id.initial_page_logo);

        //Add in the logo image using Glide
        Glide.with(this)
                .asBitmap()
                .load("https://lh3.googleusercontent.com/-L7JVyEiD-o8/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rf7Ew0ZUgwOZ67ca_-sewJ0V34XYA/mo/photo.jpg=k-s128")  //change this
                .into(initial_page_logo);

        //Initialize the buttons that will be displayed when the app is first launched
        initial_login_btn = findViewById(R.id.initial_login_btn);
        initial_create_btn = findViewById(R.id.initial_create_btn);
        initial_guest_btn = findViewById(R.id.initial_guest_btn);


        main_test_txt = findViewById(R.id.main_test_txt);
        main_test_txt.setText("testing git commit");
         main_test_txt.setText("testing git pull");
        main_test_txt.setText("testing git commit 2");

        /* ----------------------------------------------------------------
        DATABASE ALTERATION HISTORY
        String create_subscription_table = "CREATE TABLE test_table(database_test TEXT)";
        db.execSQL(create_subscription_table);

       NOTE - ADDED THESE COLUMNS TO OWNER ACCOUNT TABLE (do so in the database initialization of the main project)
        String alter_owner1 = "ALTER TABLE owner_account ADD card_number INTEGER";
        db.execSQL(alter_owner1);

        String alter_owner2 = "ALTER TABLE owner_account ADD first_name VARCHAR";
        db.execSQL(alter_owner2);

        String alter_owner3 = "ALTER TABLE owner_account ADD last_name VARCHAR";
        db.execSQL(alter_owner3);

        String alter_owner4 = "ALTER TABLE owner_account ADD address VARCHAR";
        db.execSQL(alter_owner4);

        String alter_owner5 = "ALTER TABLE owner_account ADD city VARCHAR";
        db.execSQL(alter_owner5);

        String alter_owner6 = "ALTER TABLE owner_account ADD state VARCHAR";
        db.execSQL(alter_owner6);

        String alter_owner7 = "ALTER TABLE owner_account ADD postal INTEGER";
        db.execSQL(alter_owner7);

        String alter_owner8 = "ALTER TABLE owner_account ADD country VARCHAR";
        db.execSQL(alter_owner8);

        >>>>>>>>>>>>>
        Added 3/20:
        String create_profile_table = "CREATE TABLE profile(profile_id INTEGER PRIMARY KEY AUTOINCREMENT, owner_id INTEGER, profile_avatar_image TEXT, profile_about_image TEXT, profile_about_desc TEXT, profile_hours_desc, profile_map_image TEXT, FOREIGN KEY(owner_id) REFERENCES owner_account(owner_id))";
        db.execSQL(create_profile_table);

        String alter_profile1 = "ALTER TABLE profile ADD business_name VARCHAR";
        db.execSQL(alter_profile1);

        ContentValues insert_profile1 = new ContentValues();
        insert_profile1.put("owner_id", 1);
        insert_profile1.put("profile_avatar_image", "https://cdn-icons-png.flaticon.com/512/2083/2083417.png");
        insert_profile1.put("profile_about_image", "https://static.thenounproject.com/png/161182-200.png");
        insert_profile1.put("profile_about_desc", "This is a cool business");
        insert_profile1.put("profile_hours_desc", "Open 24/7/365");
        insert_profile1.put("profile_map_image", "https://i.pinimg.com/originals/78/a1/65/78a165f3db1121f23fe4524f40da2608.png");
        insert_profile1.put("business_name", "Bob's Antiques");

        db.insert("profile", null, insert_profile1);

        ContentValues insert_profile2 = new ContentValues();
        insert_profile2.put("owner_id", 2);
        insert_profile2.put("profile_avatar_image", "https://library.kissclipart.com/20190911/kw/kissclipart-clip-art-lollipop-confectionery-candy-5e6bed0693fcc508.png");
        insert_profile2.put("profile_about_image", "https://media.architecturaldigest.com/photos/55e7658d302ba71f3016531d/4:3/w_800,h_600,c_limit/dam-images-architecture-2015-02-candy-shops-beautiful-candy-shops-01-dylans-candy-bar.jpg");
        insert_profile2.put("profile_about_desc", "This is a candy shop");
        insert_profile2.put("profile_hours_desc", "Closed forever");
        insert_profile2.put("profile_map_image", "https://durfeehardware.com/wp-content/uploads/2016/10/Map_First_Floor.png");
        insert_profile2.put("business_name", "Sweetings");

        db.insert("profile", null, insert_profile2);


        ContentValues insert_profile3 = new ContentValues();
        insert_profile3.put("owner_id", 3);
        insert_profile3.put("profile_avatar_image", "https://www.pngitem.com/pimgs/m/485-4856996_transparent-radio-clipart-transparent-background-radio-clip-art.png"); //changed since it didn't work
        insert_profile3.put("profile_about_image", "https://cbradiomagazine.com/wp-content/uploads/2020/04/Pb150001.jpg");
        insert_profile3.put("profile_about_desc", "Cool radios");
        insert_profile3.put("profile_hours_desc", "Open only on Mondays at 1:00pm - 1:05pm");
        insert_profile3.put("profile_map_image", "https://www.littletraveler.com/wp-content/uploads/2014/05/storemap.png");
        insert_profile3.put("business_name", "Ramee's Radio Shop");

        db.insert("profile", null, insert_profile3);


        ContentValues update_profile3 = new ContentValues();
        update_profile3.put("profile_avatar_image", "https://media.istockphoto.com/photos/transistor-radio-receiver-on-wood-table-in-home-interior-3d-picture-id1139509180?k=20&m=1139509180&s=612x612&w=0&h=cmU7t-4-gL5Zd7NRj7rFLg6d8OjnecYclqj5fU_8dAk=");
        db.update("profile", update_profile3, "business_name=?", new String[]{"Ramee's Radio Shop"});

      ---------------------------------------------------------------------
        //START OF TESTING CODE
        //These statements and all of the commented code below are for database testing purposes, you
        //can ignore these or study them to get an idea of how operations on the database work
        main_test_txt = findViewById(R.id.main_test_txt);
        main_test_txt.setVisibility(View.VISIBLE);


        databaseHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();


        This block here demonstrates how to set UI element information using the database
        Cursor cursor = db.rawQuery("SELECT * FROM owner_account", null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                cursor.moveToNext();
                int owner_id = cursor.getColumnIndex("owner_id");
                int owner_username = cursor.getColumnIndex("owner_username");
                int owner_password = cursor.getColumnIndex("owner_password");

                main_test_txt.setText(String.valueOf(cursor.getInt(owner_id)) + " " + cursor.getString(owner_username) + " " + cursor.getString(owner_password));
            }
        }
      ----------------------------------------
        This block here tested to see if the database was being updated when an owner account was created
        Cursor cursor = db.rawQuery("SELECT * FROM owner_account", null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                int owner_id = cursor.getColumnIndex("owner_id");
                int owner_username = cursor.getColumnIndex("owner_username");
                int owner_password = cursor.getColumnIndex("owner_password");
                int owner_card_num = cursor.getColumnIndex("card_number");
                int owner_fname = cursor.getColumnIndex("first_name");
                int owner_lname = cursor.getColumnIndex("last_name");
                int owner_address = cursor.getColumnIndex("address");
                int owner_city = cursor.getColumnIndex("city");
                int owner_state = cursor.getColumnIndex("state");
                int owner_postal = cursor.getColumnIndex("postal");
                int owner_country = cursor.getColumnIndex("country");


                main_test_txt.setText(String.valueOf(cursor.getInt(owner_id)) + " " + cursor.getString(owner_username) + " " + cursor.getString(owner_password) + " " + String.valueOf(cursor.getInt(owner_card_num)) + " " + cursor.getString(owner_fname) + " "
                + cursor.getString(owner_lname) + " " + cursor.getString(owner_address) + " " + cursor.getString(owner_city) + " " + cursor.getString(owner_state) + " " + cursor.getString(owner_postal) + " " + cursor.getString(owner_country));
            }
        }


        ---------------------------------
        This block here is for testing to see if the profile table updated correctly
        Cursor cursor = db.rawQuery("SELECT * FROM profile", null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                int profile_id = cursor.getColumnIndex("profile_id");
                int owner_id = cursor.getColumnIndex("owner_id");
                int business_name = cursor.getColumnIndex("business_name");
                int profile_avatar_image = cursor.getColumnIndex("profile_avatar_image");


                main_test_txt.setText(String.valueOf(cursor.getInt(profile_id)) + " " + cursor.getString(owner_id) + " " + cursor.getString(business_name) + " " + cursor.getString(profile_avatar_image));
            }
        }
        -------------------------------

        END OF TESTING CODE
        ------------------------ */



        //Set the on-click listener for the login button
        initial_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_login_activity = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(to_login_activity);
            }
        });

        //Set the on-click listener for the create account button
        initial_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When the create account button is tapped, the app switches to the initial create account activity
                Intent to_create_activity = new Intent(MainActivity.this, Initial_Create_Activity.class);
                startActivity(to_create_activity);
            }
        });

        //Set the on-click listener for the browse as guest button button
        initial_guest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When the browse as guest button is tapped, the app switches to the initial guest browsing activity
                Intent to_guest_activity = new Intent();

            }
        });


    }
}
