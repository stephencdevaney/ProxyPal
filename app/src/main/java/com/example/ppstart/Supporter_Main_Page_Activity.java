package com.example.ppstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Supporter_Main_Page_Activity extends AppCompatActivity {

    //testing
    private TextView browse_test_txt;

    //UI elements
    //added 3/20
    private DrawerLayout supporter_main_page_drawer;
    private NavigationView supporter_main_page_navigationView;
    private MaterialToolbar supporter_main_page_toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        supporter_main_page_drawer = findViewById(R.id.supporter_main_page_drawer);
        supporter_main_page_navigationView = findViewById(R.id.supporter_main_page_navigationView);
        supporter_main_page_toolbar = findViewById(R.id.supporter_main_page_toolbar);


        //added 3/20
        setSupportActionBar(supporter_main_page_toolbar);
        //added 3/20
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, supporter_main_page_drawer, supporter_main_page_toolbar, (R.string.supporter_drawer_open), (R.string.supporter_drawer_close));
        //added 3/20
        supporter_main_page_drawer.addDrawerListener(toggle);
        //added 3/20
        toggle.syncState();
        //added 3/20

        supporter_main_page_navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    default:
                        break;
                }
                return false;
            }
        });


        //added 3/20
        //Creating a fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.supporter_main_page_fragment_container, new Supporter_Main_Page_Fragment());
        transaction.commit();



    }


}
