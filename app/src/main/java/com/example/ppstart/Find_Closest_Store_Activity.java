package com.example.ppstart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.ListView;
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
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Find_Closest_Store_Activity extends AppCompatActivity {
    FusedLocationProviderClient mFusedLocationClient;
    ListView listview;
    static ArrayList<String> companyList;
    static ArrayList<Float> percentMatchList;
    static ArrayList<String> custList;
    static ArrayList<String> storeItemList;
    static ArrayList<String> tempList;
    static double destRange;
    static double[] location;
    ListViewAdapter_closest_business adapter;

    private Cursor profile_cursor;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_closest_store);

        listview = findViewById(R.id.listview_business);
        Intent intent = getIntent();
        double destRange=0;
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("closestBundle");
            if (bundle != null) {
                destRange = bundle.getInt("distReq");
            }
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location = new double[2];
        //debugDB();
        // method to get User long/lat
        getLastLocation(destRange);
    }

    @SuppressLint("Missing Permission")
    private void getLastLocation(double destRange) {


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
                            //Toast.makeText(Find_Closest_Store_Activity.this, "Lat: " + lat + "Lon: " + lon, Toast.LENGTH_LONG).show();
                            loadBusiness(lon, lat, destRange);

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
                getLastLocation(destRange);
            }
        }
    }

    private void loadBusiness(double lon, double lat, double destRange){
        storeItemList = new ArrayList<>();
        percentMatchList = new ArrayList<>();
        companyList = new ArrayList<>();
        tempList = new ArrayList<>();

        System.out.println("This is a test\n");

        //initialize database
        DatabaseHelper databaseHelper = new DatabaseHelper(Find_Closest_Store_Activity.this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor business_cursor = db.rawQuery("SELECT owner_id, business_name, latitude, longitude FROM profile", null);

        if(business_cursor != null){
            if(business_cursor.moveToFirst()){
                int business_id_index =     business_cursor.getColumnIndex("owner_id");
                int business_name_index =   business_cursor.getColumnIndex("business_name");
                int lat_index =             business_cursor.getColumnIndex("latitude");
                int lon_index =             business_cursor.getColumnIndex("longitude");

                for(int i = 0; i <  business_cursor.getCount(); i++) {
                    String business_id = "%"+business_cursor.getString(business_id_index)+"%";
                    String business_name =  business_cursor.getString(business_name_index);
                    String businessLat = business_cursor.getString(lat_index);
                    String businessLon = business_cursor.getString(lon_index);
                    System.out.println(business_name + " -> " + business_id);

                    double x = (Float.parseFloat(businessLat) - lat);
                    double y = (Float.parseFloat(businessLon) - lon);
                    //only include if in range
                    double result = Math.sqrt(x*x + y*y);
                    if (destRange/55 < result){

                        //pull item data from database to build list
                        //WHERE owner_id =  " + business_id
                        //SQLiteDatabase db1 = databaseHelper.getReadableDatabase();
                        Cursor item_number_cursor = db.rawQuery("SELECT * " +
                                        "FROM store_inventory " +
                                        "WHERE owner_id LIKE ?"
                                , new String[]{business_id});
                        if (item_number_cursor!=null){
                            //if(item_number_cursor.moveToFirst()){
                            //int item_number_index = item_number_cursor.getColumnIndex("item_number");

                            //for (int k = 0; k < item_number_cursor.getCount(); k++){
                            while(item_number_cursor.moveToNext()){
                                //System.out.print("->"+ item_number_cursor.getCount()+ ": ");
                                String item_number = item_number_cursor.getString(0);
                                System.out.println("\t"+ item_number);
                                Cursor item_name_cursor = db.rawQuery("SELECT * " +
                                        "FROM item " +
                                        "WHERE item_number " +
                                        "LIKE ?", new String[]{item_number});
                                if(item_name_cursor != null){
                                    while(item_name_cursor.moveToNext()){
                                        //int item_name_index = item_name_cursor.getColumnIndex("item_name");
                                        //item_name_cursor.moveToNext();
                                        String item_name = item_name_cursor.getString(1);
                                        //System.out.println("\t\t"+item_name);
                                        storeItemList.add(item_name);
                                        //jump back to
                                        item_name_cursor.close();
                                        System.out.println("inside loop0: "+storeItemList);
                                    }

                                }
                            }
                            item_number_cursor.close();
                        }

                        System.out.println("inside loop1: "+storeItemList);
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
                                System.out.println("inside ITEM.txt: "+storeItemList);
                                System.out.println(custList);
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

                    }
                    business_cursor.moveToNext();
                }

                business_cursor.close();
            }
            db.close();
        }

        //build adapter off resulting business information
        for (int i =0; i<percentMatchList.size(); i++){
            for(int j=percentMatchList.size()-1; j>i; j--){
                if(percentMatchList.get(i)<percentMatchList.get(j)){
                    float temp1 = percentMatchList.get(i);
                    String temp2 = companyList.get(i);

                    percentMatchList.set(i, percentMatchList.get(j));
                    percentMatchList.set(j, temp1);
                    companyList.set(i, companyList.get(i));
                    companyList.set(j, temp2);
                }
            }
        }
        adapter = new ListViewAdapter_closest_business(getApplicationContext(), companyList, percentMatchList);
        listview.setAdapter(adapter);
    }
}