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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Promo_Edit_Activity extends AppCompatActivity {
    private TextView name;
    private TextView error;
    private EditText desc;
    private Button save;
    private Button delete;
    private String item_name;
    private String promo_desc;
    private int promo_id;
    private int owner_id;
    private String mode;
    private int store_id;
    private int item_id;
    private boolean successful = false;

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
                store_id = bundle.getInt("store_id");
                item_id = bundle.getInt("item_id");
                owner_id = bundle.getInt("owner_id");
                mode = bundle.getString("mode");
            }
        }

        name = findViewById(R.id.promos_item_name_tv);
        desc = findViewById(R.id.promos_promo_desc_et);
        save = findViewById(R.id.promos_save_btn);
        delete = findViewById(R.id.promos_delete_btn);
        error = findViewById(R.id.promos_found_tv);

        name.setText(item_name);
        desc.setText(promo_desc);

        if(mode.equals("edit")) {
            delete.setVisibility(View.VISIBLE);
        }
        else{
            delete.setVisibility(View.GONE);
        }

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                databaseHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                String newDesc = desc.getText().toString();
                newDesc = newDesc.trim();

                if(mode.equals("edit")) {
                    ContentValues update_desc = new ContentValues();
                    update_desc.put("dp_desc", newDesc);
                    db.update("promos", update_desc, "dp_id = ?", new String[]{String.valueOf(promo_id)});
                    db.close();
                }
                else{
                    try {
                        Cursor pCursor = db.rawQuery("SELECT dp_id FROM promos WHERE store_id = ? and item_id = ?", new String[]{String.valueOf(store_id),String.valueOf((item_id))});
                        if (pCursor == null) {
                            ContentValues add_promo = new ContentValues();
                            add_promo.put("store_id", store_id);
                            add_promo.put("item_id", item_id);
                            add_promo.put("dp_desc", newDesc);
                            db.insert("promos", null, add_promo);
                            error.setVisibility(View.GONE);
                            successful = true;
                        }
                        else {
                            error.setVisibility(View.VISIBLE);
                            successful = false;
                        }
                        db.close();
                    }
                    catch (SQLiteException e) {
                        e.printStackTrace();
                    }
                }
                if (successful == true) {
                    Intent intent = new Intent(view.getContext(), Business_Profile_Activity.class);
                    Bundle profile_bundle = new Bundle();
                    profile_bundle.putInt("owner_id", owner_id);
                    intent.putExtra("profile_bundle", profile_bundle);
                    view.getContext().startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Promotion Updated.", Toast.LENGTH_SHORT).show();
                }
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
                Toast.makeText(getApplicationContext(), "Promotion Deleted.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
