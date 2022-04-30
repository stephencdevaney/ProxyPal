//CREATED BY BLAKE - EDITED BY CHANDLER

package com.example.ppstart;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class Supporter_Main_Page_Fragment<location_Spinner> extends Fragment {
    //Declare the navigation menu displayed at the bottom of this fragment -Blake
    private BottomNavigationView supporter_bottom_nav_menu;

    //Declare the RecyclerView and its adapter -Blake
    private RecyclerView browse_profiles_rec_view;
    private BrowseProfilesAdapter profilesAdapter;

    //For database useage - Blake
    private DatabaseHelper databaseHelper;

    //Declare the variables used to store the id and username of the supporter account -Blake
    private int supporter_id;
    private String supporter_username;

    //Declare the spinner that will be used to select a location radius -Blake
    private Spinner location_Spinner;

    public int j = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //This inflates the supporter_main_page_fragment.xml layout file for this fragment -Blake
        View view = inflater.inflate(R.layout.supporter_main_page_fragment, container, false);


        //Chandler
        //Initialize the location spinner - the location_spinner is populated using the supporter_spinner2 string array
        //found in the strings.xml file -Blake
        location_Spinner = view.findViewById(R.id.location_spinner);
        location_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //hack to fix activity opening with 1st index sellected
                //activity automatically opens to first index of spinner
                //1st sellect is ignored and j toggle indicates on the next sellect to launch
                    //find_closest_store_activity
                if(j ==0){
                    j++;
                }
                else{
                    //debug to show current long/lat for map traversal later on
                    Toast.makeText(getContext(),adapterView.getItemAtPosition(i).toString() , Toast.LENGTH_SHORT).show();
                    //initiate intent to new activity, but not launch yet
                    Intent to_Closest = new Intent(getActivity(), Find_Closest_Store_Activity.class);

                    Bundle closestBundle = new Bundle();
                    //bundle includes what distance value is sellected based on spinner sellection
                    if(adapterView.getItemAtPosition(i).toString()=="5 miles"){
                        closestBundle.putInt("distReq", 5);
                    }
                    else if(adapterView.getItemAtPosition(i).toString()=="10 miles"){
                        closestBundle.putInt("distReq", 10);
                    }
                    else if(adapterView.getItemAtPosition(i).toString()=="15 miles"){
                        closestBundle.putInt("distReq", 15);
                    }
                    else if(adapterView.getItemAtPosition(i).toString()=="25 miles"){
                        closestBundle.putInt("distReq", 25);
                    }
                    else{
                        closestBundle.putInt("distReq", 50);
                    }

                    //add bundle to intent, and launch intent
                    to_Closest.putExtra("closestBundle", closestBundle);
                    startActivity(to_Closest);

                }
            }
            //<\Chandler
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Receive the supporter id and username passed from the Supporter_Main_Page_Activity
        //and store them -Blake
        Bundle main_fragment_bundle = this.getArguments();
        if(main_fragment_bundle != null){
            supporter_id = main_fragment_bundle.getInt("supporter_id");
            supporter_username = main_fragment_bundle.getString("supporter_username");
        }


        //These methods are just for helping keep the code cleaner -Blake
        initViews(view);
        initBottomNavigationView();
        initRecView();
        GetProfiles();

        return view;
    }



    //This method initialize UI elements -Blake
    private void initViews(View view){
        supporter_bottom_nav_menu = view.findViewById(R.id.supporter_bottom_nav_menu);
        browse_profiles_rec_view = view.findViewById(R.id.browse_profiles_rec_view);
    }

    //This method is for initializing/defining the behavior of the navigation menu displayed at the bottom -Blake
    private void initBottomNavigationView() {

        //sets the "explore" page as the default page -Blake
        supporter_bottom_nav_menu.setSelectedItemId(R.id.explore);

        //Set the listener for the bottom navigation menu buttons
        //(note: use this since the onNavigationItemSelected is deprecated) -Blake
        supporter_bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //switch to the Supporter_Search_Activity activity when the "search" button at the bottom is tapped -Blake
                case R.id.search:
                        Intent to_search = new Intent(getActivity(), Supporter_Search_Activity.class);
                        //pass the supporter account id and supporter account username to the Supporter_Search_Activity activity -Blake
                        Bundle supporter_bundle_search = createNavMenuBundle();
                        to_search.putExtra("supporter_bundle", supporter_bundle_search);
                        //switch to the Supporter_Search_Activity activity -Blake
                        startActivity(to_search);
                    break;
                //Does nothing since the supporter is already in the "explore" page (the Supporter_Main_Page is the explore page) -Blake
                case R.id.explore:
                    break;
                //switch to the Discounts_Promos_Activity activity when the "Discounts & Promos" button at the bottom is tapped -Blake
                case R.id.discounts_and_promos:
                    if(supporter_id != -1){
                        Intent to_dp = new Intent(getActivity(), Discounts_Promos_Activity.class);
                        //pass the supporter account id and supporter account username to the Discounts_Promos_Activity activity -Blake
                        Bundle supporter_bundle_dp = createNavMenuBundle();
                        to_dp.putExtra("promo_bundle", supporter_bundle_dp);
                        //switch to the Discounts_Promos_Activity activity -Blake
                        startActivity(to_dp);
                    }else{
                        //If the user is browsing as a guest, this feature is unavailable
                        Toast.makeText(getActivity(), "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                //switch to the Shopping_List_Activity activity when the "Shopping List" button at the bottom is tapped -Blake
                case R.id.shopping:
                    if(supporter_id != -1){
                        Intent to_shopping_list = new Intent(getActivity(), Shopping_List_Activity.class);
                        //pass the supporter account id and supporter account username to the Shopping_List_Activity activity -Blake
                        Bundle supporter_bundle_shopping = createNavMenuBundle();
                        to_shopping_list.putExtra("supporter_bundle", supporter_bundle_shopping);
                        //switch to the Shopping_List_Activity activity -Blake
                        startActivity(to_shopping_list);
                    }else{
                        //If the user is browsing as a guest, this feature is unavailable
                        Toast.makeText(getActivity(), "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    break;
            }

            return true;
        });



    }


    //Simple function that creates a bundle containing the supporter_id and supporter_username;
    //this helps keep the navigation menu code cleaner -Blake
    private Bundle createNavMenuBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("supporter_id", supporter_id);
        bundle.putString("supporter_username", supporter_username);
        return bundle;
    }

    //initialize elements for the RecyclerView that will display the list of businesses -Blake
    private void initRecView(){

        //The RecyclerView uses a BrowseProfilesAdapter
        profilesAdapter = new BrowseProfilesAdapter(getActivity());
        browse_profiles_rec_view.setAdapter(profilesAdapter);
        //The RecyclerView is set using a grid layout manager with 2 columns
        browse_profiles_rec_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //The supporter id and username are passed to the adapter
        profilesAdapter.passUserInfo(supporter_id, supporter_username);

    }

    //get the business profiles from the database and add them to the recycler view for this fragment
    //(note: this function might be too slow - might need to use threads for when the database is large)
    private void GetProfiles(){


        //Get a writeable instance of the database
        databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try{
            //Query all business profiles from the database
            Cursor cursor = db.rawQuery("SELECT * FROM profile", null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    //Create an array list of Profile objects to store the profiles queried from the database
                    ArrayList<Profile> browsable_profiles = new ArrayList<>();

                    //The RecycleView for this fragment displays businesses using the business name and an avatar image
                    int profile_id_index = cursor.getColumnIndex("profile_id");
                    int owner_id_index = cursor.getColumnIndex("owner_id");
                    int business_name_index = cursor.getColumnIndex("business_name");
                    int profile_avatar_image_index = cursor.getColumnIndex("profile_avatar_image");

                    for(int i = 0; i < cursor.getCount(); i++){
                        //For each item queried from the database, create a Profile object and set
                        //its fields to the values taken from the associated fields of the database
                        Profile p  = new Profile();
                        p.setProfile_id(cursor.getInt(profile_id_index));
                        p.setOwner_id(cursor.getInt(owner_id_index));
                        p.setBusiness_name(cursor.getString(business_name_index));
                        p.setProfile_avatar_image(cursor.getString(profile_avatar_image_index));

                        //add the Profile object to the array list
                        browsable_profiles.add(p);
                        cursor.moveToNext();
                    }
                    cursor.close();
                    db.close();
                    //for testing that the array list was populated correctly
                    for (int i = 0; i < browsable_profiles.size();i++)
                    {
                        System.out.println(browsable_profiles.get(i));
                    }
                    //set the adapter associated with the recycler view using the array list of Profile objects
                    profilesAdapter.setBrowsable_profiles(browsable_profiles);
                }else{
                    //Don't forget to close the cursors and database instance!
                    cursor.close();
                    db.close();
                }
            }else{
                db.close();
            }
        }catch(SQLiteException e){
            //Since the database is being queried, we have to catch SQLite exeptions
            e.printStackTrace();
        }
    }
}
