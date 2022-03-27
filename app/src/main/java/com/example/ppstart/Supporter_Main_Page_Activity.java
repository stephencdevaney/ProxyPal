package com.example.ppstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Supporter_Main_Page_Activity extends AppCompatActivity {

    //testing
    private TextView browse_test_txt;

    //UI elements
    private DrawerLayout supporter_main_page_drawer;
    private NavigationView supporter_main_page_navigationView;
    private MaterialToolbar supporter_main_page_toolbar;

    private String supporter_username;
    private int supporter_id;

    private TextView toolbar_username;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_main_page);

        //initialize UI elements
        supporter_main_page_drawer = findViewById(R.id.supporter_main_page_drawer);
        supporter_main_page_navigationView = findViewById(R.id.supporter_main_page_navigationView);
        supporter_main_page_toolbar = findViewById(R.id.supporter_main_page_toolbar);

        toolbar_username = findViewById(R.id.toolbar_username);

        //create the toggleable drawer inside the toolbar at the top of this page
        setSupportActionBar(supporter_main_page_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, supporter_main_page_drawer, supporter_main_page_toolbar, (R.string.supporter_drawer_open), (R.string.supporter_drawer_close));
        supporter_main_page_drawer.addDrawerListener(toggle);
        toggle.syncState();


        //receive the username from the bundle passed to this activity from the login or create account activities
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("supporter_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
            }
        }

        toolbar_username.setText(supporter_username);


        //set the listener for when options in the drawer menu are tapped
        supporter_main_page_navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                   case R.id.drawer_account:
                        break;
                   case  R.id.drawer_favorites:
                       Intent to_favorites = new Intent(Supporter_Main_Page_Activity.this, Favorites_Activity.class);

                       //pass the supporter account id and supporter account username to the Favorites_Activity activity
                       Bundle supporter_bundle = new Bundle();
                       supporter_bundle.putInt("supporter_id", supporter_id);
                       supporter_bundle.putString("supporter_username", supporter_username);
                       to_favorites.putExtra("supporter_bundle", supporter_bundle);
                       //switch to the Favorites_Activity activity
                       startActivity(to_favorites);
                        break;
                    case R.id.drawer_direct_messages:
                        break;
                    case R.id.drawer_logout:
                        Intent logout = new Intent(Supporter_Main_Page_Activity.this, MainActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logout);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });



        //Create the transaction for the fragment for this page
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.supporter_main_page_fragment_container, new Supporter_Main_Page_Fragment());
        transaction.addToBackStack("tag").commit();



    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
