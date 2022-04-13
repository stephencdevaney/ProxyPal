package com.example.ppstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Shopping_List_Activity extends AppCompatActivity {

    private BottomNavigationView supporter_bottom_nav_menu;


    static ListView listView;
    static ArrayList<String> items;
    static ListViewAdapter adapter;

    EditText input;
    ImageView enter;
    String text;
    Toast t;


    private String supporter_username;
    private int supporter_id;

    private Bundle supporter_bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("supporter_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
            }
        }

        //initialize navigation bar
        supporter_bottom_nav_menu = findViewById(R.id.supporter_bottom_nav_menu);
        initBottomNavigationView();

        listView = findViewById(R.id.listview);
        input = findViewById(R.id.itemInput);
        enter = findViewById(R.id.add);
        items = new ArrayList<>();

        loadContent();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) listView.getItemAtPosition(position);
                Toast.makeText(Shopping_List_Activity.this, clickedItem, Toast.LENGTH_SHORT).show();
                //String itemName = items.get(i);
                //makeToast(itemName);
            }
        });

        //Alternate removal
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeToast(items.get(i) + " Removed!");

                removeItem(i);
                return false;
            }
        });

        adapter = new ListViewAdapter(getApplicationContext(), items);
        listView.setAdapter(adapter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = input.getText().toString();
                if (text == null || text.length() == 0) {
                    makeToast("LOL, nice try");
                } else {
                    addItem(text);
                    input.setText("");
                    makeToast(text + " Added to List!");
                }
            }
        });

    }
    //Load content from local file into ListView
    public void loadContent() {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "list.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try{
            stream = new FileInputStream(readFrom);
            stream.read(content);

            String s = new String(content);
            //formatted as [item1, item2, item3]
            //cut off brackets in list
            s = s.substring(1, s.length()-1);
            //split on ", "
            String split[] = s.split(", ");

             //if file empty
            if(split.length == 1 && split[0].isEmpty()){
                items = new ArrayList<>();
            }
            else{
                items = new ArrayList<>(Arrays.asList(split));
            }
            adapter = new ListViewAdapter(this, items);
            listView.setAdapter(adapter);
         } catch(Exception e){
            e.printStackTrace();
        }
    }

    //Save list data when app closed
    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try{
            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));
            writer.write(items.toString().getBytes());
            writer.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    //Save data when backing out of app as well
    @Override
    public void onBackPressed() {
        File path = getApplicationContext().getFilesDir();
        try{
            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));
            writer.write(items.toString().getBytes());
            writer.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        super.onBackPressed();
        this.finish();
    }

    public static void addItem(String item){
        items.add(item);
        listView.setAdapter(adapter);
    }
    public static void removeItem(int removalIndex){
        items.remove(removalIndex);
        listView.setAdapter(adapter);
    }

    //toast prints message about what item is clicked
    private void makeToast(String s) {
        if(t!=null){
            t.cancel();
        }
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }

    private void initBottomNavigationView() {
        //sets the shopping page as the default page
        supporter_bottom_nav_menu.setSelectedItemId(R.id.shopping);

        //Set the listener for the bottom navigation menu buttons
        //(note: use this since the onNavigationItemSelected is deprecated)
        supporter_bottom_nav_menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //switch to the Supporter_Search_Activity activity when the "search" button at the bottom is tapped
                case R.id.search:
                    Intent to_search = new Intent(Shopping_List_Activity.this, Supporter_Search_Activity.class);
                    supporter_bundle = new Bundle();
                    supporter_bundle.putInt("supporter_id", supporter_id);
                    supporter_bundle.putString("supporter_username", supporter_username);
                    to_search.putExtra("supporter_bundle", supporter_bundle);
                    startActivity(to_search);
                    break;
                //switch to the Supporter_Main_Page_Activity activity when the "explore" button at the bottom is tapped
                case R.id.explore:
                    Intent to_explore = new Intent(Shopping_List_Activity.this, Supporter_Main_Page_Activity.class);
                    supporter_bundle = new Bundle();
                    supporter_bundle.putInt("supporter_id", supporter_id);
                    supporter_bundle.putString("supporter_username", supporter_username);
                    to_explore.putExtra("supporter_bundle", supporter_bundle);
                    startActivity(to_explore);
                    break;
                //switch to the Discounts_Promos_Activity activity when the "Discounts & Promos" button at the bottom is tapped
                case R.id.discounts_and_promos:
                    Intent to_promos = new Intent(Shopping_List_Activity.this, Discounts_Promos_Activity.class);
                    supporter_bundle = new Bundle();
                    supporter_bundle.putInt("supporter_id", supporter_id);
                    supporter_bundle.putString("supporter_username", supporter_username);
                    to_promos.putExtra("supporter_bundle", supporter_bundle);
                    startActivity(to_promos);
                    break;
                //switch to the Shopping_List_Activity activity when the "Shopping List" button at the bottom is tapped
                case R.id.shopping:
                    break;
                default:
                    break;
            }

            return true;
        });


    }
}