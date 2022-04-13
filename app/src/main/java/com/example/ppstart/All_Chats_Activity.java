//CREATED BY BLAKE

package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class All_Chats_Activity extends AppCompatActivity {

    private String supporter_username;
    private int supporter_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);


        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("supporter_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
            }
        }


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle to_fragment_bundle = new Bundle();
        to_fragment_bundle.putInt("supporter_id", supporter_id);
        to_fragment_bundle.putString("supporter_username", supporter_username);
        All_Chats_Fragment fragInfo = new All_Chats_Fragment();
        fragInfo.setArguments(to_fragment_bundle);
        transaction.replace(R.id.all_chats_fragment_container, fragInfo);
        transaction.commit();


    }
}