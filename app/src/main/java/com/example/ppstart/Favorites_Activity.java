//CREATED BY BLAKE

package com.example.ppstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Favorites_Activity extends AppCompatActivity {


    //initialize loading screen
    ProgressDialog progress_dialog;

    //Initialize the elements used in the recycler view -Blake
    private RecyclerView business_favorites_rec_view, item_favorites_rec_view;
    private BrowseProfilesAdapter profilesAdapter;

    //These text views are displayed when the user has no favorited businesses/items -Blake
    private TextView no_business_favorites_txt, no_item_favorites_txt;

    private TextView item_fav_text;

    //include the items recycler view adapter when the inventory system is created -Blake

    //For database instance -Blake
    private DatabaseHelper databaseHelper;

    //To store the supporter username and id -Blake
    private String supporter_username;
    private int supporter_id;

    //These array list are used to store the results of queries -Blake
    private ArrayList<Profile> all_browsable_profiles, favorited_browsable_profiles;
    private ArrayList<ProfileFavorites> profile_favorites;

    //This will be used when the inventory system is complete and items can be favorited -Blake
    private ArrayList<ItemFavorites> item_favorites;

    //This is for getting an instance of the firestore database -Blake
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        //initialize loading screen
        progress_dialog = new ProgressDialog(this);


        //initialize texts that will be displayed if the user hasn't favorited any businesses or items -Blake
        no_business_favorites_txt = findViewById(R.id.no_business_favorites_txt);
        no_item_favorites_txt = findViewById(R.id.no_item_favorites_txt);

        item_fav_text = findViewById(R.id.item_favorites_txt);
        item_fav_text.setVisibility(View.GONE);
        no_item_favorites_txt.setVisibility(View.GONE);

        //initialize firestore database
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Receive the supporter username/id passed to this activity via an intent -Blake
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("supporter_bundle");
            if (bundle != null) {
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
            }
        }

        profile_favorites = new ArrayList<>();
        //initialize recycler view elements -Blake
        business_favorites_rec_view = findViewById(R.id.business_favorites_rec_view);
        profilesAdapter = new BrowseProfilesAdapter(this);
        business_favorites_rec_view.setAdapter(profilesAdapter);
        business_favorites_rec_view.setLayoutManager(new GridLayoutManager(this, 2));
        //pass the supporter id/username to the adapter -Blake
        profilesAdapter.passUserInfo(supporter_id, supporter_username);


        //for testing -Blake
        System.out.println("INSIDE FAVORITES: " + supporter_username + " " + supporter_id);

        //These two methods contain the functionality for retrieving favorites from the database
        //and setting the resulting array list to the recycler views -Blake
        GetProfileFavorites(supporter_username, supporter_id);
       // GetItemFavorites(supporter_username, supporter_id);


    }



    private void GetProfileFavorites(String supporter_username, int supporter_id) {

        //Get an instance of the SQLite database -Blake
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        //set loading screen
        progress_dialog.setMessage("Please Wait");
        progress_dialog.show();

       //This queries the Firestore cloud database -Blake
        firebaseFirestore.collection("Favorited_Profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        //The ProfileFavorites object stores the "link" between the supporter account and the business it favorited;
                        //this link is established by storing the supporter id and profile id together in one document of the Favorited_Profiles collection -Blake
                        ProfileFavorites pf = new ProfileFavorites();
                        pf.setProf_fav_supporter_id(Integer.parseInt(doc.get("supporter_id").toString()));
                        pf.setProf_fav_owner_id(Integer.parseInt(doc.get("owner_id").toString()));
                        pf.setProf_fav_profile_id(Integer.parseInt(doc.get("profile_id").toString()));

                        //for testing -Blake
                        System.out.println(Integer.parseInt(doc.get("supporter_id").toString()));

                        //add the ProfileFavorites objects (acting as the link between supporter accounts and the business they favorited, and not the actual business  profile
                        //that was favorited) to the profile_favorites array list -Blake
                        profile_favorites.add(pf);

                    }
                    //for testing -Blake
                    System.out.println("FAVORITES ARRAY LIST");
                    for (int i = 0; i < profile_favorites.size(); i++) {
                        System.out.println(profile_favorites.get(i));
                    }

                    //The commented-out code was how the query was done using the SQLite database before this was switched to the Firestore cloud database -Blake
                    try {
                        //Cursor profile_favorites_cursor = db.rawQuery("SELECT * FROM profile_favorites WHERE supporter_id =?", new String[]{String.valueOf(supporter_id)});
                        //query the profiles stored in the SQLite database (the profiles are not yet moved to the firestore database) -Blake
                        Cursor profile_cursor = db.rawQuery("SELECT * FROM profile", null);

            /*
            if(profile_favorites_cursor != null){
                if(profile_favorites_cursor.moveToFirst()){
                   profile_favorites = new ArrayList<>();

                    int supporter_id_index_fav = profile_favorites_cursor.getColumnIndex("supporter_id");
                    int owner_id_index_fav = profile_favorites_cursor.getColumnIndex("owner_id");
                    int profile_id_index_fav = profile_favorites_cursor.getColumnIndex("profile_id");

                    for(int i = 0; i < profile_favorites_cursor.getCount(); i++){
                        ProfileFavorites pf  = new ProfileFavorites();
                        pf.setProf_fav_supporter_id(profile_favorites_cursor.getInt(supporter_id_index_fav));
                        pf.setProf_fav_owner_id(profile_favorites_cursor.getInt(owner_id_index_fav));
                        pf.setProf_fav_profile_id(profile_favorites_cursor.getInt(profile_id_index_fav));

                        profile_favorites.add(pf);
                        profile_favorites_cursor.moveToNext();
                    }
                    profile_favorites_cursor.close();




                    for (int i = 0; i < profile_favorites.size();i++)
                    {
                        System.out.println("FAVORITES PROFILE ARRAYLIST");
                        System.out.println(profile_favorites.get(i));
                    }


                }else{
                    no_business_favorites_txt.setVisibility(View.VISIBLE);
                    profile_favorites_cursor.close();
                }
            }else{
                no_business_favorites_txt.setVisibility(View.VISIBLE);
            }

             */

                        if (profile_cursor != null) {
                            if (profile_cursor.moveToFirst()) {
                                //get the list of all browsable profiles and store it in the all_browsable_profiles array list
                                //again, this functions identically to all of the other queries for all profiles -Blake
                                all_browsable_profiles = new ArrayList<>();

                                int profile_id_index = profile_cursor.getColumnIndex("profile_id");
                                int owner_id_index = profile_cursor.getColumnIndex("owner_id");
                                int business_name_index = profile_cursor.getColumnIndex("business_name");
                                int profile_avatar_image_index = profile_cursor.getColumnIndex("profile_avatar_image");

                                for (int i = 0; i < profile_cursor.getCount(); i++) {
                                    Profile p = new Profile();
                                    p.setProfile_id(profile_cursor.getInt(profile_id_index));
                                    p.setOwner_id(profile_cursor.getInt(owner_id_index));
                                    p.setBusiness_name(profile_cursor.getString(business_name_index));
                                    p.setProfile_avatar_image(profile_cursor.getString(profile_avatar_image_index));

                                    all_browsable_profiles.add(p);
                                    profile_cursor.moveToNext();
                                }
                                profile_cursor.close();
                                db.close();
                                //for testing -Blake
                                for (int i = 0; i < all_browsable_profiles.size(); i++) {
                                    System.out.println("ALL BROWSABLE PROFILES");
                                    System.out.println(all_browsable_profiles.get(i));
                                }
                            } else {
                                profile_cursor.close();
                            }


                        }


                        //(can probably do this in a much more effective manner)
                        //This block below compares the owner id's of the ProfileFavorites objects in the profile_favorites array list to the
                        //owner id's of all of the browsable profiles to see if there's a match; likewise, it checks if the supporter id of the currently
                        //logged in supporter matches one present in a ProfileFavorites object; if both is these comparisons result in matches, it means the profile in the
                        //all_browsable_favorites profile has been favorited by the logged-in user, and thus this profile is added to the favorited_browsable_profiles array list
                        //that will be used by the adapter for the recycler view of favorited businesses -Blake
                        favorited_browsable_profiles = new ArrayList<>();
                        for (Profile profile : all_browsable_profiles) {
                            if (profile_favorites != null) {
                                for (ProfileFavorites profileFavorites : profile_favorites) {
                                    //add profile.getProfile_id() == profileFavorites.getProf_fav_profile_id() when ready
                                    if (profile.getOwner_id() == profileFavorites.getProf_fav_owner_id() && supporter_id == profileFavorites.getProf_fav_supporter_id()) {
                                        favorited_browsable_profiles.add(profile);
                                    }
                                }
                            } else {

                            }
                        }

                        //for testing -Blake
                        System.out.println("FAVORITES TO ADD TO ADAPTER");
                        for (int i = 0; i < favorited_browsable_profiles.size(); i++) {
                            System.out.println(favorited_browsable_profiles.get(i));
                        }


                        if(favorited_browsable_profiles.isEmpty()) {
                            //display a message if no businesses are favorited
                            no_business_favorites_txt.setVisibility(View.VISIBLE);
                        }else{
                            //set the adapter for the recycler view of favorited profiles, as described above -Blake
                            profilesAdapter.setBrowsable_profiles(favorited_browsable_profiles);
                        }




                    } catch (SQLiteException e) {
                        e.printStackTrace();

                    }


                    //no_item_favorites_txt.setVisibility(View.VISIBLE); //for now, just display that there are no items favorited -Blake

                    //dismiss loading screen
                    progress_dialog.dismiss();
                    return;


                }
            }


        });


    }

    //This will function the same as the GetProfileFavorites method except for items instead of profiles
    //when the inventory system is ready -Blake
    private void GetItemFavorites(String supporter_username, int supporter_id) {


        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        firebaseFirestore.collection("Favorited_Items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ItemFavorites item_favorites = new ItemFavorites();
                /*
                item_favorites.setItem_id(Integer.parseInt());
                item_favorites.setOwner_id();
                item_favorites.setProfile_id();
                item_favorites.setSupporter_id();

                //System.out.println(Integer.parseInt(doc.get("supporter_id").toString()));

                item.add(item_favorites);

                 */

            }
        });
    }

}