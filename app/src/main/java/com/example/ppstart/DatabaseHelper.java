package com.example.ppstart;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DemoDatabase";
    private final static int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Initially just creating tables for supporter accounts, owner accounts, and owner profiles. Owner accounts include payment information

        //Create table that contains supporter account data
        String create_supporter_table = "CREATE TABLE supporter_account(supporter_id INTEGER PRIMARY KEY AUTOINCREMENT, supporter_username VARCHAR NOT NULL, supporter_password NOT NULL)";
        db.execSQL(create_supporter_table);

        //Create table that contains owner account data
        String create_owner_table = "CREATE TABLE owner_account(owner_id INTEGER PRIMARY KEY AUTOINCREMENT, owner_username VARCHAR NOT NULL, owner_password NOT NULL)";
        db.execSQL(create_owner_table);

        //create the table for business profiles
        String create_profile_table = "CREATE TABLE profile(profile_id INTEGER PRIMARY KEY AUTOINCREMENT, owner_id INTEGER, profile_avatar_image TEXT, profile_about_image TEXT, profile_about_desc TEXT, profile_hours_desc, profile_map_image TEXT, business_name VARCHAR, FOREIGN KEY(owner_id) REFERENCES owner_account(owner_id))";
        db.execSQL(create_profile_table);


        //insert an example supporter account
        ContentValues insert_supporter = new ContentValues();
        insert_supporter.put("supporter_username", "James_Smith");
        insert_supporter.put("supporter_password", "password");
        db.insert("supporter_account", null, insert_supporter);


        //insert 3 example owner accounts
        //owner account 1
        ContentValues insert_owner = new ContentValues();
        insert_owner.put("owner_username", "Michael_White");
        insert_owner.put("owner_password", "12345");
        db.insert("owner_account", null, insert_owner);

        //owner account 2
        ContentValues insert_owner2 = new ContentValues();
        insert_owner2.put("owner_username", "Samuel_Ben");
        insert_owner2.put("owner_password", "securepassword");
        db.insert("owner_account", null, insert_owner2);


        //owner account 3
        ContentValues insert_owner3 = new ContentValues();
        insert_owner3.put("owner_username", "David_Bach");
        insert_owner3.put("owner_password", "bluesky357");
        db.insert("owner_account", null, insert_owner3);


        //insert 3 example profiles into the profile database
        //example profile 1
        ContentValues insert_profile1 = new ContentValues();
        insert_profile1.put("owner_id", 1);
        insert_profile1.put("profile_avatar_image", "https://cdn-icons-png.flaticon.com/512/2083/2083417.png");
        insert_profile1.put("profile_about_image", "https://static.thenounproject.com/png/161182-200.png");
        insert_profile1.put("profile_about_desc", "This is a cool business");
        insert_profile1.put("profile_hours_desc", "Open 24/7/365");
        insert_profile1.put("profile_map_image", "https://i.pinimg.com/originals/78/a1/65/78a165f3db1121f23fe4524f40da2608.png");
        insert_profile1.put("business_name", "Bob's Antiques");

        db.insert("profile", null, insert_profile1);

        //example profile 2
        ContentValues insert_profile2 = new ContentValues();
        insert_profile2.put("owner_id", 2);
        insert_profile2.put("profile_avatar_image", "https://library.kissclipart.com/20190911/kw/kissclipart-clip-art-lollipop-confectionery-candy-5e6bed0693fcc508.png");
        insert_profile2.put("profile_about_image", "https://media.architecturaldigest.com/photos/55e7658d302ba71f3016531d/4:3/w_800,h_600,c_limit/dam-images-architecture-2015-02-candy-shops-beautiful-candy-shops-01-dylans-candy-bar.jpg");
        insert_profile2.put("profile_about_desc", "This is a candy shop");
        insert_profile2.put("profile_hours_desc", "Closed forever");
        insert_profile2.put("profile_map_image", "https://durfeehardware.com/wp-content/uploads/2016/10/Map_First_Floor.png");
        insert_profile2.put("business_name", "Sweetings");

        db.insert("profile", null, insert_profile2);

        //example profile 3
        ContentValues insert_profile3 = new ContentValues();
        insert_profile3.put("owner_id", 3);
        insert_profile3.put("profile_avatar_image", "https://media.istockphoto.com/photos/transistor-radio-receiver-on-wood-table-in-home-interior-3d-picture-id1139509180?k=20&m=1139509180&s=612x612&w=0&h=cmU7t-4-gL5Zd7NRj7rFLg6d8OjnecYclqj5fU_8dAk=");
        insert_profile3.put("profile_about_image", "https://cbradiomagazine.com/wp-content/uploads/2020/04/Pb150001.jpg");
        insert_profile3.put("profile_about_desc", "Cool radios");
        insert_profile3.put("profile_hours_desc", "Open only on Mondays at 1:00pm - 1:05pm");
        insert_profile3.put("profile_map_image", "https://www.littletraveler.com/wp-content/uploads/2014/05/storemap.png");
        insert_profile3.put("business_name", "Ramee's Radio Shop");

        db.insert("profile", null, insert_profile3);




    }







    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}