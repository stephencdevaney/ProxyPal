//CREATED BY BLAKE

package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Create_Owner_Activity extends AppCompatActivity {

    //Declare interactive UI elements -Blake
    private EditText owner_username_txt, owner_password_txt, owner_password_confirm_txt;
    private EditText payment_card_num_txt, payment_fname_txt, payment_lname_txt, payment_address_txt, payment_city_txt, payment_state_txt, payment_postal_txt, payment_country_txt;
    private Button  finalize_create_owner_btn;
    private TextView username_warning_txt, password_warning_txt, password_confirm_warning_txt, mismatch_passwords_txt, missing_fields_warning_txt;

    //For database useage -Blake
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_owner);

        //Method that initialized the views; this keeps the code neat -Blake
        initViews();


        //set the on-click listener for the create account button -Blake
        finalize_create_owner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //These booleans are used to ensure login entries are valid
                //All booleans must be evaluated to true for the owner account to be created -Blake
                boolean valid_username;
                boolean valid_password;
                boolean valid_password_confirm;
                boolean passwords_match;
                boolean valid_payment;

                //Display a warning if the user did not enter a username -Blake
                if(owner_username_txt.getText().toString().equals("")){
                    username_warning_txt.setVisibility(View.VISIBLE);
                    valid_username = false;
                }else{
                    username_warning_txt.setVisibility(View.GONE);
                    valid_username = true;
                }
                //Display a warning if the user did not enter a password -Blake
                if(owner_password_txt.getText().toString().equals("")){
                    password_warning_txt.setVisibility(View.VISIBLE);
                    valid_password = false;
                }else{
                    password_warning_txt.setVisibility(View.GONE);
                    valid_password = true;
                }
                //Display a warning if the user did not enter a password confirmation -Blake
                if(owner_password_confirm_txt.getText().toString().equals("")){
                    password_confirm_warning_txt.setVisibility(View.VISIBLE);
                    valid_password_confirm = false;
                }else{
                    password_confirm_warning_txt.setVisibility(View.GONE);
                    valid_password_confirm = true;
                }
                //Display a warning if the passwords entered by the user do not match -Blake
                if((!owner_password_txt.getText().toString().equals(owner_password_confirm_txt.getText().toString())) &&
                        (!owner_password_txt.getText().toString().equals(""))
                        && (!owner_password_confirm_txt.getText().toString().equals(""))){
                    mismatch_passwords_txt.setVisibility(View.VISIBLE);
                    passwords_match = false;
                }else{
                    mismatch_passwords_txt.setVisibility(View.GONE);
                    passwords_match = true;
                }



                //check if all of the payment information was entered, display a warning if it was not -Blake
                if(payment_card_num_txt.getText().toString().equals("") || payment_fname_txt.getText().toString().equals("") ||
                        payment_lname_txt.getText().toString().equals("") || payment_address_txt.getText().toString().equals("") ||
                        payment_city_txt.getText().toString().equals("") || payment_state_txt.getText().toString().equals("") ||
                        payment_postal_txt.getText().toString().equals("") || payment_country_txt.getText().toString().equals("")){
                    missing_fields_warning_txt.setVisibility(View.VISIBLE);
                    valid_payment = false;
                }else{
                    missing_fields_warning_txt.setVisibility(View.GONE);
                    valid_payment = true;
                }


                //If the user has entered a username, two matching passwords, and valid payment info, then the owner account info is entered into the database -Blake
                if(valid_username && valid_password && valid_password_confirm && passwords_match && valid_payment){

                    try{
                        //Initialize the databaseHelper variable -Blake
                        databaseHelper = new DatabaseHelper(Create_Owner_Activity.this);
                        //Get a writeable instance of the database (since data is about to be inserted into it) -Blake
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        //Insert the information in the to "owner_account" table of the database -Blake
                        ContentValues owner_account = new ContentValues();
                        owner_account.put("owner_username", owner_username_txt.getText().toString());
                        owner_account.put("owner_password", owner_password_txt.getText().toString());
                        owner_account.put("card_number", payment_card_num_txt.getText().toString());
                        owner_account.put("first_name", payment_fname_txt.getText().toString());
                        owner_account.put("last_name", payment_lname_txt.getText().toString());
                        owner_account.put("address", payment_address_txt.getText().toString());
                        owner_account.put("city", payment_city_txt.getText().toString());
                        owner_account.put("state", payment_state_txt.getText().toString());
                        owner_account.put("postal", payment_postal_txt.getText().toString());
                        owner_account.put("country", payment_country_txt.getText().toString());

                        db.insert("owner_account", null, owner_account);
                        db.close();


                        //switch to the Business_Profile_Activity activity after the owner account is added to the database -Blake
                        Intent to_business_profile = new Intent(Create_Owner_Activity.this, Business_Profile_Activity.class);
                        startActivity(to_business_profile);

                    }catch(SQLException e){
                        e.printStackTrace();
                    }


                }

            }
        });
    }


    private void initViews(){

        //Initialize interactive UI elements
        owner_username_txt = findViewById(R.id.owner_username_txt);
        owner_password_txt = findViewById(R.id.owner_password_txt);
        owner_password_confirm_txt = findViewById(R.id.owner_password_confirm_txt);
        payment_card_num_txt = findViewById(R.id.payment_card_num_txt);
        payment_fname_txt = findViewById(R.id.payment_fname_txt);
        payment_lname_txt = findViewById(R.id.payment_lname_txt);
        payment_address_txt = findViewById(R.id.payment_address_txt);
        payment_city_txt = findViewById(R.id.payment_city_txt);
        payment_state_txt = findViewById(R.id.payment_state_txt);
        payment_postal_txt = findViewById(R.id.payment_postal_txt);
        payment_country_txt = findViewById(R.id.payment_country_txt);
        finalize_create_owner_btn = findViewById(R.id.finalize_create_owner_btn);

        //initialize warnings
        username_warning_txt = findViewById(R.id.username_warning_txt);
        password_warning_txt = findViewById(R.id.password_warning_txt);
        password_confirm_warning_txt = findViewById(R.id.password_confirm_warning_txt);
        mismatch_passwords_txt = findViewById(R.id.mismatch_passwords_txt);
        missing_fields_warning_txt = findViewById(R.id.missing_fields_warning_txt);
    }
}