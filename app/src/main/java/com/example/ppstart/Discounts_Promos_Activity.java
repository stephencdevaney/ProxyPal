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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Discounts_Promos_Activity extends AppCompatActivity {

    private ArrayList<Promo> promosList;
    private RecyclerView rvPromos;
    private DiscountsPromosAdapter dpAdapter;
    private BottomNavigationView supporter_bottom_nav_menu;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounts_promos);
        promosList = new ArrayList<>();
        rvPromos = findViewById(R.id.dp_rv);

        supporter_bottom_nav_menu = findViewById(R.id.supporter_bottom_nav_menu);
        initBottomNavigationView();
        setPromos();
        setPromosAdapter();

    }

    private void setPromosAdapter() {
        DiscountsPromosAdapter dpAdapter = new DiscountsPromosAdapter(promosList);
        RecyclerView.LayoutManager dpLayout = new LinearLayoutManager(getApplicationContext());
        rvPromos.setLayoutManager(dpLayout);
        rvPromos.setItemAnimator(new DefaultItemAnimator());
        rvPromos.setAdapter(dpAdapter);
    }

    private void setPromos() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        //promosList.add(new Promo("1", "1","1", "Store Name", null, "Description", "Product Name"));

        try{
            Cursor pCursor = db.rawQuery("SELECT * FROM promotions", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try{
            Cursor pCursor = db.rawQuery("SELECT * FROM promos", null);
            if(pCursor != null){
                if(pCursor.moveToFirst()){

                    int dp_id_index = pCursor.getColumnIndex("dp_id");
                    int store_id_index = pCursor.getColumnIndex("store_id");
                    int item_id_index = pCursor.getColumnIndex("item_id");
                    int dp_desc_index = pCursor.getColumnIndex("dp_desc");
                    for(int i = 0; i < pCursor.getCount(); i++){
                        Promo p  = new Promo();

                        p.setDp_id(pCursor.getString(dp_id_index));
                        p.setStore_id(pCursor.getString(store_id_index));
                        p.setItem_id(pCursor.getString(item_id_index));
                        p.setDp_desc(pCursor.getString(dp_desc_index));

                        Cursor sCursor = db.rawQuery("SELECT business_name FROM profile WHERE profile_id = ?", new String[] {p.getStore_id()});
                        Cursor iCursor = db.rawQuery("Select item_name, item_desc, item_picture FROM item WHERE item_number = ?",new String[] {p.getItem_id()});

                        int business_name_index = sCursor.getColumnIndex("business_name");
                        int item_name_index = iCursor.getColumnIndex("item_name");
                        int item_desc_index = iCursor.getColumnIndex("item_desc");
                        int item_picture_index = iCursor.getColumnIndex("item_picture");

                        p.setStore_name(sCursor.getString(business_name_index));
                        p.setItem_name(iCursor.getString(item_name_index));
                        p.setDp_desc(iCursor.getString(item_desc_index));
                        p.setItem_image(iCursor.getBlob(item_picture_index));

                        promosList.add(p);
                        sCursor.close();
                        iCursor.close();
                        pCursor.moveToNext();
                    }
                    pCursor.close();
                    db.close();
                }else{
                    pCursor.close();
                    db.close();
                }
            }else{
                db.close();
            }

        }catch(SQLiteException e){
            e.printStackTrace();
        }*/
    }


    private void initBottomNavigationView() {
        //sets the explore page as the default page
        supporter_bottom_nav_menu.setSelectedItemId(R.id.discounts_and_promos);

        //Set the listener for the bottom navigation menu buttons
        //(note: use this since the onNavigationItemSelected is deprecated)
        supporter_bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //switch to the Supporter_Search_Activity activity when the "search" button at the bottom is tapped
                case R.id.search:
                    Intent to_search = new Intent(Discounts_Promos_Activity.this, Supporter_Search_Activity.class);
                    startActivity(to_search);
                    break;
                //switch to the Supporter_Main_Page_Activity activity when the "explore" button at the bottom is tapped
                case R.id.explore:
                    Intent to_explore = new Intent(Discounts_Promos_Activity.this, Supporter_Main_Page_Activity.class);
                    startActivity(to_explore);
                    break;
                //switch to the Discounts_Promos_Activity activity when the "Discounts & Promos" button at the bottom is tapped
                case R.id.discounts_and_promos:
                    break;
                //switch to the Shopping_List_Activity activity when the "Shopping List" button at the bottom is tapped
                case R.id.shopping:
                    Intent to_shopping_list = new Intent(Discounts_Promos_Activity.this, Shopping_List_Activity.class);
                    startActivity(to_shopping_list);
                    break;
                default:
                    break;
            }

            return true;
        });

    }


}