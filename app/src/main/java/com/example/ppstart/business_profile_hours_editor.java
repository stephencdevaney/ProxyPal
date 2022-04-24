package com.example.ppstart;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;


public class business_profile_hours_editor extends Fragment {


    private TextView name;
    private EditText monday;
    private EditText tuesday;
    private EditText wednesday;
    private EditText thursday;
    private EditText friday;
    private EditText saturday;
    private EditText sunday;
    private Button saveButton;

    // database tools
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private int owner_Id;
    private String business_name;
    private String information;
    private String[] hours;


    public business_profile_hours_editor() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_profile_hours_editor, container, false);
        initViews(view);

        business_name = "Business Name";
        Bundle main_fragment_bundle = this.getArguments();
        if(main_fragment_bundle != null){
            business_name = main_fragment_bundle.getString("name");
            information = main_fragment_bundle.getString("info");
            owner_Id = main_fragment_bundle.getInt("owner_id");
        }

        name.setText(business_name);
        getHours();
        saveButtonSetup();
        return view;
    }


    //initialize UI elements
    private void initViews(View view){
        name = view.findViewById(R.id.business_name_hours_editor);
        monday = view.findViewById(R.id.monday_edit);
        tuesday = view.findViewById(R.id.tuesday_edit);
        wednesday = view.findViewById(R.id.wednesday_edit);
        thursday = view.findViewById(R.id.thursday_edit);
        friday = view.findViewById(R.id.friday_edit);
        saturday = view.findViewById(R.id.saturday_edit);
        sunday = view.findViewById(R.id.sunday_edit);
        saveButton = view.findViewById(R.id.hours_save_button);
    }


    private void saveButtonSetup(){
        // if the guest presses the toolbar button they are redirected to the main menu so they can login/sign up
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setup database
                databaseHelper = new DatabaseHelper(getContext());
                db = databaseHelper.getReadableDatabase();

                String newInfo = setHours();

                ContentValues update_about = new ContentValues();
                update_about.put("profile_hours_desc", newInfo);
                db.update("profile", update_about,"owner_id=?", new String[]{String.valueOf(owner_Id)});
                db.close();
            }
        });
    }


    private void getHours(){
        hours = information.split("\n", 7);
        monday.setText(hours[0].substring(7));
        tuesday.setText(hours[1].substring(8));
        wednesday.setText(hours[2].substring(10));
        thursday.setText(hours[3].substring(9));
        friday.setText(hours[4].substring(7));
        saturday.setText(hours[5].substring(9));
        sunday.setText(hours[6].substring(7));
    }


    private String setHours(){
        if (monday.getText().equals("")) monday.setText(" ");
        if (tuesday.getText().equals("")) tuesday.setText(" ");
        if (wednesday.getText().equals("")) wednesday.setText(" ");
        if (thursday.getText().equals("")) thursday.setText(" ");
        if (friday.getText().equals("")) friday.setText(" ");
        if (saturday.getText().equals("")) saturday.setText(" ");
        if (sunday.getText().equals("")) sunday.setText(" ");
        String newHours =
                "Monday " + monday.getText() + "\n" +
                "Tuesday " + tuesday.getText() + "\n" +
                "Wednesday " + wednesday.getText() + "\n" +
                "Thursday " + thursday.getText() + "\n" +
                "Friday " + friday.getText() + "\n" +
                "Saturday " + saturday.getText() + "\n" +
                "Sunday " + sunday.getText();
        return newHours;
    }
}