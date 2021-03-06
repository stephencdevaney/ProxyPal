package com.example.ppstart;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

public class business_profile_textEditor extends Fragment {
    private TextView name;
    private EditText info;
    private Button saveButton;

    // database tools
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private int owner_Id;
    private String business_name;
    private String information;

    public business_profile_textEditor() {
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
        View view = inflater.inflate(R.layout.fragment_business_profile_text_editor, container, false);
        initViews(view);

        business_name = "Business Name";
        Bundle main_fragment_bundle = this.getArguments();
        if(main_fragment_bundle != null){
            business_name = main_fragment_bundle.getString("name");
            information = main_fragment_bundle.getString("info");
            owner_Id = main_fragment_bundle.getInt("owner_id");
        }

        name.setText(business_name);
        info.setText(information);
        saveButtonSetup();
        return view;
    }

    //initialize UI elements
    private void initViews(View view){
        name = view.findViewById(R.id.business_name_text_editor);
        info = view.findViewById(R.id.edit_business_info);
        saveButton = view.findViewById(R.id.about_save_button);
    }

    private void saveButtonSetup(){
        // if the guest presses the toolbar button they are redirected to the main menu so they can login/sign up
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setup database
                databaseHelper = new DatabaseHelper(getContext());
                db = databaseHelper.getReadableDatabase();

                String Info = removeHeader(info.getText().toString());

                ContentValues update_about = new ContentValues();
                update_about.put("profile_about_desc", Info);
                db.update("profile", update_about,"owner_id=?", new String[]{String.valueOf(owner_Id)});
                db.close();
            }
        });
    }

    // Remove about header from string
    public String removeHeader(String info){
        String newInfo = info;
        if(newInfo.substring(0,5).toLowerCase(Locale.ROOT).equals("about")){
            newInfo = newInfo.substring(5);
        }
        newInfo = newInfo.trim();
        return newInfo;
    }
}