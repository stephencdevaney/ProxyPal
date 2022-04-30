package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Discounts_Promos_Activity extends AppCompatActivity {

    private ArrayList<Promo> promosList;
    private RecyclerView rvPromos;
    private DiscountsPromosAdapter dpAdapter;
    private BottomNavigationView supporter_bottom_nav_menu;
    private DatabaseHelper databaseHelper;

    private String supporter_username;
    private int supporter_id;
    private String owner_username;
    private int owner_id;

    private Bundle supporter_bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounts_promos);

        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getBundleExtra("promo_bundle");
            if (bundle != null){
                if (bundle.containsKey("supporter_id")){
                    supporter_username = bundle.getString("supporter_username");
                    supporter_id = bundle.getInt("supporter_id");
                }
                else{
                    owner_username = bundle.getString("owner_username");
                    owner_id = bundle.getInt("owner_id");
                }
            }
        }

        promosList = new ArrayList<>();
        rvPromos = findViewById(R.id.dp_rv);

        //bottom nav bar fix
        supporter_bottom_nav_menu = findViewById(R.id.supporter_bottom_nav_menu);
        if(supporter_id > 0) {
            initBottomNavigationView();
            supporter_bottom_nav_menu.setVisibility(View.VISIBLE);
        }else{
            supporter_bottom_nav_menu.setVisibility(View.GONE);
        }

        setPromos();
        setPromosAdapter();

    }

    private void setPromosAdapter() {
        DiscountsPromosAdapter dpAdapter = new DiscountsPromosAdapter(promosList);
        RecyclerView.LayoutManager dpLayout = new LinearLayoutManager(getApplicationContext());
        rvPromos.setLayoutManager(dpLayout);
        rvPromos.setItemAnimator(new DefaultItemAnimator());
        rvPromos.setAdapter(dpAdapter);
        if (supporter_id > 0)
            dpAdapter.passUserInfo(supporter_id, supporter_username);
        else
            dpAdapter.passOwnerInfo(owner_id, owner_username);
    }

    private void setPromos() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        if(supporter_id > 0) {
            try {
                Cursor pCursor = db.rawQuery("SELECT * FROM promos", null);
                //For the promos tab, we want to grab all promos from the database. A filter will be added to allow the user to better sort.
                if (pCursor != null) {
                    if (pCursor.moveToFirst()) {
                        int dp_id_index = pCursor.getColumnIndex("dp_id");
                        int store_id_index = pCursor.getColumnIndex("store_id");
                        int item_id_index = pCursor.getColumnIndex("item_id");
                        int dp_desc_index = pCursor.getColumnIndex("dp_desc");
                        for (int i = 0; i < pCursor.getCount(); i++) { //This allows us to create all promos objects that have been found in the database.
                            Promo p = new Promo();

                            p.setDp_id(pCursor.getInt(dp_id_index));
                            p.setStore_id(pCursor.getInt(store_id_index));
                            p.setItem_id(pCursor.getInt(item_id_index));
                            p.setDp_desc(pCursor.getString(dp_desc_index));

                            //2 Cursors to find information about the store and the item which are related to the promo.
                            Cursor sCursor = db.rawQuery("SELECT business_name FROM profile WHERE profile_id = ?", new String[]{String.valueOf(p.getStore_id())});
                            Cursor iCursor = db.rawQuery("Select item_name, item_picture FROM item WHERE item_number = ?", new String[]{String.valueOf(p.getItem_id())});
                            if (sCursor.moveToFirst()) {
                                int business_name_index = sCursor.getColumnIndex("business_name");
                                p.setStore_name(sCursor.getString(business_name_index));
                            }
                            if (iCursor.moveToFirst()) {
                                int item_name_index = iCursor.getColumnIndex("item_name");
                                int item_picture_index = iCursor.getColumnIndex("item_picture");
                                p.setItem_name(iCursor.getString(item_name_index));
                                p.setItem_image(iCursor.getString(item_picture_index));
                            }

                            promosList.add(p);
                            sCursor.close();
                            iCursor.close();
                            pCursor.moveToNext();
                        }
                        pCursor.close();
                        db.close();
                    } else {
                        pCursor.close();
                        db.close();
                    }
                } else {
                    db.close();
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                Cursor pCursor = db.rawQuery("SELECT * FROM promos", null);
                //For the promos tab, we want to grab all promos from the database. A filter will be added to allow the user to better sort.
                if (pCursor != null) {
                    if (pCursor.moveToFirst()) {
                        int dp_id_index = pCursor.getColumnIndex("dp_id");
                        int store_id_index = pCursor.getColumnIndex("store_id");
                        int item_id_index = pCursor.getColumnIndex("item_id");
                        int dp_desc_index = pCursor.getColumnIndex("dp_desc");
                        for (int i = 0; i < pCursor.getCount(); i++) { //This allows us to create all promos objects that have been found in the database.
                            Promo p = new Promo();

                            p.setDp_id(pCursor.getInt(dp_id_index));
                            p.setStore_id(pCursor.getInt(store_id_index));
                            p.setItem_id(pCursor.getInt(item_id_index));
                            p.setDp_desc(pCursor.getString(dp_desc_index));

                            //2 Cursors to find information about the store and the item which are related to the promo.
                            Cursor sCursor = db.rawQuery("SELECT business_name, owner_id FROM profile WHERE profile_id = ?", new String[]{String.valueOf(p.getStore_id())});
                            Cursor iCursor = db.rawQuery("SELECT item_name, item_picture FROM item WHERE item_number = ?", new String[]{String.valueOf(p.getItem_id())});

                            int temp_owner_id = 0;
                            if (sCursor.moveToFirst()) {
                                int business_name_index = sCursor.getColumnIndex("business_name");
                                int owner_id_index = sCursor.getColumnIndex("owner_id");
                                p.setStore_name(sCursor.getString(business_name_index));
                                temp_owner_id = sCursor.getInt(owner_id_index);
                            }
                            if (iCursor.moveToFirst()) {
                                int item_name_index = iCursor.getColumnIndex("item_name");
                                int item_picture_index = iCursor.getColumnIndex("item_picture");
                                p.setItem_name(iCursor.getString(item_name_index));
                                p.setItem_image(iCursor.getString(item_picture_index));
                            }

                            if (temp_owner_id == owner_id)
                                promosList.add(p);
                            sCursor.close();
                            iCursor.close();
                            pCursor.moveToNext();
                        }
                        pCursor.close();
                        db.close();
                    } else {
                        pCursor.close();
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



    private void initBottomNavigationView() {
        //sets the discounts/promos page as the default page
        supporter_bottom_nav_menu.setSelectedItemId(R.id.discounts_and_promos);

        //Set the listener for the bottom navigation menu buttons
        //(note: use this since the onNavigationItemSelected is deprecated)
        supporter_bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //switch to the Supporter_Search_Activity activity when the "search" button at the bottom is tapped
                case R.id.search:
                    Intent to_search = new Intent(Discounts_Promos_Activity.this, Supporter_Search_Activity.class);
                    supporter_bundle = new Bundle();
                    supporter_bundle.putInt("supporter_id", supporter_id);
                    supporter_bundle.putString("supporter_username", supporter_username);
                    to_search.putExtra("supporter_bundle", supporter_bundle);
                    startActivity(to_search);
                    break;
                //switch to the Supporter_Main_Page_Activity activity when the "explore" button at the bottom is tapped
                case R.id.explore:
                    Intent to_explore = new Intent(Discounts_Promos_Activity.this, Supporter_Main_Page_Activity.class);
                    supporter_bundle = new Bundle();
                    supporter_bundle.putInt("supporter_id", supporter_id);
                    supporter_bundle.putString("supporter_username", supporter_username);
                    to_explore.putExtra("supporter_bundle", supporter_bundle);
                    startActivity(to_explore);
                    break;
                //switch to the Discounts_Promos_Activity activity when the "Discounts & Promos" button at the bottom is tapped
                case R.id.discounts_and_promos:
                    break;
                //switch to the Shopping_List_Activity activity when the "Shopping List" button at the bottom is tapped
                case R.id.shopping:
                    Intent to_shopping_list = new Intent(Discounts_Promos_Activity.this, Shopping_List_Activity.class);
                    supporter_bundle = new Bundle();
                    supporter_bundle.putInt("supporter_id", supporter_id);
                    supporter_bundle.putString("supporter_username", supporter_username);
                    to_shopping_list.putExtra("supporter_bundle", supporter_bundle);
                    startActivity(to_shopping_list);
                    break;
                default:
                    break;
            }

            return true;
        });

    }




}