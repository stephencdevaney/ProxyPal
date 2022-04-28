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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Find_Closest_Store_Activity extends AppCompatActivity {
    FusedLocationProviderClient mFusedLocationClient;
    //TextView latitudeTextView, longitTextView;

    static ListView listview;
    static ArrayList<String> companyList;
    static ArrayList<Float> percentMatchList;
    static ArrayList<String> custList;
    static ArrayList<String> tempList;

    static double[] location;
    static ArrayList<String> tempItemNumber;
    static ArrayList<String> storeItemList;

    static ListViewAdapter_closest_business adapter;

    // database tools
    //private DatabaseHelper databaseHelper;
    private Cursor profile_cursor;

    int distanceRange;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_closest_store);

        listview = findViewById(R.id.listview_business);

        //loadBusinesses();


        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("closestBundle");
            if (bundle != null) {
                int destRange = bundle.getInt("distReq");
            }
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location = new double[2];

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
                    // for ActivityCompat#requestPermissions for more details
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            double lon = location.getLongitude();
                            double lat = location.getLatitude();
                            //Toast.makeText(Find_Closest_Store_Activity.this, "Lat: " + lat + "Lon: " + lon, Toast.LENGTH_LONG).show();                            //Toast.makeText(this, "adf", Toast.LENGTH_LONG).show();

                            storeItemList = new ArrayList<>();
                            percentMatchList = new ArrayList<>();
                            companyList = new ArrayList<>();
                            tempList = new ArrayList<>();


                            //initialize database
                            DatabaseHelper databaseHelper = new DatabaseHelper(Find_Closest_Store_Activity.this);
                            SQLiteDatabase db = databaseHelper.getReadableDatabase();

                            Cursor business_cursor = db.rawQuery("SELECT owner_id, business_name FROM profile", null);

                            if(business_cursor != null){
                                if(business_cursor.moveToFirst()){
                                    int business_id_index =     business_cursor.getColumnIndex("owner_id");
                                    int business_name_index =   business_cursor.getColumnIndex("business_name");



                                    for(int i = 0; i < business_cursor.getCount(); i++) {
                                        int business_id = business_cursor.getInt(business_id_index);
                                        String business_name =  business_cursor.getString(business_name_index);



                                        //pull item data from database to build list
                                        //WHERE owner_id =  " + business_id
                                        SQLiteDatabase db1 = databaseHelper.getReadableDatabase();
                                        Cursor item_number_cursor = db1.rawQuery("SELECT item_number FROM store_inventory WHERE item_number='" + business_id + "'" , null);
                                        if (item_number_cursor!=null){
                                            if(item_number_cursor.moveToFirst()){
                                                int item_number_index = item_number_cursor.getColumnIndex("item_number");

                                                for (int j = 0; j < item_number_cursor.getCount(); j++){
                                                    String item_number = item_number_cursor.getString(item_number_index);

                                                    Cursor item_name_cursor = db.rawQuery("SELECT item_name FROM item WHERE item_number='" + item_number + "'", null);
                                                    if(item_name_cursor != null){
                                                        if(item_name_cursor.moveToFirst()){
                                                            int item_name_index = item_name_cursor.getColumnIndex("item_name");
                                                            String item_name = item_name_cursor.getString(item_name_index);
                                                            storeItemList.add(item_name);
                                                            //jump back to
                                                            item_name_cursor.close();
                                                        }
                                                    }


                                                    item_number_cursor.moveToNext();
                                                }
                                                item_number_cursor.close();
                                                db1.close();
                                            }
                                        }


                                        //pull up customers shopping list
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
                                                float match = 0;
                                                percentMatchList.add(match);
                                            }
                                            else{
                                                custList = new ArrayList<>(Arrays.asList(split));
                                                int custListLen = custList.size();
                                                custList.retainAll(storeItemList);
                                                int matchCount = custList.size();
                                                float match = 100 * matchCount/custListLen;
                                                custList.clear();
                                                storeItemList.clear();
                                                percentMatchList.add(match);
                                            }
                                        } catch(Exception e){
                                            e.printStackTrace();
                                        }

                                        companyList.add(business_name);
                                        business_cursor.moveToNext();
                                    }
                                    business_cursor.close();
                                }
                                db.close();
                            }

                            //build adapter off resulting business information
                            adapter = new ListViewAdapter_closest_business(getApplicationContext(), companyList, percentMatchList);
                            listview.setAdapter(adapter);
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

    private double distance(double lonDist, double latDist) {
        Math.sqrt(lonDist*lonDist + latDist*latDist);
        return 0;
    }
}