package com.example.ppstart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//package com.example.ppstart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class Find_Closest_Store_Activity extends AppCompatActivity {
    FusedLocationProviderClient mFusedLocationClient;
    //TextView latitudeTextView, longitTextView;

    static ListView listview;
    static ArrayList<String> companyList;
    static ArrayList<Float> distanceList;
    static ListViewAdapter_closest_business adapter;

    // database tools
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor profile_cursor;

    int distanceRange;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_closest_store);

        //System.out.println(profile_cursor);
        //Log.d("Debug tag", profile_cursor);


        listview = findViewById(R.id.listview_business);
        companyList = new ArrayList<>();
        distanceList = new ArrayList<>();

        loadBusinesses();

        //adapter = new ListViewAdapter_closest_business(getApplicationContext(), companyList, distanceList);
        //listview.setAdapter(adapter);

        //read data from bundle concerning distange range to find stores


        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("closestBundle");
            if (bundle != null) {
                int destRange = bundle.getInt("distReq");
            }
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get User long/lat
        getLastLocation();
    }

    @SuppressLint("Missing Permission")
    private void getLastLocation() {
        //validate location permissions
        if (checkPermissions()) {
            //validate location enabled
            if (isLocationEnabled()) {                  //get location from FusedLocationClient
                //ignore, see if it allows to compile

                //See if needed?
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            String lon = Double.toString(location.getLongitude());
                            String lat = Double.toString(location.getLatitude());

                            Toast.makeText(Find_Closest_Store_Activity.this, "Lat: " + lat + "Lon: " + lon, Toast.LENGTH_LONG).show();                            //Toast.makeText(this, "adf", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on " + "your location..", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){
        //LocationRequest
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        //set request
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

        }
    };

    //check for permissions
    private boolean checkPermissions(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    //request permissions
    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    //location enable check
    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_ID){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }
    }


    //databaseHelper = new DatabaseHelper(Find_Closest_Store_Activity.this);
    //db = databaseHelper.getReadableDatabase();

    //profile_cursor = db.rawQuery("SELECT business_name FROM profile", null);

    private void loadBusinesses(){
        databaseHelper = new DatabaseHelper(Find_Closest_Store_Activity.this);
        db = databaseHelper.getWritableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT * FROM profile", null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    //companyList = new ArrayList<>();
                    //distanceList = new ArrayList<>();

                    int nameIndex = cursor.getColumnIndex("business_name");
                    for (int i = 0; i<cursor.getCount();i++){
                        companyList.add(cursor.getString(nameIndex));
                        distanceList.add(0.0F);
                    }

                    /*
                    ArrayList<Profile> browsable_profiles = new ArrayList<>();

                    int profile_id_index = cursor.getColumnIndex("profile_id");
                    int owner_id_index = cursor.getColumnIndex("owner_id");
                    int business_name_index = cursor.getColumnIndex("business_name");
                    int profile_avatar_image_index = cursor.getColumnIndex("profile_avatar_image");

                    for(int i = 0; i < cursor.getCount(); i++){
                        Profile p  = new Profile();
                        p.setProfile_id(cursor.getInt(profile_id_index));
                        p.setOwner_id(cursor.getInt(owner_id_index));
                        p.setBusiness_name(cursor.getString(business_name_index));
                        p.setProfile_avatar_image(cursor.getString(profile_avatar_image_index));

                        browsable_profiles.add(p);
                        cursor.moveToNext();
                    }

                     */
                    cursor.close();
                    db.close();
                    adapter = new ListViewAdapter_closest_business(getApplicationContext(), companyList, distanceList);
                    listview.setAdapter(adapter);

                }else{
                    cursor.close();
                    db.close();
                }
            }else{
                db.close();
            }
        }catch(SQLiteException e){
            e.printStackTrace();
        }
    }
}