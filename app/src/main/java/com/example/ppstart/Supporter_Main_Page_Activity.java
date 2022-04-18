//CREATED BY BLAKE

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Supporter_Main_Page_Activity extends AppCompatActivity {

    //for testing
    private TextView browse_test_txt;

    //Declare UI elements
    private DrawerLayout supporter_main_page_drawer;
    private NavigationView supporter_main_page_navigationView;
    private MaterialToolbar supporter_main_page_toolbar;

    //These variables are used to store the info passed to this activity from an intent,
    //and likewise used to pass this information using an intent
    private String supporter_username;
    private int supporter_id;

    //private Spinner spinner;

    //This is the username displayed in the top right if a supporter account logs in
    private TextView toolbar_username;

    //If a user is browsing as guest, no username is displayed; instead, this button is
    private Button toolbar_guest_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_main_page);

        //initialize UI elements
        supporter_main_page_drawer = findViewById(R.id.supporter_main_page_drawer);
        supporter_main_page_navigationView = findViewById(R.id.supporter_main_page_navigationView);
        supporter_main_page_toolbar = findViewById(R.id.supporter_main_page_toolbar);

        //Spinner firstSpinner = (Spinner) findViewById(R.id.location_spinner);


        //Initialize UI elements displayed in the toolbar
        toolbar_guest_btn = findViewById(R.id.toolbar_guest_btn);

        toolbar_username = findViewById(R.id.toolbar_username);

        //create the toggleable drawer inside the toolbar at the top of this page
        setSupportActionBar(supporter_main_page_toolbar); //this sets the toolbar at the top, where the drawer toggle is contained
        //this creates the toggleable drawer using the supporter_main_page_drawer DrawerLayout in the xml file of this activity
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, supporter_main_page_drawer, supporter_main_page_toolbar, (R.string.supporter_drawer_open), (R.string.supporter_drawer_close));
        supporter_main_page_drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Drawer notes:
        //what is displayed when the drawer is tapped on is dictated by the navigation view for the DrawerLayout defined in this activity's xml file;
        //the header of the drawer uses the supporter_main_page_drawer_header.xml file to define its UI layout
        //aside from the header, the drawer uses a menu, who's UI layout is defined by the supporter_main_page_drawer_menu.xml menu file
        //The behavior of these menu elements is defined further on in this activity, in the  supporter_main_page_navigationView listener

        //this allows the TextView/ImageView in the top of the drawer header to be manipulated
        View header_view = supporter_main_page_navigationView.getHeaderView(0);
        //For now, the image the supporter sees at the top of the drawer is a stock image that serves as a filler for the ProxyPal logo. This
        //could be changed to be a profile picture set by the user, although this may not have much use aside from being viewed by owner accounts
        //who are chatting with the supporter, and thus having profile pics for supporters may not really be worth it
        ImageView proxypal_logo = (ImageView) header_view.findViewById(R.id.drawer_header_image);
        TextView supporter_account_username = (TextView) header_view.findViewById(R.id.drawer_header_username);


        //receive the id/username from the bundle passed to this activity from the login or create account activities
        //and store them in the supporter_username and supporter_id variables
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("supporter_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
            }
        }

        //if supporter id is -1, this indicates that a user is browsing as a guest; if they are,
        //a button is made at the top right inside of the toolbar that redirects the user to create an account or login when it is pressed.
        //Otherwise, if the user is logged in to a supporter account, their username is displayed in the top right instead of the guest button
        if(supporter_id != -1){
            toolbar_guest_btn.setVisibility(View.GONE);
            toolbar_username.setVisibility(View.VISIBLE);
            toolbar_username.setText("Hello, " + supporter_username + "!");
        }else{
            toolbar_guest_btn.setVisibility(View.VISIBLE);
            toolbar_username.setVisibility(View.GONE);
        }


        //set the TextView inside the drawer header for this activity to the supporter account's username if the user is logged in to
        //a supporter account; otherwise, the text in the drawer header simply displays "Guest"
        if(supporter_id != -1){
            supporter_account_username.setText(supporter_username);
        }else{
            supporter_account_username.setText("Guest");
        }

        //set the icon in the drawer header to the ProxyPal logo (which is a filler stock icon for now, we will later photoshop a real icon)
        //The images is set with the help of the Glide library
        Glide.with(this).asBitmap()
                .load("https://icons.iconarchive.com/icons/hydrattz/multipurpose-alphabet/256/Letter-P-violet-icon.png")
                .into(proxypal_logo);



        //this sets the on-click listener for the button displayed in the top right of the toolbar
        //when a user is browsing as a guest
        toolbar_guest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //the function of this button is simply to redirect the user to the starting screen of the app, which
                //contains the options to to create an account or login (or continue to browse as a guest if they please)
                Intent back_to_main = new Intent(Supporter_Main_Page_Activity.this, MainActivity.class);
                back_to_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //this ensures that the backstack is cleared (might fix this)
                startActivity(back_to_main);
            }
        });

        //this sets the listener for when options in the drawer menu are tapped
        supporter_main_page_navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //items in the menu are switched by their view id's (as defined in the supporter_main_page_drawer_menu.xml menu file)
                switch(item.getItemId()){

                    //this is the "account options" option of the drawer menu; currently, this does nothing as we couldn't really think of something good to put
                    //here yet. The best use I can think of for this is to allow a supporter account to change their username and password; this will be best implemented
                    //when the login authentication system is re-done using the Firebase authentication
                   case R.id.drawer_account:
                       if(supporter_id != -1){
                           Intent to_options = new Intent(Supporter_Main_Page_Activity.this, Supporter_Acc_Options_Activity.class);
                           //pass the supporter account id and supporter account username
                           Bundle supporter_bundle = new Bundle();
                           supporter_bundle.putInt("supporter_id", supporter_id);
                           supporter_bundle.putString("supporter_username", supporter_username);
                           to_options.putExtra("supporter_bundle", supporter_bundle);
                           //switch to the Supporter_Acc_Options_Activity activity
                           startActivity(to_options);
                       }else{
                           //if the user is browsing as a Guest, this feature is not available to them; this toast message pops up letting the user know this
                           Toast.makeText(Supporter_Main_Page_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                       }
                        break;
                   //this option in the drawer account menu is for the business favorited by the user (favorting items will be added once the busines profile
                    //inventory system is more complete)
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
                           //if the user is browsing as a Guest, this feature is not available to them; this toast message pops up letting the user know this
                           Toast.makeText(Supporter_Main_Page_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                       }
                        break;
                    //this option in the drawer account menu is for viewing the chats with a business that this supporter has engaged in
                    case R.id.drawer_direct_messages:
                        if(supporter_id != -1){
                            Intent to_dm = new Intent(Supporter_Main_Page_Activity.this, All_Chats_Activity.class);
                            Bundle dm_bundle = new Bundle();
                            dm_bundle.putInt("supporter_id", supporter_id);
                            dm_bundle.putString("supporter_username", supporter_username);
                            to_dm.putExtra("dm_bundle", dm_bundle);
                            startActivity(to_dm);
                        }else{
                            //if the user is browsing as a Guest, this feature is not available to them; this toast message pops up letting the user know this
                            Toast.makeText(Supporter_Main_Page_Activity.this, "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //this option in the drawer account menu allows the user to log out; it functions the same for supporters and guests
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

        //This block below allows the fragment of this activity (contained in the supporter_main_page_fragment_container FrameLayout
        //defined in the xml file of this activity

        //First a FragmentTransaction is defined
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //Next, I define a bundle used to pass the id and username of the logged in supporter to the fragment
        Bundle to_fragment_bundle = new Bundle();
        to_fragment_bundle.putInt("supporter_id", supporter_id);
        to_fragment_bundle.putString("supporter_username", supporter_username);
        //Then, the fragment to be used is declared
        Supporter_Main_Page_Fragment fragInfo = new Supporter_Main_Page_Fragment();
        //The bundle defined above is used to set the arguments of the fragment
        fragInfo.setArguments(to_fragment_bundle);
        //The FrameLayout is replaced by the fragment
        transaction.replace(R.id.supporter_main_page_fragment_container, fragInfo);
        //The transaction is committed, meaning the fragment will show
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        //for now, the back button functionality is disabled to prevent the user from going back to the logout screen
        //there is probably a better way to do this
        //super.onBackPressed();
    }
}
