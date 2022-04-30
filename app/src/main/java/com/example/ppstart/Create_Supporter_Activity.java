//CREATED BY BLAKE

package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Create_Supporter_Activity extends AppCompatActivity {

    //Declare interactive UI elements
    private EditText supporter_username_txt, supporter_password_txt, supporter_password_confirm_txt;
    private Button finalize_create_supporter_btn;
    private TextView username_warning_txt, password_warning_txt, password_confirm_warning_txt, mismatch_passwords_txt;

    //For testing strings entered into edit text elements -Blake
    private TextView supporter_test_txt;


    //For storing strings that the user enters into the EditText boxes
    private String supporter_username_input = "";
    private String supporter_password_input = "";
    private String supporter_password_confirm_input = "";

    private String supporter_username;
    private int supporter_id;

    //For database useage -Blake
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_supporter);

        //Initialize interactive UI elements -Blake
        supporter_username_txt = findViewById(R.id.supporter_username_txt);
        supporter_password_txt = findViewById(R.id.supporter_password_txt);
        supporter_password_confirm_txt = findViewById(R.id.supporter_password_confirm_txt);
        finalize_create_supporter_btn = findViewById(R.id.finalize_create_supporter_btn);
        username_warning_txt = findViewById(R.id.username_warning_txt);
        password_warning_txt = findViewById(R.id.password_warning_txt);
        password_confirm_warning_txt = findViewById(R.id.password_confirm_warning_txt);
        mismatch_passwords_txt = findViewById(R.id.mismatch_passwords_txt);

        //for testing -Blake
        supporter_test_txt = findViewById(R.id.supporter_test_txt);

        //set the on-click listener for the create account button -Blake
        finalize_create_supporter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                supporter_username_input = supporter_username_txt.getText().toString();
                supporter_password_input = supporter_password_txt.getText().toString();
                supporter_password_confirm_input = supporter_password_confirm_txt.getText().toString();




                //These booleans are used to ensure login entries are valid
                //All booleans must be evaluated to true for the owner account to be created -Blake
                boolean valid_username;
                boolean valid_password;
                boolean valid_password_confirm;
                boolean passwords_match;

                //Display a warning if the user did not enter a username -Blake
                if(supporter_username_txt.getText().toString().equals("")){
                    username_warning_txt.setVisibility(View.VISIBLE);
                    valid_username = false;
                }else{
                    username_warning_txt.setVisibility(View.GONE);
                    valid_username = true;
                }
                //Display a warning if the user did not enter a password -Blake
                if(supporter_password_txt.getText().toString().equals("")){
                    password_warning_txt.setVisibility(View.VISIBLE);
                    valid_password = false;
                }else{
                    password_warning_txt.setVisibility(View.GONE);
                    valid_password = true;
                }
                //Display a warning if the user did not enter a password confirmation -Blake
                if(supporter_password_confirm_txt.getText().toString().equals("")){
                    password_confirm_warning_txt.setVisibility(View.VISIBLE);
                    valid_password_confirm = false;
                }else{
                    password_confirm_warning_txt.setVisibility(View.GONE);
                    valid_password_confirm = true;
                }
                //Display a warning if the passwords entered by the user do not match -Blake
                if((!supporter_password_txt.getText().toString().equals(supporter_password_confirm_txt.getText().toString())) &&
                        (!supporter_password_txt.getText().toString().equals(""))
                        && (!supporter_password_confirm_txt.getText().toString().equals(""))){
                    mismatch_passwords_txt.setVisibility(View.VISIBLE);
                    passwords_match = false;
                }else{
                    mismatch_passwords_txt.setVisibility(View.GONE);
                    passwords_match = true;
                }



                //If the user has entered a username and two matching passwords, then the account info is entered into the database -Blake
                if(valid_username && valid_password && valid_password_confirm && passwords_match){

                    boolean username_exists = false;

                    try{

                        //Initialize the databaseHelper variable
                        databaseHelper = new DatabaseHelper(Create_Supporter_Activity.this);
                        //Get a writeable instance of the database since data is about to be inserted into it -Blake
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();



                         Cursor cursor = db.rawQuery("SELECT * FROM supporter_account", null);
                            if(cursor != null){
                                if(cursor.moveToFirst()){

                                    int supporter_username_index = cursor.getColumnIndex("supporter_username");

                                    for(int i = 0; i < cursor.getCount(); i++){
                                        if(cursor.getString(supporter_username_index).equals(supporter_username_txt.getText().toString())){
                                            Toast.makeText(Create_Supporter_Activity.this, "Username already exists, please enter another username.", Toast.LENGTH_SHORT).show();
                                            username_exists = true;
                                        }

                                        cursor.moveToNext();
                                    }
                                    cursor.close();
                                }else{
                                    //Don't forget to close the cursors and database instance!
                                    cursor.close();
                                }
                            }



                        if(!username_exists){
                            //Insert the username and password of the created supporter account into the "supporter_account" table of the db -Blake
                            ContentValues supporter_account = new ContentValues();
                            supporter_account.put("supporter_username", supporter_username_txt.getText().toString());
                            supporter_account.put("supporter_password", supporter_password_txt.getText().toString());

                            db.insert("supporter_account", null, supporter_account);
                            db.close();

                            Intent to_main = new Intent(Create_Supporter_Activity.this, MainActivity.class);

                            Toast.makeText(Create_Supporter_Activity.this, "Account created, you can now login.", Toast.LENGTH_SHORT).show();
                            startActivity(to_main);
                        }




                    }catch(SQLException e){
                        e.printStackTrace();
                    }


                }

            }
        });

    }



}