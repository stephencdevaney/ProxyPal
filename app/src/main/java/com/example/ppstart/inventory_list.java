package com.example.ppstart;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class inventory_list extends Fragment {

    private RecyclerView inventory;
    private inventoryAdapter adapter;
    private TextView business_name;

    private DatabaseHelper databaseHelper;

    private int owner_id;
    private String businessName;
    private boolean edit_flag;

    private ArrayList<item> itemList;

    public inventory_list() {
        // Required empty public constructor
    }

    public static inventory_list newInstance() {
        inventory_list fragment = new inventory_list();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);


        Bundle bundle = this.getArguments();
        if(bundle != null){
            owner_id = bundle.getInt("owner_id");
            businessName = bundle.getString("name");
            edit_flag = bundle.getBoolean("edit_flag");
        }

        business_name = view.findViewById(R.id.inventory_business_name);
        business_name.setText(businessName);

        itemList = new ArrayList<>();
        inventory = view.findViewById(R.id.inventory_rec_view);
        setItemAdapter();
        GetItems();

        return view;
    }


    //initialize elements for the RecyclerView that will display the list of businesses -Blake
    /*private void initRecView(){
        adapter = new inventoryAdapter(getActivity());
        inventory.setAdapter(adapter);
        //The RecyclerView is set using a grid layout manager with 2 columns
        inventory.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.passUserInfo(owner_id, businessName, edit_flag);
    }*/
    private void setItemAdapter() {
        inventoryAdapter adapter = new inventoryAdapter(itemList);
        RecyclerView.LayoutManager itemLayout = new LinearLayoutManager(getContext());
        inventory.setLayoutManager(itemLayout);
        inventory.setItemAnimator(new DefaultItemAnimator());
        inventory.setAdapter(adapter);
        adapter.passUserInfo(owner_id, businessName);
    }

    private void GetItems(){
        //Get a writeable instance of the database
        databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try{
            //Query all business profiles from the database
            //Cursor inventory_cursor = db.rawQuery("SELECT * FROM store_inventory WHERE owner_id=" + owner_id, null);
            Cursor inventory_cursor = db.rawQuery("SELECT * FROM store_inventory WHERE owner_id = ?", new String[]{String.valueOf(owner_id)});
            if(inventory_cursor != null){
                if(inventory_cursor.moveToFirst()){
                    //The RecycleView for this fragment displays businesses using the business name and an avatar image
                    int price_index = inventory_cursor.getColumnIndex("price");
                    int item_num_index = inventory_cursor.getColumnIndex("item_number");
                    for(int i = 0; i < inventory_cursor.getCount(); i++){
                        //For each item queried from the database, create a Profile object and set
                        //its fields to the values taken from the associated fields of the database
                        //String item_num = inventory_cursor.getString(item_num_index);
                        item Item  = new item();
                        Item.setOwnerID(owner_id);
                        Item.setItemNumber(inventory_cursor.getInt(item_num_index));
                        Item.setPrice(inventory_cursor.getString(price_index));

                        //Cursor item_cursor = db.rawQuery("SELECT * FROM item WHERE item_number=" + item_num, null);
                        Cursor item_cursor = db.rawQuery("SELECT * FROM item WHERE item_number = ?", new String[]{String.valueOf(Item.getItemNumber())});
                        if(item_cursor != null) {
                            if(item_cursor.moveToFirst()){
                                int item_name_index = item_cursor.getColumnIndex("item_name");
                                //Item.setName(inventory_cursor.getString(item_name_index));
                                Item.setName(item_cursor.getString(item_name_index));
                            }
                        }
                        //add the Profile object to the array list
                        itemList.add(Item);
                        item_cursor.close();
                        inventory_cursor.moveToNext();
                    }
                    inventory_cursor.close();
                    db.close();
                    //for testing that the array list was populated correctly

                    //set the adapter associated with the recycler view using the array list of Profile objects
                    //adapter.setInventory(itemList);
                }else{
                    //Don't forget to close the cursors and database instance!
                    inventory_cursor.close();
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