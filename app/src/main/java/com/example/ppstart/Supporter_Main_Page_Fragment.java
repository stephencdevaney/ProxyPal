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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Supporter_Main_Page_Fragment extends Fragment {
    private BottomNavigationView supporter_main_page_bottom_menu;

    private RecyclerView browse_profiles_rec_view;
    private BrowseProfilesAdapter profilesAdapter;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supporter_main_page_fragment, container, false);

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

    private void initViews(View view){
        supporter_main_page_bottom_menu = view.findViewById(R.id.supporter_main_page_bottom_menu);
        browse_profiles_rec_view = view.findViewById(R.id.browse_profiles_rec_view);
    }

    private void initBottomNavigationView() {
        //selects home as the default view - make this browse for supporter
        supporter_main_page_bottom_menu.setSelectedItemId(R.id.explore);
        //use this since the onNavigationItemSelected is deprecated
        supporter_main_page_bottom_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                    Intent to_search = new Intent(getActivity(), Supporter_Search_Activity.class);
                    to_search.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //clears back-stack - might use elsewhere too
                    startActivity(to_search);
                    break;
                case R.id.explore:
                    Toast.makeText(getActivity(), "browse", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.discounts_and_promos:
                    Toast.makeText(getActivity(), "discounts_and_promos", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.shopping:
                    Toast.makeText(getActivity(), "shopping", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

            return true;
        });

    }

    private void initRecView(){

        profilesAdapter = new BrowseProfilesAdapter(getActivity());
        browse_profiles_rec_view.setAdapter(profilesAdapter);
        browse_profiles_rec_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));

    }

    //this function might be too slow - might need to use threads
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
