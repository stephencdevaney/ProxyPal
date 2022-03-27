package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Shopping_List_Activity extends AppCompatActivity {

    private BottomNavigationView supporter_bottom_nav_menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        supporter_bottom_nav_menu = findViewById(R.id.supporter_bottom_nav_menu);
        initBottomNavigationView();



    }



    private void initBottomNavigationView() {
        //sets the explore page as the default page
        supporter_bottom_nav_menu.setSelectedItemId(R.id.shopping);

        //Set the listener for the bottom navigation menu buttons
        //(note: use this since the onNavigationItemSelected is deprecated)
        supporter_bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //switch to the Supporter_Search_Activity activity when the "search" button at the bottom is tapped
                case R.id.search:
                    Intent to_search = new Intent(Shopping_List_Activity.this, Supporter_Search_Activity.class);
                    startActivity(to_search);
                    break;
                //switch to the Supporter_Main_Page_Activity activity when the "explore" button at the bottom is tapped
                case R.id.explore:
                    Intent to_explore = new Intent(Shopping_List_Activity.this, Supporter_Main_Page_Activity.class);
                    startActivity(to_explore);
                    break;
                //switch to the Discounts_Promos_Activity activity when the "Discounts & Promos" button at the bottom is tapped
                case R.id.discounts_and_promos:
                    Intent to_dp = new Intent(Shopping_List_Activity.this, Discounts_Promos_Activity.class);
                    startActivity(to_dp);
                    break;
                //switch to the Shopping_List_Activity activity when the "Shopping List" button at the bottom is tapped
                case R.id.shopping:
                    break;
                default:
                    break;
            }

            return true;
        });

    }
}