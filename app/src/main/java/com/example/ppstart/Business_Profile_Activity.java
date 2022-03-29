package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Business_Profile_Activity extends AppCompatActivity {

    private TextView test_txt;
    private int owner_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        test_txt = findViewById(R.id.test_txt);

        //receive the bundle passed from the BrowseProfilesAdapter (containing the owner_id of the business profile)
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getBundleExtra("profile_bundle");
            if(bundle != null){
                test_txt.setText(String.valueOf(bundle.getInt("owner_id")));
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}