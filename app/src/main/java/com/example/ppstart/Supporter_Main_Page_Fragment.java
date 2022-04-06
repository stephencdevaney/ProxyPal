package com.example.ppstart;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Supporter_Main_Page_Fragment extends Fragment {
    private BottomNavigationView supporter_bottom_nav_menu;

    private RecyclerView browse_profiles_rec_view;
    private BrowseProfilesAdapter profilesAdapter;
    private DatabaseHelper databaseHelper;

    private int supporter_id;
    private String supporter_username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supporter_main_page_fragment, container, false);


        Bundle main_fragment_bundle = this.getArguments();
        if(main_fragment_bundle != null){
            supporter_id = main_fragment_bundle.getInt("supporter_id");
            supporter_username = main_fragment_bundle.getString("supporter_username");
        }

        initViews(view);
        initBottomNavigationView();
        initRecView();
        GetProfiles();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //initialize UI elements
    private void initViews(View view){
        supporter_bottom_nav_menu = view.findViewById(R.id.supporter_bottom_nav_menu);
        browse_profiles_rec_view = view.findViewById(R.id.browse_profiles_rec_view);
    }

    private void initBottomNavigationView() {

        //sets the explore page as the default page
        supporter_bottom_nav_menu.setSelectedItemId(R.id.explore);

        //Set the listener for the bottom navigation menu buttons
        //(note: use this since the onNavigationItemSelected is deprecated)
        supporter_bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //switch to the Supporter_Search_Activity activity when the "search" button at the bottom is tapped
                case R.id.search:
                        Intent to_search = new Intent(getActivity(), Supporter_Search_Activity.class);
                        //pass the supporter account id and supporter account username to the Favorites_Activity activity
                        Bundle supporter_bundle_search = createNavMenuBundle();
                        to_search.putExtra("supporter_bundle", supporter_bundle_search);
                        //switch to the Favorites_Activity activity
                        startActivity(to_search);
                    break;
                //switch to the Supporter_Main_Page_Activity activity when the "explore" button at the bottom is tapped
                case R.id.explore:
                    break;
                //switch to the Discounts_Promos_Activity activity when the "Discounts & Promos" button at the bottom is tapped
                case R.id.discounts_and_promos:
                    if(supporter_id != -1){
                        Intent to_dp = new Intent(getActivity(), Discounts_Promos_Activity.class);
                        //pass the supporter account id and supporter account username to the Favorites_Activity activity
                        Bundle supporter_bundle_dp = createNavMenuBundle();
                        to_dp.putExtra("supporter_bundle", supporter_bundle_dp);
                        //switch to the Favorites_Activity activity
                        startActivity(to_dp);
                    }else{
                        Toast.makeText(getActivity(), "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                //switch to the Shopping_List_Activity activity when the "Shopping List" button at the bottom is tapped
                case R.id.shopping:
                    if(supporter_id != -1){
                        Intent to_shopping_list = new Intent(getActivity(), Shopping_List_Activity.class);
                        //pass the supporter account id and supporter account username to the Favorites_Activity activity
                        Bundle supporter_bundle_shopping = createNavMenuBundle();
                        to_shopping_list.putExtra("supporter_bundle", supporter_bundle_shopping);
                        //switch to the Favorites_Activity activity
                        startActivity(to_shopping_list);
                    }else{
                        Toast.makeText(getActivity(), "Sign in or create an account to access this!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    break;
            }

            return true;
        });



    }


    private Bundle createNavMenuBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("supporter_id", supporter_id);
        bundle.putString("supporter_username", supporter_username);
        return bundle;
    }

    //initialize the recycler view elements
    private void initRecView(){

        profilesAdapter = new BrowseProfilesAdapter(getActivity());
        browse_profiles_rec_view.setAdapter(profilesAdapter);
        browse_profiles_rec_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        profilesAdapter.passUserInfo(supporter_id, supporter_username);

    }

    //get the business profiles from the database and add them to the recycler view for this fragment
    //(note: this function might be too slow - might need to use threads for when the database is large)
    private void GetProfiles(){

        databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT * FROM profile", null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    ArrayList<Profile> browsable_profiles = new ArrayList<>();

                    int profile_id_index = cursor.getColumnIndex("profile_id");
                    int owner_id_index = cursor.getColumnIndex("owner_id");
                    int business_name_index = cursor.getColumnIndex("business_name");
                    int profile_avatar_image_index = cursor.getColumnIndex("profile_avatar_image");

                    for(int i = 0; i < cursor.getCount(); i++){
                        Profile p  = new Profile();
                        p.setProfile_id(cursor.getInt(profile_id_index));
                        p.setOwner_id(cursor.getInt(owner_id_index));
                        p.setBusiness_name(cursor.getString(business_name_index));
                        p.setProfile_avatar_image(cursor.getString(profile_avatar_image_index));

                        browsable_profiles.add(p);
                        cursor.moveToNext();
                    }
                    cursor.close();
                    db.close();



                    for (int i = 0; i < browsable_profiles.size();i++)
                    {
                        System.out.println(browsable_profiles.get(i));
                    }

                    profilesAdapter.setBrowsable_profiles(browsable_profiles);



                }else{
                    cursor.close();
                    db.close();
                }
            }else{
                db.close();
            }




        }catch(SQLiteException e){
            e.printStackTrace();

        }


    }


}
