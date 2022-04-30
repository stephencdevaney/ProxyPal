package com.example.ppstart;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Promo_Edit_Activity extends AppCompatActivity {
    private TextView name;
    private EditText desc;
    private Button save;
    private Button delete;
    private String item_name;
    private String promo_desc;
    private int promo_id;
    private int owner_id;

    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_edit);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("promo_edit_bundle");
            if (bundle != null) {
                item_name = bundle.getString("item_name");
                promo_desc = bundle.getString("promo_desc");
                promo_id = bundle.getInt("promo_id");
                owner_id = bundle.getInt("owner_id");
            }
        }

        name = findViewById(R.id.promos_item_name_tv);
        desc = findViewById(R.id.promos_promo_desc_et);
        save = findViewById(R.id.promos_save_btn);
        delete = findViewById(R.id.promos_delete_btn);

        name.setText(item_name);
        desc.setText(promo_desc);

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                databaseHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                String newDesc = desc.getText().toString();
                newDesc = newDesc.trim();

                ContentValues update_desc = new ContentValues();
                update_desc.put("dp_desc", newDesc);
                db.update("promos", update_desc,"dp_id = ?", new String[]{String.valueOf(promo_id)});
                db.close();

                Intent intent = new Intent(view.getContext(), Business_Profile_Activity.class);
                Bundle profile_bundle = new Bundle();
                profile_bundle.putInt("owner_id", owner_id);
                intent.putExtra("profile_bundle", profile_bundle);
                view.getContext().startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                databaseHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                db.delete("promos","dp_id = ?", new String[]{String.valueOf(promo_id)});
                db.close();

                Intent intent = new Intent(view.getContext(), Business_Profile_Activity.class);
                Bundle profile_bundle = new Bundle();
                profile_bundle.putInt("owner_id", owner_id);
                intent.putExtra("profile_bundle", profile_bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

}
