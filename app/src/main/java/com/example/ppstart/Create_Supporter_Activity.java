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

public class Create_Supporter_Activity extends AppCompatActivity {

    //Declare interactive UI elements
    private EditText supporter_username_txt, supporter_password_txt, supporter_password_confirm_txt;
    private Button finalize_create_supporter_btn;
    private TextView username_warning_txt, password_warning_txt, password_confirm_warning_txt, mismatch_passwords_txt;

    //For testing strings entered into edit text elements
    private TextView supporter_test_txt;
    private String supporter_username_input = "";
    private String supporter_password_input = "";
    private String supporter_password_confirm_input = "";

    private String supporter_username;
    private int supporter_id;

    //For database useage
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_supporter);

        //Initialize interactive UI elements
        supporter_username_txt = findViewById(R.id.supporter_username_txt);
        supporter_password_txt = findViewById(R.id.supporter_password_txt);
        supporter_password_confirm_txt = findViewById(R.id.supporter_password_confirm_txt);
        finalize_create_supporter_btn = findViewById(R.id.finalize_create_supporter_btn);
        username_warning_txt = findViewById(R.id.username_warning_txt);
        password_warning_txt = findViewById(R.id.password_warning_txt);
        password_confirm_warning_txt = findViewById(R.id.password_confirm_warning_txt);
        mismatch_passwords_txt = findViewById(R.id.mismatch_passwords_txt);

        //for testing
        supporter_test_txt = findViewById(R.id.supporter_test_txt);

        //set the on-click listener for the create account button
        finalize_create_supporter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid_username;
                boolean valid_password;
                boolean valid_password_confirm;
                boolean passwords_match;

                //Display a warning if the user did not enter a username
                if(supporter_username_txt.getText().toString().equals("")){
                    username_warning_txt.setVisibility(View.VISIBLE);
                    valid_username = false;
                }else{
                    username_warning_txt.setVisibility(View.GONE);
                    valid_username = true;
                }
                //Display a warning if the user did not enter a password
                if(supporter_password_txt.getText().toString().equals("")){
                    password_warning_txt.setVisibility(View.VISIBLE);
                    valid_password = false;
                }else{
                    password_warning_txt.setVisibility(View.GONE);
                    valid_password = true;
                }
                //Display a warning if the user did not enter a password confirmation
                if(supporter_password_confirm_txt.getText().toString().equals("")){
                    password_confirm_warning_txt.setVisibility(View.VISIBLE);
                    valid_password_confirm = false;
                }else{
                    password_confirm_warning_txt.setVisibility(View.GONE);
                    valid_password_confirm = true;
                }
                //Display a warning if the passwords entered by the user do not match
                if((!supporter_password_txt.getText().toString().equals(supporter_password_confirm_txt.getText().toString())) &&
                        (!supporter_password_txt.getText().toString().equals(""))
                        && (!supporter_password_confirm_txt.getText().toString().equals(""))){
                    mismatch_passwords_txt.setVisibility(View.VISIBLE);
                    passwords_match = false;
                }else{
                    mismatch_passwords_txt.setVisibility(View.GONE);
                    passwords_match = true;
                }

                //If the user has entered a username and two matching passwords, then the account info is entered into the database
                if(valid_username && valid_password && valid_password_confirm && passwords_match){

                    try{
                        databaseHelper = new DatabaseHelper(Create_Supporter_Activity.this);
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        ContentValues supporter_account = new ContentValues();
                        supporter_account.put("supporter_username", supporter_username_txt.getText().toString());
                        supporter_account.put("supporter_password", supporter_password_txt.getText().toString());

                        db.insert("supporter_account", null, supporter_account);
                        db.close();

                        supporter_username = supporter_username_txt.getText().toString(); //save the username
                        //supporter_id = db.rawQuery("SELECT supporter_"); //save the id


                        //switch to supporter account main activity, passing the supporter_username/id
                        Intent to_browse = new Intent(Create_Supporter_Activity.this, Supporter_Main_Page_Activity.class);
                        Bundle supporter_bundle = new Bundle();
                        supporter_bundle.putInt("supporter_id", supporter_id);
                        supporter_bundle.putString("supporter_username", supporter_username);
                        to_browse.putExtra("supporter_bundle", supporter_bundle);

                        startActivity(to_browse);


                    }catch(SQLException e){
                        e.printStackTrace();
                    }


                }

            }
        });

    }

    //clear warnings if necessary
    @Override
    protected void onResume() {
        super.onResume();
        username_warning_txt.setVisibility(View.GONE);
        password_warning_txt.setVisibility(View.GONE);
        password_confirm_warning_txt.setVisibility(View.GONE);
    }
}