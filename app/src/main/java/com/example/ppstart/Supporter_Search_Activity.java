package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Supporter_Search_Activity extends AppCompatActivity {

    private MaterialToolbar search_toolbar;
    private EditText search_box;
    private ImageView search_btn;
    private BottomNavigationView supporter_main_page_bottom_menu;
    private RecyclerView search_recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_search);

        initViews();

        initBottomNavigationView();

        setSupportActionBar(search_toolbar);



    }

    private void initBottomNavigationView() {
        //selects home as the default view - make this browse for supporter
        supporter_main_page_bottom_menu.setSelectedItemId(R.id.search);
        //use this since the onNavigationItemSelected is deprecated
        supporter_main_page_bottom_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    break;
                case R.id.explore:
                    Intent to_explore = new Intent(Supporter_Search_Activity.this, Supporter_Main_Page_Activity.class);
                    to_explore.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //clears back-stack - might use elsewhere too
                    startActivity(to_explore);
                    break;
                case R.id.discounts_and_promos:
                    break;
                case R.id.shopping:
                    break;
                default:
                    break;
            }

            return true;
        });

    }

    private void initViews(){
        search_toolbar = findViewById(R.id.search_toolbar);
        search_box = findViewById(R.id.search_box);
        search_btn = findViewById(R.id.search_btn);
        supporter_main_page_bottom_menu = findViewById(R.id.supporter_main_page_bottom_menu);
        search_recycler_view = findViewById(R.id.search_recycler_view);

    }
}