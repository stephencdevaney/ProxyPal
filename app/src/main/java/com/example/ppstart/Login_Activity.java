package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login_Activity extends AppCompatActivity {

    private EditText login_username_txt, login_password_txt;
    private Button confirm_login_btn;
    private DatabaseHelper databaseHelper;
    private TextView no_login_match_warning_txt;

    private Boolean supporter_username_match = false;
    private Boolean supporter_password_match = false;

    private Boolean owner_username_match = false;
    private Boolean owner_password_match = false;

    private String supporter_username;
    private int supporter_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize UI elements
        login_username_txt = findViewById(R.id.login_username_txt);
        login_password_txt = findViewById(R.id.login_password_txt);
        confirm_login_btn = findViewById(R.id.confirm_login_btn);
        no_login_match_warning_txt = findViewById(R.id.no_login_match_warning_txt);


        //set the on-click listener for the confirm login button
        confirm_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get an instance of the database
                databaseHelper = new DatabaseHelper(Login_Activity.this);
                SQLiteDatabase db = databaseHelper.getReadableDatabase();

                //query the database for supporter accounts
                Cursor supporter_cursor = db.rawQuery("SELECT supporter_id, supporter_username, supporter_password FROM supporter_account", null);

                //query the database for owner accounts
                Cursor owner_cursor = db.rawQuery("SELECT owner_id, owner_username, owner_password FROM owner_account", null);

                //check for a supporter username and password match
                if(supporter_cursor != null){
                    if(supporter_cursor.moveToFirst()){
                        int supporter_id_index = supporter_cursor.getColumnIndex("supporter_id");
                        int supporter_username_index = supporter_cursor.getColumnIndex("supporter_username");
                        int supporter_password_index = supporter_cursor.getColumnIndex("supporter_password");

                        for(int i = 0; i < supporter_cursor.getCount(); i++){

                            if(login_username_txt.getText().toString().equals(supporter_cursor.getString(supporter_username_index))){
                                supporter_username_match = true;
                                supporter_username = supporter_cursor.getString(supporter_username_index); //save the username
                                supporter_id = supporter_cursor.getInt(supporter_id_index); //save the id
                            }
                            if(login_password_txt.getText().toString().equals(supporter_cursor.getString(supporter_password_index))){
                                supporter_password_match = true;
                            }

                            supporter_cursor.moveToNext();
                        }
                        supporter_cursor.close();
                    }
                }

                //check for an owner username and password match
                if(owner_cursor != null){
                    if(owner_cursor.moveToFirst()){
                        int owner_id_index = owner_cursor.getColumnIndex("owner_id");
                        int owner_username_index = owner_cursor.getColumnIndex("owner_username");
                        int owner_password_index = owner_cursor.getColumnIndex("owner_password");

                        for(int i = 0; i < owner_cursor.getCount(); i++){
                            if(login_username_txt.getText().toString().equals(owner_cursor.getString(owner_username_index))){
                                owner_username_match = true;
                            }
                            if(login_password_txt.getText().toString().equals(owner_cursor.getString(owner_password_index))){
                                owner_password_match = true;
                            }

                            owner_cursor.moveToNext();
                        }



                    }
                }
                db.close();

                //log the user in if the username and password matched a supporter account
                if(supporter_username_match && supporter_password_match){
                    Intent to_browse = new Intent(Login_Activity.this, Supporter_Main_Page_Activity.class);

                    //pass the supporter account id and supporter account username to the main supporter browsing page activity
                    Bundle supporter_bundle = new Bundle();
                    supporter_bundle.putInt("supporter_id", supporter_id);
                    supporter_bundle.putString("supporter_username", supporter_username);
                    to_browse.putExtra("supporter_bundle", supporter_bundle);
                    //switch to the Supporter_Main_Page_Activity activity
                    startActivity(to_browse);



                    //log the user in if the username and password matched an owner account
                }else if(owner_username_match && owner_password_match){
                    Intent to_business_profile = new Intent(Login_Activity.this, Business_Profile_Activity.class);
                    //switch to the Business_Profile_Activity activity
                    startActivity(to_business_profile);


                    //display a warning if there was no account in the database that matched the entered username and password
                }else{
                    no_login_match_warning_txt.setVisibility(View.VISIBLE);
                }

            }
        });




    }
}