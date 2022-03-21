package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Initial_Create_Activity extends AppCompatActivity {

    //Declare the interactive UI elements for the initial Create Account page
    private Button create_supporter_btn, create_owner_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_create);

        //Initialize the create owner and create supporter account buttons
        create_supporter_btn = findViewById(R.id.create_supporter_btn);
        create_owner_btn = findViewById(R.id.create_owner_btn);

        //Set the on-click listener for the create supporter button
        create_supporter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When the create supporter account button is clicked, the app will switch to
                //the supporter account creation page
                Intent to_create_supporter = new Intent(Initial_Create_Activity.this, Create_Supporter_Activity.class);
                startActivity(to_create_supporter);
            }
        });

        //Set the on-click listener for the create owner account button
        create_owner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When the create owner account button is clicked, the app will switch to
                //the owner account creation page
                Intent to_create_owner = new Intent(Initial_Create_Activity.this, Create_Owner_Activity.class);
                startActivity(to_create_owner);
            }
        });

    }
}