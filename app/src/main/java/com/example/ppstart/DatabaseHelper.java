//CREATED BY BLAKE - EDITED BY CHANDLER, JOHNATHAN, AND STEPHEN

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

        //owner accounts include payment info

        //Create table that contains supporter account data
        String create_supporter_table = "CREATE TABLE " +
                "supporter_account(supporter_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "supporter_username VARCHAR NOT NULL, " +
                "supporter_password VARCHAR NOT NULL, " +
                "supporter_avatar BLOB)";
        db.execSQL(create_supporter_table);

        //Create table that contains owner account data
        String create_owner_table = "CREATE TABLE " +
                "owner_account(owner_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "owner_username VARCHAR NOT NULL, " +
                "owner_password VARCHAR NOT NULL, " +
                "card_number VARCHAR NOT NULL, " +
                "first_name VARCHAR NOT NULL, " +
                "last_name VARCHAR NOT NULL, " +
                "address VARCHAR NOT NULL, " +
                "city VARCHAR NOT NULL, " +
                "state VARCHAR NOT NULL, " +
                "postal VARCHAR NOT NULL, " +
                "country VARCHAR NOT NULL)";
        //"longitude DOUBLE NOT NULL," +
        //"latitude DOUBLE NOT NULL)";
        db.execSQL(create_owner_table);

        //create the table for business profiles
        String create_profile_table = "CREATE TABLE " +
                "profile(profile_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "owner_id INTEGER, " +
                "profile_avatar_image TEXT, " +
                "profile_about_image TEXT, " +
                "profile_about_desc TEXT, " +
                "profile_hours_desc TEXT, " +
                "profile_map_image TEXT, " +
                "business_name VARCHAR, " +
                "latitude DOUBLE NOT NULL," +
                "longitude DOUBLE NOT NULL," +
                "FOREIGN KEY(owner_id) " +
                "REFERENCES owner_account(owner_id))";
        db.execSQL(create_profile_table);

        //create the table for businesses favorited by a supporter account
        String create_profile_favorites = "CREATE TABLE " +
                "profile_favorites(supporter_id INTEGER, " +
                "owner_id INTEGER, " +
                "profile_id INTEGER, " +
                "PRIMARY KEY(supporter_id, owner_id, profile_id))";
        db.execSQL(create_profile_favorites);

        //create the table for item
        String create_item = "CREATE TABLE item" +
                "(item_number VARCHAR PRIMARY KEY, " +
                "item_name VARCHAR NOT NULL, " +
                "item_desc TEXT NOT NULL, " +
                "item_picture TEXT)";
        db.execSQL(create_item);

        //create inventory for stores
        String create_store_inventory = "CREATE TABLE " +
                "store_inventory(item_number VARCHAR, " +
                "profile_id INTEGER, " +
                "owner_id INTEGER, " +
                "price DECIMAL NOT NULL, " +
                "additional_information TEXT, " +
                "PRIMARY KEY(item_number, owner_id, profile_id))";
        db.execSQL(create_store_inventory);

        //Create the table for discounts and promotions
        String create_discounts_and_promos_table = "CREATE TABLE promos(dp_id INTEGER PRIMARY KEY, store_id INTEGER, item_id INTEGER, dp_desc VARCHAR, FOREIGN KEY (store_id) REFERENCES profile (profile_id), FOREIGN KEY (item_id) REFERENCES item(item_number))";
        db.execSQL(create_discounts_and_promos_table);


        //insert an example supporter account
        ContentValues insert_supporter = new ContentValues();
        insert_supporter.put("supporter_username", "James_Smith");
        insert_supporter.put("supporter_password", "password");
        db.insert("supporter_account", null, insert_supporter);


        //insert 3 example owner accounts
        //owner account 1
        ContentValues insert_owner = new ContentValues();
        insert_owner.put("owner_username", "Bobs_Antiques");
        insert_owner.put("owner_password", "password");
        insert_owner.put("card_number", "1234567887654321");
        insert_owner.put("first_name", "Michael");
        insert_owner.put("last_name", "White");
        insert_owner.put("address", "123 My Street");
        insert_owner.put("city", "Lubbock");
        insert_owner.put("state", "Texas");
        insert_owner.put("postal", "79413");
        insert_owner.put("country", "USA");

        db.insert("owner_account", null, insert_owner);

        //owner account 2
        ContentValues insert_owner2 = new ContentValues();
        insert_owner2.put("owner_username", "Sweetings");
        insert_owner2.put("owner_password", "password");
        insert_owner2.put("card_number", "1234567887654321");
        insert_owner2.put("first_name", "Samuel");
        insert_owner2.put("last_name", "Ben");
        insert_owner2.put("address", "124 My Street");
        insert_owner2.put("city", "Lubbock");
        insert_owner2.put("state", "Texas");
        insert_owner2.put("postal", "79413");
        insert_owner2.put("country", "USA");

        db.insert("owner_account", null, insert_owner2);


        //owner account 3
        ContentValues insert_owner3 = new ContentValues();
        insert_owner3.put("owner_username", "Ramees_Radio_Shop");
        insert_owner3.put("owner_password", "password");
        insert_owner3.put("card_number", "1234567887654321");
        insert_owner3.put("first_name", "David");
        insert_owner3.put("last_name", "Bach");
        insert_owner3.put("address", "125 My Street");
        insert_owner3.put("city", "Lubbock");
        insert_owner3.put("state", "Texas");
        insert_owner3.put("postal", "79413");
        insert_owner3.put("country", "USA");

        db.insert("owner_account", null, insert_owner3);

        //Inserting in a fake promo
        ContentValues insert_test_promo1 = new ContentValues();
        insert_test_promo1.put("dp_id", 1);
        insert_test_promo1.put("store_id", 2);
        insert_test_promo1.put("item_id", 1);
        insert_test_promo1.put("dp_desc", "20% Off.");

        db.insert("promos", null, insert_test_promo1);

        //Inserting in a fake promo
        ContentValues insert_test_promo2 = new ContentValues();
        insert_test_promo2.put("dp_id", 2);
        insert_test_promo2.put("store_id", 1);
        insert_test_promo2.put("item_id", 2);
        insert_test_promo2.put("dp_desc", "Buy 1 get 1 half off.");

        db.insert("promos", null, insert_test_promo2);

        //Inserting in a fake promo
        ContentValues insert_test_promo3 = new ContentValues();
        insert_test_promo3.put("dp_id", 3);
        insert_test_promo3.put("store_id", 1);
        insert_test_promo3.put("item_id", 3);
        insert_test_promo3.put("dp_desc", "10% Off.");

        db.insert("promos", null, insert_test_promo3);


        //insert 3 example profiles into the profile database
        //example profile 1
        ContentValues insert_profile1 = new ContentValues();
        insert_profile1.put("owner_id", 1);
        insert_profile1.put("profile_avatar_image", "https://cdn-icons-png.flaticon.com/512/2083/2083417.png");
        insert_profile1.put("profile_about_image", "https://static.thenounproject.com/png/161182-200.png");
        insert_profile1.put("profile_about_desc", "This is a cool business");
        insert_profile1.put("profile_hours_desc", "Monday 12:00AM - 11:59PM\nTuesday 12:00AM - 11:59PM\nWednesday 12:00AM - 11:59PM\nThursday 12:00AM - 11:59PM\nFriday 12:00AM - 11:59PM\nSaturday 12:00AM - 11:59PM\nSunday 12:00AM - 11:59PM");
        insert_profile1.put("profile_map_image", "https://i.pinimg.com/originals/78/a1/65/78a165f3db1121f23fe4524f40da2608.png");
        insert_profile1.put("business_name", "Bob's Antiques");
        insert_profile1.put("latitude", "33.570000");
        insert_profile1.put("longitude", "-101.865518");

        db.insert("profile", null, insert_profile1);

        //example profile 2
        ContentValues insert_profile2 = new ContentValues();
        insert_profile2.put("owner_id", 2);
        insert_profile2.put("profile_avatar_image", "https://library.kissclipart.com/20190911/kw/kissclipart-clip-art-lollipop-confectionery-candy-5e6bed0693fcc508.png");
        insert_profile2.put("profile_about_image", "https://media.architecturaldigest.com/photos/55e7658d302ba71f3016531d/4:3/w_800,h_600,c_limit/dam-images-architecture-2015-02-candy-shops-beautiful-candy-shops-01-dylans-candy-bar.jpg");
        insert_profile2.put("profile_about_desc", "This is a candy shop");
        insert_profile2.put("profile_hours_desc", "Monday Closed\nTuesday Closed\nWednesday Closed\nThursday Closed\nFriday Closed\nSaturday Closed\nSunday Closed");
        insert_profile2.put("profile_map_image", "https://durfeehardware.com/wp-content/uploads/2016/10/Map_First_Floor.png");
        insert_profile2.put("business_name", "Sweetings");
        insert_profile2.put("latitude", "33.5735781");
        insert_profile2.put("longitude", "-101.865518");

        db.insert("profile", null, insert_profile2);

        //example profile 3
        ContentValues insert_profile3 = new ContentValues();
        insert_profile3.put("owner_id", 3);
        insert_profile3.put("profile_avatar_image", "https://media.istockphoto.com/photos/transistor-radio-receiver-on-wood-table-in-home-interior-3d-picture-id1139509180?k=20&m=1139509180&s=612x612&w=0&h=cmU7t-4-gL5Zd7NRj7rFLg6d8OjnecYclqj5fU_8dAk=");
        insert_profile3.put("profile_about_image", "https://cbradiomagazine.com/wp-content/uploads/2020/04/Pb150001.jpg");
        insert_profile3.put("profile_about_desc", "Cool radios");
        insert_profile3.put("profile_hours_desc", "Monday 1:00pm - 1:05pm\nTuesday Closed\nWednesday Closed\nThursday Closed\nFriday Closed\nSaturday Closed\nSunday Closed");
        insert_profile3.put("profile_map_image", "https://www.littletraveler.com/wp-content/uploads/2014/05/storemap.png");
        insert_profile3.put("business_name", "Ramee's Radio Shop");
        insert_profile3.put("latitude", "34.5735781");
        insert_profile3.put("longitude", "-101.865518");

        db.insert("profile", null, insert_profile3);

        //inserting a fake favorited profile for the sake of testing
        ContentValues insert_test_profile_favorite1 = new ContentValues();
        insert_test_profile_favorite1.put("supporter_id", 1098);
        insert_test_profile_favorite1.put("owner_id", 1);
        insert_test_profile_favorite1.put("profile_id", 1);

        db.insert("profile_favorites", null, insert_test_profile_favorite1);

        //Inserting a fake item1
        ContentValues insert_test_item1 = new ContentValues();
        insert_test_item1.put("item_number", "1");
        insert_test_item1.put("item_name", "Gumballs");
        insert_test_item1.put("item_desc", "A 20 pack of gumballs");

        db.insert("item", null, insert_test_item1);

        //Inserting a fake item2
        ContentValues insert_test_item2 = new ContentValues();
        insert_test_item2.put("item_number", "2");
        insert_test_item2.put("item_name", "Cinnamon Toast Crunch");
        insert_test_item2.put("item_desc", "1 LB box");

        db.insert("item", null, insert_test_item2);

        //Inserting a fake item3
        ContentValues insert_test_item3 = new ContentValues();
        insert_test_item3.put("item_number", "3");
        insert_test_item3.put("item_name", "Paper");
        insert_test_item3.put("item_desc", "White: 8.5\" x 11\"");

        db.insert("item", null, insert_test_item3);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}