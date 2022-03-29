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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    private Button toolbar_guest_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_main_page);

        //initialize UI elements
        supporter_main_page_drawer = findViewById(R.id.supporter_main_page_drawer);
        supporter_main_page_navigationView = findViewById(R.id.supporter_main_page_navigationView);
        supporter_main_page_toolbar = findViewById(R.id.supporter_main_page_toolbar);

        toolbar_guest_btn = findViewById(R.id.toolbar_guest_btn);

        toolbar_username = findViewById(R.id.toolbar_username);

        //create the toggleable drawer inside the toolbar at the top of this page
        setSupportActionBar(supporter_main_page_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, supporter_main_page_drawer, supporter_main_page_toolbar, (R.string.supporter_drawer_open), (R.string.supporter_drawer_close));
        supporter_main_page_drawer.addDrawerListener(toggle);
        toggle.syncState();

        //this allows the TextView/ImageView in the top of the drawer header to be manipulated
        View header_view = supporter_main_page_navigationView.getHeaderView(0);
        ImageView proxypal_logo = (ImageView) header_view.findViewById(R.id.drawer_header_image);
        TextView supporter_account_username = (TextView) header_view.findViewById(R.id.drawer_header_username);




        //receive the id/username from the bundle passed to this activity from the login or create account activities
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("supporter_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
            }
        }

        if(supporter_id != -1){
            toolbar_guest_btn.setVisibility(View.GONE);
            toolbar_username.setVisibility(View.VISIBLE);
            toolbar_username.setText("Hello, " + supporter_username + "!");
        }else{
            toolbar_guest_btn.setVisibility(View.VISIBLE);
            toolbar_username.setVisibility(View.GONE);
        }


        //set the TextView inside the drawer header for this activity to the supporter account's username
        if(supporter_id != -1){
            supporter_account_username.setText(supporter_username);
        }else{
            supporter_account_username.setText("Guest");
        }

        //set the icon in the drawer header to the ProxyPal logo
        Glide.with(this).asBitmap()
                .load("https://icons.iconarchive.com/icons/hydrattz/multipurpose-alphabet/256/Letter-P-violet-icon.png")
                .into(proxypal_logo);


        //toolbar button
        toolbar_guest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(Supporter_Main_Page_Activity.this, MainActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logout);
            }
        });


        //set the listener for when options in the drawer menu are tapped
        supporter_main_page_navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                   case R.id.drawer_account:
                       if(supporter_id != -1){
                           Intent to_options = new Intent(Supporter_Main_Page_Activity.this, Supporter_Acc_Options_Activity.class);
                           //pass the supporter account id and supporter account username to the Favorites_Activity activity
                           Bundle supporter_bundle = new Bundle();
                           supporter_bundle.putInt("supporter_id", supporter_id);
                           supporter_bundle.putString("supporter_username", supporter_username);
                           to_options.putExtra("supporter_bundle", supporter_bundle);
                           //switch to the Favorites_Activity activity
                           startActivity(to_options);
                       }else{
                           Toast.makeText(Supporter_Main_Page_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                       }
                        break;
                   case  R.id.drawer_favorites:
                       if(supporter_id != -1){
                           Intent to_favorites = new Intent(Supporter_Main_Page_Activity.this, Favorites_Activity.class);
                           //pass the supporter account id and supporter account username to the Favorites_Activity activity
                           Bundle supporter_bundle = new Bundle();
                           supporter_bundle.putInt("supporter_id", supporter_id);
                           supporter_bundle.putString("supporter_username", supporter_username);
                           to_favorites.putExtra("supporter_bundle", supporter_bundle);
                           //switch to the Favorites_Activity activity
                           startActivity(to_favorites);
                       }else{
                           Toast.makeText(Supporter_Main_Page_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                       }
                        break;
                    case R.id.drawer_direct_messages:
                        if(supporter_id != -1){
                            Toast.makeText(Supporter_Main_Page_Activity.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Supporter_Main_Page_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                        }
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


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle to_fragment_bundle = new Bundle();
        to_fragment_bundle.putInt("supporter_id", supporter_id);
        to_fragment_bundle.putString("supporter_username", supporter_username);
        Supporter_Main_Page_Fragment fragInfo = new Supporter_Main_Page_Fragment();
        fragInfo.setArguments(to_fragment_bundle);
        transaction.replace(R.id.supporter_main_page_fragment_container, fragInfo);
        transaction.commit();


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
